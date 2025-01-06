package com.sillypantscoder.background;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.sillypantscoder.utils.Utils;

public class SaveData {
	public static void save() {
		String saveData = "";
		// Save settings
		for (Settings.Setting<?> setting : Settings.settings) {
			saveData += setting.save();
		}
		// Save levels
		for (int i = 0; i < Levels.levels.length; i++) {
			if (Levels.levels[i].bestTime != -1) {
				saveData += Levels.levels[i].bestTime;
			}
			if (Levels.levels[i].bestCoinTime != -1) saveData += "," + Levels.levels[i].bestCoinTime;
			if (i != Levels.levels.length - 1) saveData += ".";
		}
		// Write data to file
		try {
			FileWriter writer = new FileWriter("save_data.txt");
			try {
				writer.write(saveData);
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public static void load() {
		// Check if file exists
		if (! new File("save_data.txt").exists()) return;
		// Read data
		String data = Utils.readFile("save_data.txt");
		// Read settings
		int settingsWidth = 0;
		for (Settings.Setting<?> setting : Settings.settings) {
			int width = setting.getSaveLength();
			// save setting
			if (settingsWidth + width > data.length()) break;
			String settingData = data.substring(settingsWidth, settingsWidth + width);
			setting.load(settingData);
			// continue
			settingsWidth += width;
		}
		// Read levels
		String[] levels = data.substring(settingsWidth).split("\\.", -1);
		for (int i = 0; i < levels.length; i++) {
			if (levels[i].contains(",")) {
				// Got coin
				String[] times = levels[i].split(",");
				Levels.levels[i].bestCoinTime = Integer.parseInt(times[1]);
				levels[i] = times[0];
			}
			if (levels[i].length() == 0) continue;
			int time = Integer.parseInt(levels[i]);
			Levels.levels[i].bestTime = time;
		}
	}
}
