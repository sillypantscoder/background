package com.sillypantscoder.background;

import java.awt.Color;

import com.sillypantscoder.background.screen.Abstract3DScene;
import com.sillypantscoder.background.screen.EditorScreen;
import com.sillypantscoder.background.screen.MapScreen;
import com.sillypantscoder.background.screen.OpeningAnimation;
import com.sillypantscoder.background.screen.Screen;
import com.sillypantscoder.windowlib.Surface;
import com.sillypantscoder.windowlib.Window;

public class MainWindow extends Window {
	public Screen screen;
	public void open() {
		SaveData.load();
		this.initializeScreen();
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
	public void initializeScreen() {
		int targetLevel = Levels.levels.length;
		for (int i = Levels.levels.length - 1; i >= 0; i--) { if (Levels.levels[i].bestTime == -1) targetLevel = i; }
		// Find correct screen
		Screen targetScreen = new MapScreen(this, Math.min(Levels.levels.length - 1, targetLevel)); // map screen by default
		if (targetLevel < Levels.levels.length) {
			// If cheating, switch directly to game screen
			if (Abstract3DScene.DEBUG_MODE) {
				targetScreen = new EditorScreen(this, targetLevel);
			}
		}
		OpeningAnimation anim = new OpeningAnimation(this, targetScreen);
		anim.maxTime *= 2;
		this.screen = anim;
	}
}
