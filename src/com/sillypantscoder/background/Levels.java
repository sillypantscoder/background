package com.sillypantscoder.background;

import com.sillypantscoder.utils.Rect;

public class Levels {
	public static void level1(Game game) {
		game.boxes.add(new Boxes.Wall(game, new Rect(-100, 250, 250, 50)));
		game.boxes.add(new Boxes.Wall(game, new Rect(150, 150, 50, 100)));
		game.boxes.add(new Boxes.Wall(game, new Rect(450, 150, 150, 50)));
		game.boxes.add(new Boxes.Wall(game, new Rect(600, 300, 100, 50)));
		{
			Boxes.Door door = new Boxes.Door(game, new Rect(700, -200, 50, 150), 700, 0);
			game.boxes.add(door);
			game.boxes.add(new Boxes.Button(game, 650, 300, door));
			game.boxes.add(new Boxes.Button(game, 400, -300, door));
		}
		game.boxes.add(new Boxes.Wall(game, new Rect(900, -150, 50, 50)));
		game.boxes.add(new Boxes.Wall(game, new Rect(350, -300, 150, 50)));
		game.boxes.add(new Boxes.Wall(game, new Rect(100, -450, 150, 50)));
		game.boxes.add(new Boxes.Target(game, new Rect(100, -600, 100, 100)));
	}
}
