package com.sillypantscoder.background;

import java.awt.Color;

import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;

public class GameScreen extends Screen {
	public Game game;
	public GameScreen(Game game) {
		this.game = game;
	}
	public Surface frame(int width, int height) {
		game.tick(width, height);
		// Draw Layers
		Surface s = new Surface(width, height, Color.WHITE);
		for (int i = game.layers.size() - 1; i >= 0; i--) {
			double zoom = 14d / (i + 14);
			double brightness = 256 - Math.pow(2, 8 - i);
			for (int j = 0; j < game.layers.get(i).size(); j++) {
				Box box = game.layers.get(i).get(j);
				// Get rect
				Rect drawRect = new Rect(
					(box.rect.x * 50) - game.cameraX,
					(box.rect.y * 50) - game.cameraY,
					box.rect.w * 50,
					box.rect.h * 50
				);
				drawRect = new Rect(
					(drawRect.x * zoom) + ((width / 2d) * (1 - zoom)),
					(drawRect.y * zoom) + ((height / 2d) * (1 - zoom)),
					drawRect.w * zoom,
					drawRect.h * zoom
				);
				// Draw
				box.draw(s, drawRect, brightness);
			}
		}
		// Ending animation
		if (game.endingAnimation > 0) {
			// Find animation amount
			int anim = game.endingAnimation;
			if (game.endingAnimation > 80) {
				anim = 80+80 - game.endingAnimation;
			}
			double borderSize = Math.pow(anim / 80d, 8) / 2;
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
		}
		// Timer
		if (Game.SHOW_TIMER) {
			s.blit(Surface.renderText(30, (game.timer / 60) + ":" + Math.round(game.timer % 60), Color.RED), 0, 0);
		}
		return s;
	}
	public void keyDown(String e) {
		game.keyDown(e);
	}
	public void keyUp(String e) {
		game.keyUp(e);
	}
	public void mouseMoved(int x, int y) {
		game.mouseMoved(x, y);
	}
	public void mouseDown(int x, int y) {
		game.mouseDown(x, y);
	}
	public void mouseUp(int x, int y) {
		game.mouseUp(x, y);
	}
	public void mouseWheel(int amount) {}
}
