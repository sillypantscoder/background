package com.sillypantscoder.background;

import java.awt.Color;

import com.sillypantscoder.windowlib.Surface;
import com.sillypantscoder.windowlib.Window;

public class MainWindow extends Window {
	public Screen screen;
	public MainWindow() {
		this.screen = new GameScreen(new Game(this, 0));
		// Open the window
		this.open("Background", 750, 550);
	}
	public Surface getIcon() {
		Surface s = new Surface(32, 32, new Color(0, 0, 0, 0));
		int sw = s.get_width() / 4;
		s.drawRect(new Color(128, 128, 128), 1*sw, 1*sw, 3*sw, 3*sw);
		s.drawRect(new Color(64, 64, 64), 0, 0, 3*sw, 3*sw);
		return s;
	}
	public Surface frame(int width, int height) {
		return screen.frame(width, height);
	}
	public void keyDown(String e) {
		if (e.equals("R")) {
			if (this.screen instanceof GameScreen gamescreen) {
				GameScreen newScreen = new GameScreen(new Game(this, gamescreen.game.level));
				this.screen = new EndingAnimation(this, screen, new OpeningAnimation(this, newScreen));
			}
		}
		screen.keyDown(e);
	}
	public void keyUp(String e) {
		screen.keyUp(e);
	}
	public void mouseMoved(int x, int y) {
		screen.mouseMoved(x, y);
	}
	public void mouseDown(int x, int y) {
		screen.mouseDown(x, y);
	}
	public void mouseUp(int x, int y) {
		screen.mouseUp(x, y);
	}
	public void mouseWheel(int amount) {
		screen.mouseWheel(amount);
	}
}
