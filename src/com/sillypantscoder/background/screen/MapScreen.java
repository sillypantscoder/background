package com.sillypantscoder.background.screen;

import java.awt.Color;
import java.util.ArrayList;

import com.sillypantscoder.background.Box;
import com.sillypantscoder.background.Boxes;
import com.sillypantscoder.background.Drawable3D;
import com.sillypantscoder.background.Game;
import com.sillypantscoder.background.Level;
import com.sillypantscoder.background.Levels;
import com.sillypantscoder.background.MainWindow;
import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.utils.Utils;
import com.sillypantscoder.windowlib.Surface;

public class MapScreen extends Abstract3DScene {
	public double cameraX;
	public int targetCameraX;
	public int lastWidth;
	public int lastHeight;
	public ArrayList<ArrayList<Drawable3D>> layers;
	public Boxes.Text screenTitle;
	public Arrow arrowLeft;
	public Arrow arrowRight;
	public MapScreen(MainWindow window, int startLevel) {
		super(window);
		this.boxCoordScale = 1;
		this.cameraX = startLevel - 1;
		this.targetCameraX = startLevel;
		this.makeLayers();
	}
	public double getFullLevelWidth() { return lastWidth * 0.4; }
	public ArrayList<ArrayList<Drawable3D>> getLayers() { return layers; }
	public double getCameraX() { return (cameraX * getFullLevelWidth()) - (lastWidth * 0.5); }
	public double getCameraY() { return 0; }
	public static class LockIcon implements Drawable3D {
		public Rect rect;
		public LockIcon(Rect rect) {
			this.rect = rect;
		}
		public Rect getRect() { return rect; }
		public void draw(Surface s, Rect drawRect, double brightness) {
			Color color = Box.getColor(brightness);
			Surface lock = new Surface((int)(drawRect.w), (int)(drawRect.h*1.5), new Color(0, 0, 0, 0));
			double bodyBorderRadius = drawRect.size() / 2;
			// draw lock top
			double lockTopSize = drawRect.size() - (bodyBorderRadius * 0.6);
			lock.drawCircle(color, Rect.fromCenter(drawRect.w / 2, drawRect.h / 2, lockTopSize, lockTopSize));
			lock.eraseCircle((int)(drawRect.w / 2), (int)(drawRect.h / 2), (int)(lockTopSize * 0.5 * 0.5));
			// draw lock body
			lock.drawRoundedRect(color, new Rect(0, drawRect.h * 0.5, drawRect.w, drawRect.h), bodyBorderRadius);
			// draw dot
			lock.eraseCircle((int)(drawRect.w / 2), (int)(drawRect.h * 1), (int)(drawRect.size() / 6));
			// draw to screen
			s.blit(lock, (int)(drawRect.x), (int)(drawRect.y));
		}
	}
	public static class Arrow implements Drawable3D {
		public Rect rect;
		public boolean direction;
		public Arrow(Rect rect, boolean direction) {
			this.rect = rect;
			this.direction = direction;
		}
		public Rect getRect() { return rect; }
		public void draw(Surface s, Rect drawRect, double brightness) {
			Color color = Box.getColor(brightness);
			if (direction) s.drawPolygon(color, new int[][] {
				new int[] { (int)(drawRect.x + (drawRect.w * 0)), (int)(drawRect.y + (drawRect.h * 0)) },
				new int[] { (int)(drawRect.x + (drawRect.w * 1)), (int)(drawRect.y + (drawRect.h * 0.5)) },
				new int[] { (int)(drawRect.x + (drawRect.w * 0)), (int)(drawRect.y + (drawRect.h * 1)) }
			});
			else s.drawPolygon(color, new int[][] {
				new int[] { (int)(drawRect.x + (drawRect.w *  0)), (int)(drawRect.y + (drawRect.h * 0)) },
				new int[] { (int)(drawRect.x + (drawRect.w * -1)), (int)(drawRect.y + (drawRect.h * 0.5)) },
				new int[] { (int)(drawRect.x + (drawRect.w *  0)), (int)(drawRect.y + (drawRect.h * 1)) }
			});
		}
	}
	public boolean isLevelLocked(int levelNo) {
		return levelNo != 0 && Levels.levels[levelNo - 1].bestTime == -1;
	}
	public void makeLayers() {
		layers = new ArrayList<ArrayList<Drawable3D>>();
		layers.add(new ArrayList<Drawable3D>());
		layers.add(new ArrayList<Drawable3D>());
		layers.add(new ArrayList<Drawable3D>());
		layers.add(new ArrayList<Drawable3D>());
		layers.add(new ArrayList<Drawable3D>());
		screenTitle = new Boxes.Text(null, 0, lastWidth / 60.0, "Level Select", Math.max(1, lastWidth / 20), true);
		layers.get(1).add(screenTitle);
		arrowLeft = new Arrow(Rect.fromCenter(0, lastHeight / 2, 40, 80), false);
		layers.get(1).add(arrowLeft);
		arrowRight = new Arrow(Rect.fromCenter(0, lastHeight / 2, 40, 80), true);
		layers.get(1).add(arrowRight);
		for (int i = 0; i < Levels.levels.length; i++) {
			makeLevel(i);
		}
	}
	public void makeLevel(int levelNo) {
		// Find level rect
		double levelSize = getFullLevelWidth();
		Rect levelRect = Rect.fromCenter(levelSize * levelNo, lastHeight / 2, levelSize * 0.9, levelSize * 0.9);
		// Background
		layers.get(2).add(new Boxes.Wall(null, levelRect));
		layers.get(3).add(new Boxes.Wall(null, levelRect));
		layers.get(4).add(new Boxes.Wall(null, levelRect));
		// Level #
		Boxes.Text levelTitle = new Boxes.Text(null, levelRect.centerX(), levelRect.centerY() - (levelSize * 0.2), levelNo + "", Math.max(1, (int)(levelSize / 5)), true);
		layers.get(0).add(levelTitle); layers.get(1).add(levelTitle);
		// Level complete sign
		Level l = Levels.levels[levelNo];
		int textSize = (int)(levelSize / 14);
		if (l.bestTime != -1) {
			layers.get(1).add(new Boxes.Text(null, levelRect.left() + (levelSize * 0.05), levelRect.centerY() + (textSize *  -1), "Level Complete", Math.max(1, textSize), false));
			layers.get(1).add(new Boxes.Text(null, levelRect.left() + (levelSize * 0.05), levelRect.centerY() + (textSize * 0.5), "Time: " + Utils.formatTime(l.bestTime), Math.max(1, textSize), false));
		}
		// Coin complete sign
		if (l.bestCoinTime != -1) {
			layers.get(1).add(new Boxes.Text(null, levelRect.left() + (levelSize * 0.05), levelRect.centerY() + (textSize *   2), "Got Coin", Math.max(1, textSize), false));
			layers.get(1).add(new Boxes.Text(null, levelRect.left() + (levelSize * 0.05), levelRect.centerY() + (textSize * 3.5), "Time: " + Utils.formatTime(l.bestCoinTime), Math.max(1, textSize), false));
		}
		// Lock Icon
		if (isLevelLocked(levelNo)) {
			layers.get(1).add(new LockIcon(Rect.fromCenter(levelRect.centerX(), levelRect.centerY() + (levelSize * 0.1), levelSize * 0.25, levelSize * 0.25)));
		}
	}
	public Surface frame(int width, int height) {
		// Record width & height
		if (width  != this.lastWidth ) { this.lastWidth  = width ; makeLayers(); }
		if (height != this.lastHeight) { this.lastHeight = height; makeLayers(); }
		// Camera
		cameraX = ((cameraX * 9) + targetCameraX) / 10;
		// Update UI locations
		screenTitle.rect.x = cameraX * getFullLevelWidth();
		arrowLeft.rect.x = (cameraX - 0.5) * getFullLevelWidth();
		arrowRight.rect.x = (cameraX + 0.5) * getFullLevelWidth();
		// 3D effect
		Surface result = super.frame(width, height);
		// Settings icon
		result.blit(SettingsScreen.settingsIcon.resize(30, 30), 5, 5);
		// Finish
		return result;
	}
	public void keyDown(String e) {
		if (e.equals("Left") || e.equals("←")) scroll(-1);
		if (e.equals("Right") || e.equals("→")) scroll(1);
		if (e.equals("Space") || e.equals("␣") || e.equals("Z") || e.equals("Enter")) selectLevel();
	}
	public void keyUp(String e) {}
	public void mouseMoved(int x, int y) {}
	public void mouseDown(int x, int y) {}
	public void mouseUp(int x, int y) {
		// Check for settings button
		if (x < 40 && y < 40) {
			navigate(new SettingsScreen(this));
			return;
		}
		// Find level position
		int centerX = lastWidth / 2;
		int leftX = centerX - (int)(getFullLevelWidth() * 0.5 * 0.8);
		int rightX = centerX + (int)(getFullLevelWidth() * 0.5 * 0.8);
		// Find click position
		if (x < leftX) scroll(-1);
		else if (x > rightX) scroll(1);
		else selectLevel();
	}
	public void scroll(int amount) {
		if (targetCameraX + amount < Levels.levels.length && targetCameraX + amount >= 0) {
			targetCameraX += amount;
		}
	}
	public void selectLevel() {
		if ((! isLevelLocked(targetCameraX)) || Game.CHEAT) {
			LevelTitleScreen newScreen = new LevelTitleScreen(window, this.targetCameraX);
			navigate(new EndingAnimation(window, this, newScreen));
		}
	}
	public void mouseWheel(int amount) {}
}
