package com.sillypantscoder.background;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.sillypantscoder.background.Box.PhysicsState;
import com.sillypantscoder.background.screen.GameScreen;
import com.sillypantscoder.utils.ListCombination;
import com.sillypantscoder.utils.Rect;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains all of the data needed to run the game.
 */
public class Game {
	public static final boolean CHEAT = false;
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
	@SuppressWarnings("unlikely-arg-type")
	public Game copy() {
		ArrayList<ArrayList<Box>> newLayers = new ArrayList<ArrayList<Box>>();
		for (@SuppressWarnings("unused") ArrayList<Box> __ : this.layers) newLayers.add(new ArrayList<Box>());
		// copy each box
		Set<Box> allBoxes = new HashSet<Box>();
		for (int layer = 0; layer < layers.size(); layer++) {
			for (int b = 0; b < layers.get(layer).size(); b++) {
				allBoxes.add(layers.get(layer).get(b));
			}
		}
		Map<Box, Box> copyMap = new HashMap<Box, Box>();
		for (Box b : allBoxes) {
			Box copied = b.copy();
			copyMap.put(b, copied);
			// find world for this box
			if (this.layers.contains(copied.world)) {
				// regular world
				int layer = this.layers.indexOf(copied.world);
				copied.world = newLayers.get(layer);
			} else if (copied.world instanceof ListCombination<Box> combinedWorld) {
				// combined world
				List<Box>[] lists = combinedWorld.lists;
				@SuppressWarnings("unchecked")
				List<Box>[] newLists = new List[lists.length];
				for (int i = 0; i < lists.length; i++) {
					int layerno = this.layers.indexOf(lists[i]);
					if (layerno == -1) throw new RuntimeException("A box's world is a list combination containing unknown objects");
					newLists[i] = newLayers.get(layerno);
				}
				ListCombination<Box> newWorld = new ListCombination<Box>(newLists);
				copied.world = newWorld;
			}
		}
		// copy layers
		for (int layer = 0; layer < layers.size(); layer++) {
			ArrayList<Box> l = newLayers.get(layer);
			for (int b = 0; b < layers.get(layer).size(); b++) {
				Box box = layers.get(layer).get(b);
				// lookup
				Box copied = copyMap.get(box);
				l.add(copied);
				// special for buttons
				if (copied instanceof Boxes.Button btn) {
					for (int i = 0; i < btn.handlers.length; i++) {
						btn.handlers[i] = (Boxes.Button.SwitchHandler)(copyMap.get(btn.handlers[i]));
					}
				}
			}
		}
		// construct new game
		Game copied = new Game(screen, level);
		copied.player1 = (Boxes.Player)(copyMap.get(player1));
		copied.player2 = (Boxes.Player)(copyMap.get(player2));
		copied.switchedPlayer = this.switchedPlayer;
		copied.layers = newLayers;
		copied.cameraX = this.cameraX;
		copied.cameraY = this.cameraY;
		copied.timer = this.timer;
		// set games
		if (player1 != null) copied.player1.game = copied;
		if (player2 != null) copied.player2.game = copied;
		for (int layer = 0; layer < newLayers.size(); layer++) {
			for (int b = 0; b < newLayers.get(layer).size(); b++) {
				if (newLayers.get(layer).get(b) instanceof Boxes.End end) {
					end.game = copied;
				}
			}
		}
		// finish
		return copied;
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
			System.err.println("Error loading level " + level);
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
		if (keys.contains("Up") || keys.contains("W") || keys.contains("↑") || keys.contains("Space") || keys.contains("␣")) {
			if (player.touchingGround) {
				player.vy = -0.3;
			}
		}
		if (keys.contains("Down") || keys.contains("S") || keys.contains("↓")) {
			if (!player.getAboveWalls().isEmpty()) {
				player.vy = 0.3;
			}
		}
		if (keys.contains("Left") || keys.contains("A") || keys.contains("←")) {
			player.vx -= 0.014;
		}
		if (keys.contains("Right") || keys.contains("D") || keys.contains("→")) {
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
