package com.sillypantscoder.background.screen;

import java.awt.Color;

import com.sillypantscoder.background.Level;
import com.sillypantscoder.background.Levels;
import com.sillypantscoder.background.MainWindow;
import com.sillypantscoder.windowlib.Surface;

public class LevelTitleScreen extends Screen {
	public int level;
	public int time;
	public Surface titleRender;
	public Surface taglineRender;
	public LevelTitleScreen(MainWindow window, int level) {
		super(window);
		this.level = level;
		this.time = 0;
	}
	public Surface frame(int width, int height) {
		if (this.time == 0) updateRenders(width, height);
		// Create surface
		this.time += 1;
		Surface s = new Surface(width, height, Color.BLACK);
		// Draw title
		int titleX = (width / 2) - (titleRender.get_width() / 2);
		int titleY = (height / 2) - (titleRender.get_height() + 30);
		if (this.time >= 130) {
			float amount = (20 - (this.time - 130)) / 20f;
			s.blit(titleRender.scaleValues(amount), titleX, titleY);
		} else if (this.time >= 30) {
			s.blit(titleRender, titleX, titleY);
		} else if (this.time >= 5) {
			float amount = (this.time - 5) / 25f;
			s.blit(titleRender.scaleValues(amount), titleX, titleY);
		}
		// Draw tagline
		int taglineX = (width / 2) - (taglineRender.get_width() / 2);
		int taglineY = (height / 2) + 30;
		if (this.time >= 130) {
			float amount = (20 - (this.time - 130)) / 20f;
			s.blit(taglineRender.scaleValues(amount), taglineX, taglineY);
		} else if (this.time >= 80) {
			s.blit(taglineRender, taglineX, taglineY);
		} else if (this.time >= 60) {
			float amount = (this.time - 60) / 20f;
			s.blit(taglineRender.scaleValues(amount), taglineX, taglineY);
		}
		// Continue
		if (this.time >= 170) {
			GameScreen newScreen = new GameScreen(window, level);
			OpeningAnimation anim = new OpeningAnimation(window, newScreen);
			anim.maxTime *= 2;
			navigate(anim);
		}
		// Return
		return s;
	}
	public void updateRenders(int width, int height) {
		Level l = Levels.levels[level];
		this.titleRender = Surface.renderWrappedText(50, l.getName().toUpperCase(), new Color(200, 200, 200), width);
		this.taglineRender = Surface.renderWrappedText(35, l.getTagline(), new Color(100, 100, 100), width);
	}
	public void keyDown(String e) {}
	public void keyUp(String e) {}
	public void mouseMoved(int x, int y) {}
	public void mouseDown(int x, int y) {}
	public void mouseUp(int x, int y) {}
	public void mouseWheel(int amount) {}
}
