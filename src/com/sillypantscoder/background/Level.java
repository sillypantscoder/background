package com.sillypantscoder.background;

public abstract class Level {
	public int bestTime = -1;
	public abstract String getName();
	public abstract String getTagline();
	public abstract void build(Game game);
}
