package com.sillypantscoder.background;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.sillypantscoder.background.Box.PhysicsState;
import com.sillypantscoder.background.screen.GameScreen;
import com.sillypantscoder.utils.ListCombination;
import com.sillypantscoder.utils.Rect;

/**
 * This class contains all of the data needed to run the game.
 */
public class Game {
	public static final boolean CHEAT = true;
	public GameScreen screen;
	public Boxes.Player player1;
	public Boxes.Player player2;
	public boolean switchedPlayer;
	public ArrayList<ArrayList<Box>> layers;
	public Set<String> keys;
	public double cameraX;
	public double cameraY;
	public int level;
	public int timer = 0;
	public Game(GameScreen screen, int level) {
		this.screen = screen;
		keys = new HashSet<String>();
		layers = new ArrayList<ArrayList<Box>>();
		// Level
		this.level = level;
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
		if (level >= Levels.levels.length) {
			System.err.println("Error loading level");
		} else {
			Levels.levels[level].build(this);
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
		if (keys.contains("Down")) {
			if (player.getAboveWalls().size() > 0) {
				player.vy = 0.3;
			}
		}
		if (keys.contains("Left")) {
			player.vx -= 0.014;
		}
		if (keys.contains("Right")) {
			player.vx += 0.014;
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
	}
	public void keyUp(String key) {
		keys.remove(key);
	}
}
