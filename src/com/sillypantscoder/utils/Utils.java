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
		// calculate
		int frames = time % 60;
		int seconds = Math.floorDiv(time, 60);
		int minutes = Math.floorDiv(seconds, 60);
		seconds -= minutes * 60;
		int s100ths = (int)((frames / 60d) * 100);
		// format string
		String dec = String.valueOf(s100ths);
		if (dec.length() == 1) dec = "0" + dec;
		String sec = String.valueOf(seconds);
		if (sec.length() == 1) sec = "0" + sec;
		return (minutes > 0 ? (minutes + ":" + sec) : seconds) + "." + dec;
	}
}
