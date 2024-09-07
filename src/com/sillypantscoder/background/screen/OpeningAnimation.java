package com.sillypantscoder.background.screen;

import java.awt.Color;

import com.sillypantscoder.background.MainWindow;
import com.sillypantscoder.windowlib.Surface;

public class OpeningAnimation extends Screen {
	public int maxTime;
	public Screen nextScreen;
	public int animation;
	public OpeningAnimation(MainWindow window, Screen nextScreen) {
		super(window);
		this.nextScreen = nextScreen;
		this.maxTime = 80;
	}
	public Surface frame(int width, int height) {
		// update animation
		animation += 1;
		if (animation >= this.maxTime) {
			navigate(nextScreen);
		}
		// get subsurface
		Surface s = getDisplayScreen().frame(width, height);
		// Find animation amount
		double borderSize = Math.pow(getAnimationValue() / (double)(this.maxTime), 8) / 2;
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
	public int getAnimationValue() { return maxTime - animation; }
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
