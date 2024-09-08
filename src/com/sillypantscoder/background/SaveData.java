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
		for (int i = 0; i < Levels.levels.length; i++) {
			if (Levels.levels[i].bestTime != -1) {
				saveData += Levels.levels[i].bestTime;
			}
			saveData += ".";
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
		String[] levels = data.split("\\.", -1);
		for (int i = 0; i < Levels.levels.length; i++) {
			if (levels[i].length() == 0) continue;
			int time = Integer.parseInt(levels[i]);
			Levels.levels[i].bestTime = time;
		}
	}
	public static Screen getEntryScreen(MainWindow window) {
		int highestLevel = 0;
		for (int i = 0; i < Levels.levels.length; i++) { if (Levels.levels[i].bestTime != -1) highestLevel = i + 1; }
		// Find correct screen
		Screen targetScreen = new GameScreen(window, 0);
		if (highestLevel != 0) {
			targetScreen = new MapScreen(window, highestLevel);
		}
		OpeningAnimation anim = new OpeningAnimation(window, targetScreen);
		anim.maxTime *= 2;
		return anim;
	}
}
