package com.sillypantscoder.windowlib;

import java.awt.Color;

public class ArcTest extends Window {
	public static void main(String[] args) {
		new ArcTest().open("Arc Test", 500, 500);
	}
	public int startAngle = 90;
	public int arcAngle = 90;
	public Surface getIcon() {
		return new Surface(40, 40, Color.BLACK);
	}
	public Surface frame(int width, int height) {
		Surface s = new Surface(width, height, new Color(255, 255, 255));
		s._drawArc(Color.BLACK, 0, 0, width, height, 90 - startAngle, 90 - arcAngle);
		return s;
	}
	public void keyDown(String e) {
	}
	public void keyUp(String e) {
	}
	public void mouseMoved(int x, int y) {
		this.startAngle = x;
		this.arcAngle = y;
	}
	public void mouseDown(int x, int y) {
	}
	public void mouseUp(int x, int y) {
	}
	public void mouseWheel(int amount) {
	}
}
