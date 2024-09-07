package com.sillypantscoder.background.screen;

import java.awt.Color;

import com.sillypantscoder.background.Game;
import com.sillypantscoder.background.Level;
import com.sillypantscoder.background.Levels;
import com.sillypantscoder.background.MainWindow;
import com.sillypantscoder.utils.Rect;
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
		final int levelSpacing = 100;
		final int levelSize = Math.min(width / 3, height - 200);
		final int verticalSpace = height - levelSize;
		final int wholeLevelWidth = levelSpacing + levelSize + levelSpacing;
		final int horizontalSpace = width - wholeLevelWidth;
		// Camera
		cameraX = ((cameraX * 9) + targetCameraX) / 10;
		final int cameraOffset = (int)(wholeLevelWidth * cameraX) - (horizontalSpace / 2);
		// Draw Levels
		Surface s = new Surface(width, height, new Color(150, 150, 150));
		for (int i = 0; i < Levels.levels.length; i++) {
			Level l = Levels.levels[i];
			// Draw path
			int centerX = (wholeLevelWidth * i) + (wholeLevelWidth / 2);
			if (l.completed) {
				s.drawLine(new Color(0, 0, 0), centerX - cameraOffset, height / 2, (centerX - cameraOffset) + wholeLevelWidth, height / 2, 10);
			}
			// Draw rectangle
			int topY = (verticalSpace / 3) * (1 + (i % 2));
			Rect levelRect = Rect.fromCenter(centerX - cameraOffset, topY + (levelSize / 2), levelSize, levelSize);
			s.drawRect(new Color(50, 50, 50), levelRect);
			// Draw number
			Surface number = Surface.renderText(levelSize / 2, "" + i, new Color(200, 200, 200));
			int numberX = centerX - (number.get_width() / 2);
			s.blit(number, numberX - cameraOffset, topY);
			// Draw checkmark
			if (l.completed) {
				s.drawCircle(new Color(200, 200, 200), centerX - cameraOffset, topY + levelSize, levelSize / 6);
				s.drawPolygon(new Color(50, 50, 50), new double[][] {
					new double[] { -4,  1 },
					new double[] { -3,  0 },
					new double[] { -1,  2 },
					new double[] {  3, -3 },
					new double[] {  4, -2 },
					new double[] { -1,  4 }
				}, centerX - cameraOffset, topY + levelSize, levelSize / 40);
			}
		}
		return s;
	}
	public void keyDown(String e) {}
	public void keyUp(String e) {}
	public void mouseMoved(int x, int y) {}
	public void mouseDown(int x, int y) {}
	public void mouseUp(int x, int y) {
		final int levelSize = Math.min(lastWidth / 3, lastHeight - 200);
		// Find level position
		int centerX = lastWidth / 2;
		int leftX = centerX - (levelSize / 2);
		int rightX = centerX + (levelSize / 2);
		// Find click position
		if (x < leftX) {
			targetCameraX -= 1;
		} else if (x > rightX) {
			targetCameraX += 1;
		} else {
			boolean canContinue = targetCameraX == 0 || Levels.levels[targetCameraX - 1].completed;
			if (canContinue || Game.CHEAT) {
				LevelTitleScreen newScreen = new LevelTitleScreen(window, this.targetCameraX);
				navigate(new EndingAnimation(window, this, newScreen));
			}
		}
	}
	public void mouseWheel(int amount) {}
}
