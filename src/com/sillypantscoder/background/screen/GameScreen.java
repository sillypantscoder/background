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
	public static boolean TAS_MODE = false;
	public Game game;
	public boolean levelCompleted;
	public int lastWidth;
	public int lastHeight;
	public int continueTime;
	public ArrayList<Game> undoStack;
	public GameScreen(MainWindow window, int level) {
		super(window);
		this.game = new Game(this, level);
		continueTime = -1;
		this.undoStack = new ArrayList<Game>();
		this.undoStack.add(this.game.copy());
	}
	public ArrayList<? extends ArrayList<? extends Drawable3D>> getLayers() { return game.layers; }
	public double getCameraX() { return game.cameraX; }
	public double getCameraY() { return game.cameraY; }
	public Surface frame(int width, int height) {
		this.lastWidth = width;
		this.lastHeight = height;
		// ticking
		boolean ticked = (!TAS_MODE) || continueTime == 0
			|| (continueTime >= 15 && continueTime % 5 == 0);
		if (continueTime > -1) continueTime += 1;
		if (ticked) {
			game.tick(width, height);
			if (TAS_MODE) this.undoStack.add(this.game.copy());
		}
		// Draw Layers
		Surface s = super.frame(width, height);
		// Timer
		if (ticked && !levelCompleted) game.timer += 1;
		if (Settings.SHOW_TIMER.value) {
			String time = Utils.formatTime(game.timer);
			Surface timeS = Surface.renderText(30, time, new Color(50, 50, 50));
			int timeX = (width / 2) - (timeS.get_width() / 2);
			s.blit(timeS, timeX, 10);
		}
		return s;
	}
	public void keyDown(String e) {
		if (e.equals("P") && TAS_MODE) {
			this.continueTime = 0;
		} else if (e.equals("V") && TAS_MODE) {
			if (undoStack.isEmpty()) return;
			Game newGame = undoStack.remove(undoStack.size() - 1);
			this.game = newGame.copy();
		} else if (e.equals("R")) {
			GameScreen newScreen = new GameScreen(window, game.level);
			navigate(new EndingAnimation(window, this, new OpeningAnimation(window, newScreen)));
		} else if (e.equals("Escape") || e.equals("⎋")) {
			if (this.window.screen instanceof EndingAnimation) return;
			MapScreen newScreen = new MapScreen(window, game.level);
			navigate(new EndingAnimation(window, this, new OpeningAnimation(window, newScreen)));
		} else {
			game.keyDown(e);
		}
	}
	public void keyUp(String e) {
		if (e.equals("P") && TAS_MODE) this.continueTime = -1;
		game.keyUp(e);
	}
	public void mouseMoved(int x, int y) {
		super.mouseMoved(x, y);
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