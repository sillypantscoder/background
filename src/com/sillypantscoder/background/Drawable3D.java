package com.sillypantscoder.background;

import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;

public interface Drawable3D {
	public Rect getRect();
	public void draw(Surface s, Rect drawRect, double brightness);
}
