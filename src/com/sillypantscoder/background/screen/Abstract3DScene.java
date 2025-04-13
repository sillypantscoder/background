package com.sillypantscoder.background.screen;

import java.awt.Color;
import java.util.List;

import com.sillypantscoder.background.Drawable3D;
import com.sillypantscoder.background.MainWindow;
import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;

public abstract class Abstract3DScene extends Screen {
	public static final boolean DEBUG_MODE = true;
	public double boxCoordScale = 50;
	public Abstract3DScene(MainWindow window) {
		super(window);
	}
	public abstract List<? extends List<? extends Drawable3D>> getLayers();
	public abstract double getCameraX();
	public abstract double getCameraY();
	public Surface frame(int width, int height) {
		double cameraX = getCameraX();
		double cameraY = getCameraY();
		// Create Surface
		Surface s = new Surface(width, height, Color.WHITE);
		// Draw Layers
		List<? extends List<? extends Drawable3D>> layers = getLayers();
		for (int i = layers.size() - 1; i >= 0; i--) {
			double zoom = 14d / (i + 14);
			double brightness = 256 - Math.pow(2, 8 - i);
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
