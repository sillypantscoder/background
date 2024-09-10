package com.sillypantscoder.background;

import java.io.File;
import java.io.FileWriter;

import com.sillypantscoder.background.screen.GameScreen;
import com.sillypantscoder.background.screen.MapScreen;
import com.sillypantscoder.background.screen.OpeningAnimation;
import com.sillypantscoder.background.screen.Screen;
import com.sillypantscoder.utils.Utils;

public class SaveData {
	public static void save() {
		String saveData = "";
		// Save settings
		for (int i = 0; i < Settings.settings.length; i++) {
			saveData += Settings.settings[i].save();
		}
		// Save levels
		for (int i = 0; i < Levels.levels.length; i++) {
			if (Levels.levels[i].bestTime != -1) {
				saveData += Levels.levels[i].bestTime;
			}
			if (i != Levels.levels.length - 1) saveData += ".";
		}
		// Write data to file
		try {
			FileWriter writer = new FileWriter("save_data.txt");
			writer.write(saveData);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void load() {
		// Check if file exists
		if (! new File("save_data.txt").exists()) return;
		// Read data
		String data = Utils.readFile("save_data.txt");
		// Read settings
		int settingsWidth = 0;
		for (int i = 0; i < Settings.settings.length; i++) {
			Settings.Setting<?> setting = Settings.settings[i];
			int width = setting.getSaveLength();
			// save setting
			String settingData = data.substring(settingsWidth, settingsWidth + width);
			setting.load(settingData);
			// continue
			settingsWidth += width;
		}
		// Read levels
		String[] levels = data.substring(settingsWidth).split("\\.", -1);
		for (int i = 0; i < levels.length; i++) {
			if (levels[i].length() == 0) continue;
			int time = Integer.parseInt(levels[i]);
			Levels.levels[i].bestTime = time;
		}
	}
	public static Screen getEntryScreen(MainWindow window) {
		int highestLevel = 0;
		for (int i = 0; i < Levels.levels.length; i++) { if (Levels.levels[i].bestTime != -1) highestLevel = i + 1; }
		// Find correct screen
		Screen targetScreen = new GameScreen(window, highestLevel);
		if (highestLevel != 0) {
			boolean forceGameScreen = highestLevel < Levels.levels.length;
			if (! (forceGameScreen && Game.CHEAT)) {
				if (highestLevel == Levels.levels.length) highestLevel -= 1;
				targetScreen = new MapScreen(window, highestLevel);
			}
		}
		OpeningAnimation anim = new OpeningAnimation(window, targetScreen);
		anim.maxTime *= 2;
		return anim;
	}
}
