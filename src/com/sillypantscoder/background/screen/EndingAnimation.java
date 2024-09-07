package com.sillypantscoder.background.screen;

import com.sillypantscoder.background.MainWindow;

public class EndingAnimation extends OpeningAnimation {
	public Screen prevScreen;
	public EndingAnimation(MainWindow win, Screen prevScreen, Screen nextScreen) {
		super(win, nextScreen);
		this.animation = 35;
		this.prevScreen = prevScreen;
	}
	public Screen getDisplayScreen() { return prevScreen; }
	public int getAnimationValue() { return animation; }
}
