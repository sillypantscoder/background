package com.sillypantscoder.background.screen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.sillypantscoder.background.Box;
import com.sillypantscoder.background.Boxes;
import com.sillypantscoder.background.Box.PhysicsState;
import com.sillypantscoder.background.Drawable3D;
import com.sillypantscoder.background.MainWindow;
import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;

public class EditorScreen extends GameScreen {
	public static final double precision = 4;
	public int focusLayer = -1;
	public double gridMouseX;
	public double gridMouseY;
	public EditorScreen(MainWindow window, int level) {
		super(window, level);
	}
	public void drawGrid(Surface s) {
		double boxCoordScale = Math.max(10, this.boxCoordScale);
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
		this.lastWidth = width;
		this.lastHeight = height;
		// tick
		game.tick(width, height);
		// Get camera pos
		double cameraX = getCameraX();
		double cameraY = getCameraY();
		// Draw Grid
		Surface s = new Surface(width, height, Color.WHITE);
		if (! this.game.keys.contains("M")) drawGrid(s);
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
		Surface text2 = Surface.renderText(20, "Hold T and drag to resize boxes", Color.BLACK);
		s.blit(text2, 0, text1.get_height());
		Surface text3 = Surface.renderText(20, "Hold N and drag to create walls", Color.BLACK);
		s.blit(text3, 0, text1.get_height() + text2.get_height());
		Surface text4 = Surface.renderText(20, "Hold M to show grid on top", Color.BLACK);
		s.blit(text4, 0, text1.get_height() + text2.get_height() + text3.get_height());
		if (this.game.keys.contains("M")) drawGrid(s);
		// Finish
		return s;
	}
	public void keyDown(String e) {
		if (Character.isDigit(e.charAt(0))) {
			int tl = Integer.valueOf(e) - 1;
			this.focusLayer = tl;
		} else if (e.equals("R")) {
			EditorScreen newScreen = new EditorScreen(window, game.level);
			navigate(new EndingAnimation(window, this, new OpeningAnimation(window, newScreen)));
		} else if (e.equals("Escape") || e.equals("⎋")) {
			if (this.window.screen instanceof EndingAnimation) return;
			MapScreen newScreen = new MapScreen(window, game.level);
			navigate(new EndingAnimation(window, this, new OpeningAnimation(window, newScreen)));
		} else {
			game.keyDown(e);
		}
	}
	/**
	 * Stores the currently selected box.
	 */
	public Box mouseCarrying = null;
	/**
	 * True if we are resizing this box instead of moving it
	 */
	public boolean resizing = false;
	public void mouseMoved(int x, int y) {
		gridMouseX = x;
		gridMouseY = y;
		if (mouseCarrying != null) {
			mouseCarrying.vx = 0;
			mouseCarrying.vy = 0;
			// Find mouse pos
			double realMouseX = (x + getCameraX()) / this.boxCoordScale;
			double realMouseY = (y + getCameraY()) / this.boxCoordScale;
			// Make box changes
			if (! resizing) {
				// Find new box pos
				double targetX = realMouseX - (mouseCarrying.rect.w / 2);
				double targetY = realMouseY - (mouseCarrying.rect.h / 2);
				double newX = Math.round(targetX * precision) / precision;
				double newY = Math.round(targetY * precision) / precision;
				// update pos
				mouseCarrying.rect.x = newX;
				mouseCarrying.rect.y = newY;
			} else {
				// Find new box size
				double targetX = Math.abs(realMouseX - mouseCarrying.rect.x);
				double targetY = Math.abs(realMouseY - mouseCarrying.rect.y);
				if (targetX == 0) targetX += 1/precision;
				if (targetY == 0) targetY += 1/precision;
				double newX = Math.round(targetX * precision) / precision;
				double newY = Math.round(targetY * precision) / precision;
				// update pos
				mouseCarrying.rect.w = newX;
				mouseCarrying.rect.h = newY;
			}
		}
	}
	public void mouseDown(int x, int y) {
		if (mouseCarrying != null) return;
		// Create new box?
		if (this.game.keys.contains("N")) {
			// Find mouse coords
			double mouseX = (x + getCameraX()) / this.boxCoordScale;
			double mouseY = (y + getCameraY()) / this.boxCoordScale;
			mouseX = Math.round(mouseX * precision) / precision;
			mouseY = Math.round(mouseY * precision) / precision;
			// Create box
			Box b = new Boxes.Wall(
				game.getLayer(this.focusLayer == -1 ? 0 : this.focusLayer),
				new Rect(mouseX, mouseY, 1, 1)
			);
			// Register
			b.spawn();
			this.mouseCarrying = b;
			this.resizing = true;
			System.out.println("created object at x: " + mouseX + " y: " + mouseY);
			return;
		}
		// Get a collision rect for the mouse
		Rect mouseRect = new Rect(
			(x + getCameraX()) / this.boxCoordScale,
			(y + getCameraY()) / this.boxCoordScale,
		0.01, 0.01);
		// Select physics objects
		for (ArrayList<? extends Drawable3D> list : this.getLayers()) {
			if (this.focusLayer != -1 && this.getLayers().get(this.focusLayer)!=list) continue;
			for (Drawable3D d3 : list) {
				// If it is a box (which it always should be) then select it if it has physics.
				if (d3 instanceof Box b) {
					if (b.physics != PhysicsState.PHYSICS) continue;
					if (b.rect.colliderect(mouseRect)) {
						mouseCarrying = b;
					}
				}
			}
		}
		// If still null check static objects
		if (mouseCarrying == null) {
			for (ArrayList<? extends Drawable3D> list : this.getLayers()) {
				if (this.focusLayer != -1 && this.getLayers().get(this.focusLayer)!=list) continue;
				for (Drawable3D d3 : list) {
					// If it is a box (which it always should be) then select it.
					// (We only get here if we didn't already select a box with physics.)
					if (d3 instanceof Box b) {
						if (b.rect.colliderect(mouseRect)) {
							mouseCarrying = b;
						}
					}
				}
			}
		}
		// Set resizing
		this.resizing = this.game.keys.contains("T");
	}
	public void mouseUp(int x, int y) {
		if (mouseCarrying != null && mouseCarrying.physics != PhysicsState.PHYSICS) {
			if (! resizing) {
				System.out.println("moved object to x: " + mouseCarrying.rect.x + " y: " + mouseCarrying.rect.y);
			} else {
				System.out.println("resized object to w: " + mouseCarrying.rect.w + " h: " + mouseCarrying.rect.h);
			}
		}
		mouseCarrying = null;
	}
}