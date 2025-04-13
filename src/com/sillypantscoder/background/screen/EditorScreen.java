package com.sillypantscoder.background.screen;

import java.awt.Color;
import java.util.List;

import com.sillypantscoder.background.Drawable3D;
import com.sillypantscoder.background.MainWindow;
import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;

public class EditorScreen extends GameScreen {
	public int focusLayer = -1;
	public EditorScreen(MainWindow window, int level) {
		super(window, level);
	}
	public Surface frame(int width, int height) {
		this.lastWidth = width;
		this.lastHeight = height;
		// tick
		game.tick(width, height);
		// Get camera pos
		double cameraX = getCameraX();
		double cameraY = getCameraY();
		// Draw Grid
		Surface s = new Surface(width, height, Color.WHITE);
		drawGrid(s);
		// Draw Layers
		List<? extends List<? extends Drawable3D>> layers = getLayers();
		for (int i = layers.size() - 1; i >= 0; i--) {
			double brightness = 256 - Math.pow(2, 8 - i);
			double offset = boxCoordScale * i * 0.1;
			if (focusLayer != -1) {
				if (i == focusLayer) {
					brightness = 0;
					offset = 0;
				} else if (i < focusLayer) continue;
				else brightness = 127;
			}
			for (int j = 0; j < layers.get(i).size(); j++) {
				Drawable3D box = layers.get(i).get(j);
				// Get rect
				Rect drawRect = new Rect(
					(box.getRect().x * boxCoordScale) - cameraX,
					(box.getRect().y * boxCoordScale) - cameraY,
					box.getRect().w * boxCoordScale,
					box.getRect().h * boxCoordScale
				);
				drawRect = new Rect(
					drawRect.x + offset,
					drawRect.y + offset,
					drawRect.w,
					drawRect.h
				);
				// Draw
				box.draw(s, drawRect, brightness);
			}
		}
		// Debug Text
		Surface text1 = Surface.renderText(20, "Layer: " + this.focusLayer + " [1-9]", Color.BLACK);
		s.blit(text1, 0, 0);
		// Finish
		return s;
	}
	public void keyDown(String e) {
		if (Character.isDigit(e.charAt(0))) {
			int tl = Integer.valueOf(e) - 1;
			this.focusLayer = tl;
		} else if (e.equals("R")) {
			GameScreen newScreen = new GameScreen(window, game.level);
			navigate(new EndingAnimation(window, this, new OpeningAnimation(window, newScreen)));
		} else if (e.equals("Escape") || e.equals("⎋")) {
			if (this.window.screen instanceof EndingAnimation) return;
			MapScreen newScreen = new MapScreen(window, game.level);
			navigate(new EndingAnimation(window, this, new OpeningAnimation(window, newScreen)));
		} else {
			game.keyDown(e);
		}
	}
}