package com.sillypantscoder.background;

import java.util.ArrayList;
import java.util.List;

import com.sillypantscoder.background.Box.PhysicsState;
import com.sillypantscoder.utils.ListCombination;
import com.sillypantscoder.utils.Rect;

public class Levels {
	public static void levelErr(Game game) {
		new Boxes.Wall(game.getLayer(0), new Rect(-100, 250, 250, 50)).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -50, 0);
		game.player1.spawn();
		game.player2 = new Boxes.Player(game, game.getLayer(0), 50, 0);
		game.player2.spawn();
	}
	public static void level0(Game game) {
		new Boxes.Wall(game.getLayer(0), new Rect(-100, 250, 250, 50)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(150, 100, 250, 50)).spawn();
		new Boxes.PhysicsObject(game.getLayer(0), new Rect(200, -300, 50, 50)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(550, -100, 250, 50)).spawn();
		new Boxes.Target(game, new Rect(500, -350, 100, 100)).spawn();
		// Decoration
		new Boxes.Wall(game.getLayer(1), new Rect(-100, 100, 500, 200)).spawn();
		new Boxes.Text(game.getLayer(2), -75, -25, "BACKGROUND", 100).spawn();
		new Boxes.Wall(game.getLayer(3), new Rect(150, -100, 650, 250)).spawn();
		// Instructions
		new Boxes.Text(game.getLayer(1), -50, 300, "Arrow keys", 30).spawn();
		new Boxes.Text(game.getLayer(1), -25, 330, "to move", 30).spawn();
		new Boxes.Text(game.getLayer(1), 650, -50, "Press space to", 30).spawn();
		new Boxes.Text(game.getLayer(1), 675, -20, "switch players", 30).spawn();
		new Boxes.Text(game.getLayer(2), 280, -350, "Both players", 20).spawn();
		new Boxes.Text(game.getLayer(2), 300, -320, "must be touching", 20).spawn();
		new Boxes.Text(game.getLayer(2), 260, -290, "the end to continue", 20).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -50, 0);
		game.player1.spawn();
		game.player2 = new Boxes.Player(game, game.getLayer(0), 50, 0);
		game.player2.spawn();
	}
	public static void level1(Game game) {
		new Boxes.Text(game.getLayer(1), -50, 100, "1", 80).spawn();
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
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -50, 0);
		game.player1.spawn();
		game.player2 = new Boxes.Player(game, game.getLayer(0), 50, 0);
		game.player2.spawn();
	}
	public static void level2(Game game) {
		new Boxes.Text(game.getLayer(1), -175, 200, "2", 80).spawn();
		// Create both layer
		ListCombination<Box> bothLayer;
		{
			ArrayList<ArrayList<Box>> bothLayerC = new ArrayList<ArrayList<Box>>();
			bothLayerC.add(game.getLayer(0));
			bothLayerC.add(game.getLayer(1));
			bothLayer = new ListCombination<Box>(bothLayerC);
		}
		new Boxes.Wall(bothLayer, new Rect(-100, 250, 250, 50)).spawn(); // Starting platform
		new Boxes.Wall(game.getLayer(1), new Rect(200, 250, 250, 50)).spawn();
		new Boxes.PhysicsObject(bothLayer, new Rect(200, 0, 50, 50)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(500, 100, 50, 50)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(450, 200, 450, 50)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(425, 350, 500, 50)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(950, 325, 50, 50)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(425, 350, 100, 50)).spawn();
		{
			Boxes.Door door = new Boxes.Door(bothLayer, new Rect(550, -125, 50, 400), 550, 100);
			door.spawn();
			new Boxes.Button(bothLayer, 475, 350, door).spawn();
		}
		new Boxes.Wall(game.getLayer(0), new Rect(600, 0, 100, 50)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(675, 50, 100, 50)).spawn();
		new Boxes.Target(game, new Rect(625, -125, 100, 100)).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -50, 0);
		game.player1.spawn();
		game.player2 = new Boxes.Player(game, game.getLayer(1), 50, 0);
		game.player2.spawn();
	}
	public static void level3(Game game) {
		new Boxes.Text(game.getLayer(1), 300, 200, "3", 80).spawn();
		// Create both layer
		ListCombination<Box> bothLayer;
		{
			ArrayList<ArrayList<Box>> bothLayerC = new ArrayList<ArrayList<Box>>();
			bothLayerC.add(game.getLayer(0));
			bothLayerC.add(game.getLayer(1));
			bothLayer = new ListCombination<Box>(bothLayerC);
		}
		// Platform
		new Boxes.Wall(bothLayer, new Rect(-100, 250, 250, 50)).spawn(); // starting platform
		new Box(bothLayer, new Rect(-100, 250, 250, 300), PhysicsState.NONE).spawn(); // to cover the opening
		new Boxes.Wall(bothLayer, new Rect(-100, 100, 1600, 50)).spawn(); // roof
		new Boxes.Wall(bothLayer, new Rect(-100, 100, 50, 450)).spawn(); // left wall
		Boxes.Wall belt = (new Boxes.Wall(game.getLayer(1), new Rect(-100, 500, 1600, 50)) { // conveyor belt
			public void tick() {
				for (Box b : world) {
					if (b.physics != PhysicsState.PHYSICS) continue;
					if (this.getHead().colliderect(b.getFeet())) {
						b.vx += 0.3;
					}
				}
				super.tick();
			}
		});
		belt.spawn();
		new Boxes.Spawner(game, 120, () -> {
			return new Box(bothLayer, new Rect(0, 350, 100, 100), PhysicsState.PHYSICS) {
				public List<Box> getInteractions() {
					ArrayList<Box> boxes = new ArrayList<Box>();
					boxes.add(belt);
					boxes.add(game.player1);
					boxes.add(game.player2);
					return boxes;
				}
			};
		}).spawn();
		// Obstacles
		new Boxes.Wall(game.getLayer(1), new Rect(400, 125, 50, 200)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(400, 325, 50, 200)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(550, 125, 50, 200)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(550, 325, 50, 200)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(700, 125, 50, 200)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(700, 325, 50, 200)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(900, 100, 100, 225)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(900, 325, 100, 75)).spawn();
		// End
		new Boxes.Target(game, new Rect(1200, 375, 100, 100)).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -25, 175);
		game.player1.spawn();
		game.player1.setrespawn();
		game.player2 = new Boxes.Player(game, game.getLayer(1), 75, 175);
		game.player2.spawn();
		game.player2.setrespawn();
	}
}
