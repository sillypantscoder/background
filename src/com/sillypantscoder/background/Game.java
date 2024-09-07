package com.sillypantscoder.background;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.sillypantscoder.background.Box.PhysicsState;
import com.sillypantscoder.utils.ListCombination;
import com.sillypantscoder.utils.Rect;

/**
 * This class contains all of the data needed to run the game.
 */
public class Game {
	public static final boolean CHEAT = true;
	public static final boolean SHOW_TIMER = false;
	public static final boolean SAVE_TIMES = false;
	public Boxes.Player player1;
	public Boxes.Player player2;
	public boolean switchedPlayer;
	public ArrayList<ArrayList<Box>> layers;
	public Set<String> keys;
	public double cameraX;
	public double cameraY;
	public int endingAnimation;
	public boolean levelCompleted;
	public int level = 0;
	public int timer = 0;
	public Game() {
		keys = new HashSet<String>();
		layers = new ArrayList<ArrayList<Box>>();
		// Level
		generateLevel();
	}
	/**
	 * Get the layer with the specified number.
	 * Creates the layer if it does not exist.
	 */
	public ArrayList<Box> getLayer(int layer) {
		while (layers.size() <= layer) {
			layers.add(new ArrayList<Box>());
		}
		return layers.get(layer);
	}
	/**
	 * Get a ListCombination that behaves as if it is on multiple layers.
	 */
	public ListCombination<Box> getMultilayer(int[] layers) {
		ArrayList<ArrayList<Box>> layerList = new ArrayList<ArrayList<Box>>();
		for (int i = 0; i < layers.length; i++) {
			layerList.add(this.getLayer(layers[i]));
		}
		return new ListCombination<Box>(layerList);
	}
	/**
	 * Generate the level by finding the appropriate method in Levels.
	 */
	public void generateLevel() {
		ArrayList<Consumer<Game>> levels = new ArrayList<Consumer<Game>>();
		levels.add(Levels::level0);
		levels.add(Levels::level1);
		levels.add(Levels::level2);
		levels.add(Levels::level3);
		levels.add(Levels::level4);
		levels.add(Levels::level5);
		levels.add(Levels::level6);
		levels.add(Levels::level7);
		levels.add(Levels::level8);
		levels.add(Levels::level9);
		levels.add(Levels::level10);
		if (level >= levels.size()) {
			Levels.levelErr(this);
		} else {
			levels.get(level).accept(this);
		}
		timer = 0;
	}
	/**
	 * Get the currently active player.
	 */
	public Boxes.Player getPlayer() { return switchedPlayer ? player2 : player1; }
	public double getTargetCameraX(int width) { return (getPlayer().rect.centerX() * 50) - (width / 2d); }
	public double getTargetCameraY(int height) { return (getPlayer().rect.centerY() * 50) - (height / 2d); }
	/**
	 * Step the camera towards its target position.
	 */
	public void updateCameraPos(int width, int height) {
		// X
		this.cameraX = ((this.cameraX * 9) + getTargetCameraX(width)) / 10;
		// Y
		this.cameraY = ((this.cameraY * 9) + getTargetCameraY(height)) / 10;
	}
	public void tick(int width, int height) {
		// Update the camera
		Boxes.Player player = getPlayer();
		updateCameraPos(width, height);
		// Tick the boxes
		for (int i = layers.size() - 1; i >= 0; i--) {
			for (int j = 0; j < layers.get(i).size(); j++) {
				Box box = layers.get(i).get(j);
				if (box.ticked == false) box.tick();
				box.ticked = true;
			}
		}
		for (int i = layers.size() - 1; i >= 0; i--) {
			for (int j = 0; j < layers.get(i).size(); j++) {
				Box box = layers.get(i).get(j);
				box.ticked = false;
			}
		}
		// Player Movement
		if (keys.contains("Up") || keys.contains("Space")) {
			if (player.touchingGround) {
				player.vy = -0.3;
			}
		}
		if (keys.contains("Left")) {
			player.vx -= 0.014;
		}
		if (keys.contains("Right")) {
			player.vx += 0.014;
		}
		// Ending animation & timer
		if (! this.levelCompleted) this.timer += 1;
		if (this.endingAnimation == 0) {
		} else {
			// Ending animation!
			this.endingAnimation += 1;
			if (this.endingAnimation == 80) {
				if (SAVE_TIMES) {
					try {
						double time = Math.round((this.timer / 60d) * 100d) / 100d;
						// Save time to file
						File outFile = new File("levels.txt");
						FileWriter f = new FileWriter(outFile, true);
						f.write("Level " + level + ": " + (levelCompleted ? "completed" : "reset") + " after " +
							time + " seconds\n");
						f.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (levelCompleted) {
					this.level += 1;
					levelCompleted = false;
				}
				this.layers = new ArrayList<ArrayList<Box>>();
				this.switchedPlayer = false;
				generateLevel();
			}
			if (this.endingAnimation > 80+80) {
				this.endingAnimation = 0;
			}
		}
	}
	/**
	 * If cheating mode is enabled, stores the currently selected box.
	 */
	public Box mouseCarrying = null;
	public void mouseMoved(int x, int y) {
		if (mouseCarrying != null) {
			mouseCarrying.vx = 0;
			mouseCarrying.vy = 0;
			// Find mouse pos
			double precision = 4;
			double realMouseX = (x + cameraX) / 50;
			double realMouseY = (y + cameraY) / 50;
			// Find new box pos
			double targetX = realMouseX - (mouseCarrying.rect.w / 2);
			double targetY = realMouseY - (mouseCarrying.rect.h / 2);
			double newX = Math.round(targetX * precision) / precision;
			double newY = Math.round(targetY * precision) / precision;
			// update pos
			if (newX != mouseCarrying.rect.x || newY != mouseCarrying.rect.y) {
				mouseCarrying.rect.x = newX;
				mouseCarrying.rect.y = newY;
				if (mouseCarrying.physics != PhysicsState.PHYSICS) {
					System.out.println("moved object to x: " + newX + " y: " + newY);
				}
			}
		}
	}
	public void mouseDown(int x, int y) {
		if (! CHEAT) return;
		Rect mouseRect = new Rect((x + cameraX) / 50, (y + cameraY) / 50, 0.01, 0.01);
		for (ArrayList<Box> list : this.layers) {
			for (Box b : list) {
				if (b.physics != PhysicsState.PHYSICS) continue;
				if (b.rect.colliderect(mouseRect)) {
					mouseCarrying = b;
				}
			}
		}
		// If still null check static objects
		if (mouseCarrying == null) {
			for (ArrayList<Box> list : this.layers) {
				for (Box b : list) {
					if (b.rect.colliderect(mouseRect)) {
						mouseCarrying = b;
					}
				}
			}
		}
	}
	public void mouseUp(int x, int y) {
		mouseCarrying = null;
	}
	public void keyDown(String key) {
		keys.add(key);
		if (key.equals("Z")) {
			this.switchedPlayer = !switchedPlayer;
		}
		if (key.equals("R")) {
			this.endingAnimation = 40;
		}
	}
	public void keyUp(String key) {
		keys.remove(key);
	}
}
