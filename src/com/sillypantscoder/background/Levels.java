package com.sillypantscoder.background;

import com.sillypantscoder.utils.Rect;

public class Levels {
	public static void level1(Game game) {
		new Boxes.Wall(game.getLayer(0), new Rect(-100, 250, 250, 50)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(150, 150, 50, 100)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(450, 150, 150, 50)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(600, 300, 100, 50)).spawn();
		{
			Boxes.Door door = new Boxes.Door(game.getLayer(0), new Rect(700, -200, 50, 150), 700, 0);
			door.spawn();
			new Boxes.Button(game.getLayer(0), 650, 300, door).spawn();
			new Boxes.Button(game.getLayer(0), 400, -300, door).spawn();
		}
		new Boxes.Wall(game.getLayer(0), new Rect(900, -150, 50, 50)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(350, -300, 150, 50)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(100, -450, 150, 50)).spawn();
		new Boxes.Target(game, new Rect(100, -600, 100, 100)).spawn();
		// Background
		for (int i = 1; i < 5; i++) {
			new Boxes.Wall(game.getLayer(i), new Rect(-100, 250, 250, 50)).spawn();
			new Boxes.Wall(game.getLayer(i), new Rect(100, -450, 150, 50)).spawn();
		}
		new Boxes.Wall(game.getLayer(5), new Rect(-100, -450, 350, 750)).spawn();
		new Boxes.Wall(game.getLayer(3), new Rect(450, -200, 550, 400)).spawn();
	}
}
