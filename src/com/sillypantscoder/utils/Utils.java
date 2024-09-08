package com.sillypantscoder.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Utils {
	public static double ease_in_out(double x) {
		if (x < 0.5) {
			return 2 * x * x;
		} else {
			return 1 - (2 * Math.pow(x - 1, 2));
		}
	}
	public static String readFile(String name) {
		try {
			File f = new File(name);
			byte[] bytes = new byte[(int)(f.length())];
			FileInputStream fis = new FileInputStream(f);
			fis.read(bytes);
			fis.close();
			return new String(bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	public static String formatTime(int time) {
		String seconds = String.valueOf(time % 60);
		if (seconds.length() == 1) seconds = "0" + seconds;
		return Math.floorDiv(time, 60) + "." + seconds;
	}
}
