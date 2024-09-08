package com.sillypantscoder.background;

import java.awt.Color;

import com.sillypantscoder.windowlib.Surface;

public class Settings {
	public static BooleanSetting SHOW_TIMER = new BooleanSetting("Show timer", false);
	public static Setting<?>[] settings = new Setting[] {
		SHOW_TIMER
	};
	// Setting classes
	public static abstract class Setting<T> {
		public String name;
		public Setting(String name) {
			this.name = name;
		}
		// save + load
		public abstract String save();
		public abstract void load(String data);
		public abstract int getSaveLength();
		// rendering
		public abstract Surface draw(int width);
		public abstract void click(int x, int y);
	}
	public static class BooleanSetting extends Setting<Boolean> {
		public boolean value;
		public double displayValue;
		public BooleanSetting(String name, boolean initialValue) {
			super(name);
			this.value = initialValue;
			this.displayValue = initialValue ? 1 : 0;
		}
		// save + load
		public String save() { return value ? "T" : "F"; }
		public void load(String data) { this.setValue(data.charAt(0) == 'T'); }
		public void setValue(boolean value) { this.value = value; this.displayValue = value ? 1 : 0; }
		public int getSaveLength() { return 1; }
		// rendering
		public Surface draw(int width) {
			// update slider
			int sliderWidth = 40;
			this.displayValue = ((this.displayValue * 4) + (this.value ? 1 : 0)) / 5d;
			// draw title
			Surface title = Surface.renderText(30, name, Color.BLACK);
			Surface s = new Surface(width, title.get_height(), new Color(0, 0, 0, 0));
			s.blit(title, sliderWidth + 20, 0);
			// draw slider
			int center = title.get_height() / 2;
			int rad = title.get_height() / 4;
			s.drawCircle(Color.WHITE, rad, center, rad);
			s.drawCircle(Color.WHITE, sliderWidth - rad, center, rad);
			s.drawRect(Color.WHITE, rad, (center - rad) + 1, sliderWidth - (rad * 2), (rad * 2) - 1);
			int slideLength = sliderWidth - (rad * 2);
			s.drawCircle(Color.BLACK, rad + (slideLength * displayValue), center, rad - 3);
			// finish
			return s;
		}
		public void click(int x, int y) {
			if (x < 40) {
				this.value = !this.value;
			}
		}
	}
}
