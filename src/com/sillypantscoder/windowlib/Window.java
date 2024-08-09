package com.sillypantscoder.windowlib;

import java.awt.image.BufferedImage;

public abstract class Window {
	public RepaintingPanel panel;
	public Window() {
		panel = new RepaintingPanel();
		panel.painter = this::painter;
		panel.keyDown = this::keyDown;
		panel.keyUp = this::keyUp;
	}
	public void open(String title, int width, int height) {
		panel.run(title, width, height);
	}
	public BufferedImage painter(int width, int height) {
		return this.frame(width, height).img;
	}
	public abstract Surface frame(int width, int height);
	public abstract void keyDown(String e);
	public abstract void keyUp(String e);
}
