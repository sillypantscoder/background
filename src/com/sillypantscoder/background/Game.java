package com.sillypantscoder.background;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.sillypantscoder.windowlib.Surface;
import com.sillypantscoder.windowlib.Window;

public class Game extends Window {
	public static void main(String[] args) {
		new Game().open("Game", 750, 500);
	}
	public Boxes.Player player1;
	public Boxes.Player player2;
	public boolean switchedPlayer;
	public ArrayList<Box> boxes;
	public Set<String> keys;
	public double cameraX;
	public double cameraY;
	public int endingAnimation;
	public Game() {
		keys = new HashSet<String>();
		boxes = new ArrayList<Box>();
		player1 = new Boxes.Player(this, -50, 0);
		boxes.add(player1);
		player2 = new Boxes.Player(this, 50, 0);
		boxes.add(player2);
		Levels.level1(this);
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
		for (int i = 0; i < boxes.size(); i++) {
			boxes.get(i).draw(s);
			boxes.get(i).tick();
		}
		if (this.endingAnimation == 0) {
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
