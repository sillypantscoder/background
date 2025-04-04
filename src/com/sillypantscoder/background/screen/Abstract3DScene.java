package com.sillypantscoder.background.screen;

import java.awt.Color;
import java.util.List;

import com.sillypantscoder.background.Drawable3D;
import com.sillypantscoder.background.MainWindow;
import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;

public abstract class Abstract3DScene extends Screen {
	public static final boolean DRAW_GRID = false;
	public double gridMouseX;
	public double gridMouseY;
	public double boxCoordScale = 50;
	public Abstract3DScene(MainWindow window) {
		super(window);
	}
	public abstract List<? extends List<? extends Drawable3D>> getLayers();
	public abstract double getCameraX();
	public abstract double getCameraY();
	public void drawGrid(Surface s) {
		double cameraX = getCameraX();
		double cameraY = getCameraY();
		// Find grid start and size
		int startX = (int)(cameraX / boxCoordScale);
		int startY = (int)(cameraY / boxCoordScale);
		int sizeX = (int)(s.get_width() / boxCoordScale) + 1;
		int sizeY = (int)(s.get_height() / boxCoordScale) + 1;
		// Draw vertical lines
		for (int x = startX; x < startX + sizeX; x++) {
			// Find color/thickness
			Color color = new Color(192, 192, 192);
			int thickness = 1;
			if ((x % 5) == 0) { color = new Color(64, 64, 64); thickness = 3; }
			if (x == 0) { color = new Color(0, 0, 0); thickness = 7; }
			// Draw
			int screenX = (int)((x * boxCoordScale) - cameraX);
			s.drawLine(color, screenX, 0, screenX, s.get_height(), thickness);
		}
		// Draw horizontal lines
		for (int y = startY; y < startY + sizeY; y++) {
			// Find color/thickness
			Color color = new Color(192, 192, 192);
			int thickness = 1;
			if ((y % 5) == 0) { color = new Color(64, 64, 64); thickness = 3; }
			if (y == 0) { color = new Color(0, 0, 0); thickness = 7; }
			if (y == 20 || y == 21) { color = new Color(255, 0, 0); }
			// Draw
			int screenY = (int)((y * boxCoordScale) - cameraY);
			s.drawLine(color, 0, screenY, s.get_width(), screenY, thickness);
		}
		// Draw numbers
		for (int x = startX; x < startX + sizeX; x++) {
			for (int y = startY; y < startY + sizeY; y++) {
				// Render text
				String text = "?";
				if (x % 5 == 0 && y % 5 == 0) text = "(" + x + ", " + y + ")";
				else continue;
				Surface t = Surface.renderText(15, text, Color.BLACK);
				// Draw
				int screenX = (int)((x * boxCoordScale) - cameraX);
				int screenY = (int)((y * boxCoordScale) - cameraY);
				s.blit(t, screenX + 5, screenY + 5);
			}
		}
		// Mouse
		{
			int gameX = (int)(Math.floor((gridMouseX + cameraX) / boxCoordScale));
			int gameY = (int)(Math.floor((gridMouseY + cameraY) / boxCoordScale));
			String text = "(" + gameX + ", " + gameY + ")";
			Surface t = Surface.renderText(15, text, Color.BLACK);
			int screenX = (int)((gameX * boxCoordScale) - cameraX);
			int screenY = (int)((gameY * boxCoordScale) - cameraY);
			s.blit(t, screenX + 1, screenY - 1);
		}
	}
	public Surface frame(int width, int height) {
		double cameraX = getCameraX();
		double cameraY = getCameraY();
		// Draw Grid (optional)
		Surface s = new Surface(width, height, Color.WHITE);
		if (DRAW_GRID) drawGrid(s);
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
	public void mouseMoved(int x, int y) {
		if (DRAW_GRID) {
			gridMouseX = x;
			gridMouseY = y;
		}
	}
}
