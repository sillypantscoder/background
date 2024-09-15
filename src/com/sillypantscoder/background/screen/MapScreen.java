package com.sillypantscoder.background.screen;

import java.awt.Color;

import com.sillypantscoder.background.Game;
import com.sillypantscoder.background.Level;
import com.sillypantscoder.background.Levels;
import com.sillypantscoder.background.MainWindow;
import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.utils.Utils;
import com.sillypantscoder.windowlib.Surface;

public class MapScreen extends Screen {
	public double cameraX;
	public int targetCameraX;
	public int lastWidth;
	public int lastHeight;
	public MapScreen(MainWindow window, int startLevel) {
		super(window);
		this.cameraX = startLevel - 1;
		this.targetCameraX = startLevel;
	}
	public Surface frame(int width, int height) {
		// Record width & height
		this.lastWidth = width;
		this.lastHeight = height;
		// Sizing
		final int levelSpacing = Math.max(100, width / 8);
		final int levelSize = getLevelSize();
		final int wholeLevelWidth = levelSpacing + levelSize + levelSpacing;
		final int horizontalSpace = width - wholeLevelWidth;
		// Camera
		cameraX = ((cameraX * 9) + targetCameraX) / 10;
		final int cameraOffset = (int)(wholeLevelWidth * cameraX) - (horizontalSpace / 2);
		// Draw Levels
		Surface s = new Surface(width, height, new Color(150, 150, 150));
		for (int i = 0; i < Levels.levels.length; i++) {
			Level l = Levels.levels[i];
			// Find geometry
			int centerX = (wholeLevelWidth * i) + (wholeLevelWidth / 2);
			int topY = getTopYForLevel(height, i);
			int centerY = topY + (levelSize / 2);
			int nextCenterY = getTopYForLevel(height, i + 1) + (levelSize / 2);
			// Draw path
			if (i != Levels.levels.length - 1) {
				Color pathColor = new Color(255, 255, 255, 30);
				if (l.bestTime != -1) {
					pathColor = new Color(0, 0, 0);
				}
				s.drawLine(pathColor, centerX - cameraOffset, centerY, (centerX - cameraOffset) + wholeLevelWidth, nextCenterY, 10);
			}
			// Draw rectangle
			Rect levelRect = Rect.fromCenter(centerX - cameraOffset, topY + (levelSize / 2), levelSize, levelSize);
			s.drawRect(new Color(50, 50, 50), levelRect);
			// Draw number
			Color numberColor = new Color(200, 200, 200, 50);
			if (i == 0 || Levels.levels[i - 1].bestTime != -1) numberColor = new Color(200, 200, 200);
			Surface number = Surface.renderText(levelSize / 2, "" + i, numberColor);
			int numberX = centerX - (number.get_width() / 2);
			s.blit(number, numberX - cameraOffset, topY - (levelSize / 10));
			// Draw checkmark
			if (l.bestTime != -1) {
				s.drawCircle(new Color(200, 200, 200), centerX - cameraOffset, topY + levelSize, levelSize / 6);
				s.drawPolygon(new Color(50, 50, 50), new double[][] {
					new double[] { -4,  1 },
					new double[] { -3,  0 },
					new double[] { -1,  2 },
					new double[] {  3, -3 },
					new double[] {  4, -2 },
					new double[] { -1,  4 }
				}, centerX - cameraOffset, topY + levelSize, levelSize / 40);
				// Draw text
				Surface t = Surface.renderText(levelSize / 6, Utils.formatTime(l.bestTime), new Color(100, 100, 100));
				s.blit(t, (centerX - cameraOffset) - (t.get_width() / 2), (topY + levelSize) - (t.get_height() * 2));
			}
			// Draw coin
			if (l.gotCoin) {
				int coinX = (centerX - (levelSize / 2)) - cameraOffset;
				int coinY = centerY + (levelSize / 2);
				int coinSize = levelSize / 14;
				s.drawCircle(new Color(200, 200, 200), coinX, coinY, coinSize);
				Surface t = Surface.renderText((int)(coinSize * 1.75), "C", new Color(50, 50, 50));
				s.blit(t, coinX - (t.get_width() / 2), coinY - (t.get_height() / 2));
			}
		}
		// Draw settings button
		s.blit(SettingsScreen.settingsIcon.resize(30, 30), 5, 5);
		return s;
	}
	public int getLevelSize() {
		int levelSize = Math.min(lastWidth / 3, lastHeight - 200);
		if (levelSize > 350) levelSize = 350;
		if (levelSize < 50) levelSize = 50;
		return levelSize;
	}
	public int getTopYForLevel(int height, int level) {
		final int levelSize = getLevelSize();
		final int verticalSpace = height - levelSize;
		return (verticalSpace / 3) * (1 + (level % 2));
	}
	public void keyDown(String e) {
		if (e.equals("Left")) scroll(-1);
		if (e.equals("Right")) scroll(1);
		if (e.equals("Space") || e.equals("Z") || e.equals("Enter")) selectLevel();
	}
	public void keyUp(String e) {}
	public void mouseMoved(int x, int y) {}
	public void mouseDown(int x, int y) {}
	public void mouseUp(int x, int y) {
		final int levelSize = getLevelSize();
		// Check for settings button
		if (x < 40 && y < 40) {
			navigate(new SettingsScreen(this));
			return;
		}
		// Find level position
		int centerX = lastWidth / 2;
		int leftX = centerX - (levelSize / 2);
		int rightX = centerX + (levelSize / 2);
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
		boolean canContinue = targetCameraX == 0 || Levels.levels[targetCameraX - 1].bestTime != -1;
		if (canContinue || Game.CHEAT) {
			LevelTitleScreen newScreen = new LevelTitleScreen(window, this.targetCameraX);
			navigate(new EndingAnimation(window, this, newScreen));
		}
	}
	public void mouseWheel(int amount) {}
}
