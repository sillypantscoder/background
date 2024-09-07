package com.sillypantscoder.background;

import java.awt.Color;

import com.sillypantscoder.windowlib.Surface;

public class OpeningAnimation extends Screen {
	public Screen nextScreen;
	public int animation;
	public OpeningAnimation(MainWindow window, Screen nextScreen) {
		super(window);
		this.nextScreen = nextScreen;
	}
	public Surface frame(int width, int height) {
		// update animation
		animation += 1;
		if (animation >= 80) {
			navigate(nextScreen);
		}
		// get subsurface
		Surface s = getDisplayScreen().frame(width, height);
		// Find animation amount
		double borderSize = Math.pow(getAnimationValue() / 80d, 8) / 2;
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
		return s;
	}
	public Screen getDisplayScreen() { return nextScreen; }
	public int getAnimationValue() { return 80 - animation; }
	public void keyDown(String e) {
		getDisplayScreen().keyDown(e);
	}
	public void keyUp(String e) {
		getDisplayScreen().keyUp(e);
	}
	public void mouseMoved(int x, int y) {
		getDisplayScreen().mouseMoved(x, y);
	}
	public void mouseDown(int x, int y) {
		getDisplayScreen().mouseDown(x, y);
	}
	public void mouseUp(int x, int y) {
		getDisplayScreen().mouseUp(x, y);
	}
	public void mouseWheel(int amount) {
		getDisplayScreen().mouseWheel(amount);
	}
}
