package com.sillypantscoder.background;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;
import com.sillypantscoder.windowlib.Window;

public class Game extends Window {
	public static void main(String[] args) {
		new Game().open("Game", 750, 500);
	}
	public Boxes.Player player1;
	public Boxes.Player player2;
	public boolean switchedPlayer;
	public ArrayList<ArrayList<Box>> layers;
	public Set<String> keys;
	public double cameraX;
	public double cameraY;
	public int endingAnimation;
	public Game() {
		keys = new HashSet<String>();
		layers = new ArrayList<ArrayList<Box>>();
		// Player Setup
		player1 = new Boxes.Player(this, -50, 0);
		player1.spawn();
		player2 = new Boxes.Player(this, 50, 0);
		player2.spawn();
		// Level
		Levels.level1(this);
	}
	public ArrayList<Box> getLayer(int layer) {
		while (layers.size() <= layer) {
			layers.add(new ArrayList<Box>());
		}
		return layers.get(layer);
	}
	public int getLayer(ArrayList<Box> layer) {
		for (int i = 0; i < layers.size(); i++) {
			if (layers.get(i) == layer) {
				return i;
			}
		}
		throw new Error("Layer is not in world");
	}
	public void addBox(Box box, int layer) {
		getLayer(layer).add(box);
	}
	public void addBox(Box box) {
		box.world.add(box);
	}
	public Boxes.Player getPlayer() { return switchedPlayer ? player2 : player1; }
	public double getTargetCameraX(int width) { return getPlayer().rect.centerX() - (width / 2d); }
	public double getTargetCameraY(int height) { return getPlayer().rect.centerY() - (height / 2d); }
	public void updateCameraPos(int width, int height) {
		// X
		this.cameraX = ((this.cameraX * 9) + getTargetCameraX(width)) / 10;
		// Y
		this.cameraY = ((this.cameraY * 9) + getTargetCameraY(height)) / 10;
	}
	public Surface frame(int width, int height) {
		Boxes.Player player = getPlayer();
		updateCameraPos(width, height);
		// Draw
		Surface s = new Surface(width, height, Color.WHITE);
		for (int i = layers.size() - 1; i >= 0; i--) {
			double zoom = 14d / (i + 14);
			double brightness = 256 - Math.pow(2, 8 - i);
			for (int j = 0; j < layers.get(i).size(); j++) {
				Box box = layers.get(i).get(j);
				// Get rect
				Rect drawRect = new Rect(
					box.rect.x - cameraX,
					box.rect.y - cameraY,
					box.rect.w,
					box.rect.h
				);
				drawRect = new Rect(
					(drawRect.x * zoom) + ((width / 2d) * (1 - zoom)),
					(drawRect.y * zoom) + ((height / 2d) * (1 - zoom)),
					drawRect.w * zoom,
					drawRect.h * zoom
				);
				// Draw
				box.draw(s, drawRect, brightness);
				box.tick();
			}
		}
		// Player Movement
		if (keys.contains("Up")) {
			if (player.touchingGround) {
				player.vy = -15;
			}
		}
		if (keys.contains("Left")) {
			player.vx -= 0.7;
		}
		if (keys.contains("Right")) {
			player.vx += 0.7;
		}
		if (this.endingAnimation == 0) {
			return s;
		} else {
			// Ending animation!
			this.endingAnimation += 1;
			if (this.endingAnimation > 80) this.endingAnimation = 80;
			// double borderSize = 0.5 - (Math.sqrt(1 - (this.endingAnimation / 80d)) / 2);
			double borderSize = Math.pow(this.endingAnimation / 80d, 8) / 2;
			int borderX = (int)(borderSize * width);
			int borderY = (int)(borderSize * height);
			// Draw border
			Surface overlay = new Surface(width, height, new Color(0, 0, 0, 0));
			// (top, bottom)
			overlay.drawRect(Color.BLACK, 0, 0, width, borderY);
			overlay.drawRect(Color.BLACK, 0, height - borderY, width, borderY);
			// (left, right)
			overlay.drawRect(Color.BLACK, 0, 0, borderX, height);
			overlay.drawRect(Color.BLACK, width - borderX, 0, borderX, height);
			s.blit(overlay, 0, 0);
			return s;
		}
	}
	public void keyDown(String key) {
		keys.add(key);
		if (key == "Space") {
			this.switchedPlayer = !switchedPlayer;
		}
	}
	public void keyUp(String key) {
		keys.remove(key);
	}
}
