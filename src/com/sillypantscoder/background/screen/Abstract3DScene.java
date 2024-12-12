package com.sillypantscoder.background.screen;

import java.awt.Color;
import java.util.List;

import com.sillypantscoder.background.Drawable3D;
import com.sillypantscoder.background.MainWindow;
import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;

public abstract class Abstract3DScene extends Screen {
	public Abstract3DScene(MainWindow window) {
		super(window);
	}
	public abstract List<List<Drawable3D>> getLayers();
	public abstract double getCameraX();
	public abstract double getCameraY();
	public Surface frame(int width, int height) {
		double cameraX = getCameraX();
		double cameraY = getCameraY();
		List<List<Drawable3D>> layers = getLayers();
		// Draw Layers
		Surface s = new Surface(width, height, Color.WHITE);
		for (int i = layers.size() - 1; i >= 0; i--) {
			double zoom = 14d / (i + 14);
			double brightness = 256 - Math.pow(2, 8 - i);
			for (int j = 0; j < layers.get(i).size(); j++) {
				Drawable3D box = layers.get(i).get(j);
				// Get rect
				Rect drawRect = new Rect(
					(box.getRect().x * 50) - cameraX,
					(box.getRect().y * 50) - cameraY,
					box.getRect().w * 50,
					box.getRect().h * 50
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
		return s;
	}
}
