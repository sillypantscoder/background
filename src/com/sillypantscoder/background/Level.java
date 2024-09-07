package com.sillypantscoder.background;

public abstract class Level {
	public boolean completed = false;
	public abstract String getName();
	public abstract String getTagline();
	public abstract void build(Game game);
}
