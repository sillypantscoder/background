package com.sillypantscoder.background;

import java.awt.Color;

import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;

public class GameScreen extends Screen {
	public Game game;
	public GameScreen(MainWindow window, int level) {
		super(window);
		this.game = new Game(this, level);
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
		// Timer
		if (Game.SHOW_TIMER) {
			s.blit(Surface.renderText(30, (game.timer / 60) + ":" + Math.round(game.timer % 60), Color.RED), 0, 0);
		}
		return s;
	}
	public void keyDown(String e) {
		if (e.equals("R")) {
			GameScreen newScreen = new GameScreen(window, game.level);
			navigate(new EndingAnimation(window, this, new OpeningAnimation(window, newScreen)));
		} else if (e.equals("Escape")) {
			navigate(new MapScreen(window, game.level));
		} else {
			game.keyDown(e);
		}
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
