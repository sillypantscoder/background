package com.sillypantscoder.background.screen;

import java.awt.Color;
import java.util.ArrayList;

import com.sillypantscoder.background.Drawable3D;
import com.sillypantscoder.background.Game;
import com.sillypantscoder.background.MainWindow;
import com.sillypantscoder.background.Settings;
import com.sillypantscoder.utils.Utils;
import com.sillypantscoder.windowlib.Surface;

public class GameScreen extends Abstract3DScene {
	public Game game;
	public boolean levelCompleted;
	public GameScreen(MainWindow window, int level) {
		super(window);
		this.game = new Game(this, level);
	}
	public ArrayList<? extends ArrayList<? extends Drawable3D>> getLayers() { return game.layers; }
	public double getCameraX() { return game.cameraX; }
	public double getCameraY() { return game.cameraY; }
	public Surface frame(int width, int height) {
		game.tick(width, height);
		// Draw Layers
		Surface s = super.frame(width, height);
		// Timer
		if (!levelCompleted) game.timer += 1;
		if (Settings.SHOW_TIMER.value) {
			String time = Utils.formatTime(game.timer);
			Surface timeS = Surface.renderText(30, time, new Color(50, 50, 50));
			int timeX = (width / 2) - (timeS.get_width() / 2);
			s.blit(timeS, timeX, 10);
		}
		return s;
	}
	public void keyDown(String e) {
		if (e.equals("R")) {
			GameScreen newScreen = new GameScreen(window, game.level);
			navigate(new EndingAnimation(window, this, new OpeningAnimation(window, newScreen)));
		} else if (e.equals("Escape") || e.equals("⎋")) {
			MapScreen newScreen = new MapScreen(window, game.level);
			navigate(new EndingAnimation(window, this, new OpeningAnimation(window, newScreen)));
		} else {
			game.keyDown(e);
		}
	}
	public void keyUp(String e) {
		game.keyUp(e);
	}
	public void mouseMoved(int x, int y) {
		game.mouseMoved(x, y);
	}
	public void mouseDown(int x, int y) {
		game.mouseDown(x, y);
	}
	public void mouseUp(int x, int y) {
		game.mouseUp(x, y);
	}
	public void mouseWheel(int amount) {}
}
