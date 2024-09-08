package com.sillypantscoder.background.screen;

import java.awt.Color;

import com.sillypantscoder.background.SaveData;
import com.sillypantscoder.background.Settings;
import com.sillypantscoder.windowlib.Surface;

public class SettingsScreen extends Screen {
	public static Surface settingsIcon = makeSettingsIcon();
	public static Surface makeSettingsIcon() {
		Surface s = new Surface(100, 100, new Color(0, 0, 0, 0));
		s.drawCircle(new Color(50, 50, 50), 50, 50, 50);
		s.drawCircle(new Color(220, 220, 220), 50, 50, 25);
		// lines
		s.drawLine(new Color(220, 220, 220), 50, 20, 50, 80, 10);
		s.drawLine(new Color(220, 220, 220), 20, 50, 80, 50, 10);
		s.drawLine(new Color(220, 220, 220), 30, 30, 70, 70, 10);
		s.drawLine(new Color(220, 220, 220), 30, 70, 70, 30, 10);
		// inner circle
		s.drawCircle(new Color(50, 50, 50), 50, 50, 10);
		return s;
	}
	// screen
	public MapScreen parent;
	public int width;
	public int height;
	public SettingsScreen(MapScreen parent) {
		super(parent.window);
		this.parent = parent;
	}
	public Surface frame(int width, int height) {
		this.width = width;
		this.height = height;
		Surface s = parent.frame(width, height);
		// Draw background & title
		s.drawRect(new Color(200, 200, 200), 50, 60, width - 100, height - 100);
		Surface title = Surface.renderText(50, "Settings", new Color(50, 50, 50));
		s.blit(title, 50, 50);
		// Draw settings
		int cum_y = title.get_height() + 50;
		for (int i = 0; i < Settings.settings.length; i++) {
			Settings.Setting<?> setting = Settings.settings[i];
			// render
			Surface d = setting.draw(width - 120);
			s.blit(d, 70, cum_y);
			cum_y += d.get_height();
		}
		// Return
		return s;
	}
	public void keyDown(String e) {}
	public void keyUp(String e) {}
	public void mouseMoved(int x, int y) {}
	public void mouseDown(int x, int y) {}
	public void mouseUp(int x, int y) {
		if (x < 50 || y < 50 || x > width - 100 || y > height - 100) {
			navigate(parent);
			return;
		}
		// Check settings
		Surface title = Surface.renderText(50, "Settings", new Color(50, 50, 50));
		int cum_y = title.get_height() + 50;
		for (int i = 0; i < Settings.settings.length; i++) {
			Settings.Setting<?> setting = Settings.settings[i];
			// render
			Surface d = setting.draw(width - 120);
			cum_y += d.get_height();
			// check for click
			if (y < cum_y) {
				int clickX = x - 70;
				int clickY = y - (cum_y - d.get_height());
				setting.click(clickX, clickY);
				// End
				SaveData.save();
				return;
			}
		}
	}
	public void mouseWheel(int amount) {}
}
