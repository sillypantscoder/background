package com.sillypantscoder.background;

import com.sillypantscoder.windowlib.Surface;

public abstract class Screen {
	public MainWindow window;
	public Screen(MainWindow window) {
		this.window = window;
	}
	public abstract Surface frame(int width, int height);
	public abstract void keyDown(String e);
	public abstract void keyUp(String e);
	public abstract void mouseMoved(int x, int y);
	public abstract void mouseDown(int x, int y);
	public abstract void mouseUp(int x, int y);
	public abstract void mouseWheel(int amount);
	public void navigate(Screen newScreen) {
		window.screen = newScreen;
	}
}
