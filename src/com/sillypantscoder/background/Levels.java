package com.sillypantscoder.background;

import java.util.ArrayList;
import java.util.List;

import com.sillypantscoder.background.Box.PhysicsState;
import com.sillypantscoder.background.Boxes.Button.SwitchHandler;
import com.sillypantscoder.utils.ListCombination;
import com.sillypantscoder.utils.Rect;

public class Levels {
	public static void levelErr(Game game) {
		new Boxes.Wall(game.getLayer(0), new Rect(-2, 5, 5, 1)).spawn();
		new Boxes.Text(game.getLayer(1), -4, 1, "There are no more levels!", 30).spawn();
		new Boxes.Text(game.getLayer(1), -3.5, 1.6, "You've done them all!", 30).spawn();
		new Boxes.Text(game.getLayer(1), -3, 2.2, ":)", 30).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -1, 0);
		game.player1.spawn();
		game.player2 = new Boxes.Player(game, game.getLayer(0), 1, 0);
		game.player2.spawn();
	}
	public static void level0(Game game) {
		new Boxes.Wall(game.getLayer(0), new Rect(-2, 5, 5, 1)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(3, 2, 5, 1)).spawn();
		new Boxes.PhysicsObject(game.getLayer(0), new Rect(4, -6, 1, 1)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(11, -2, 5, 1)).spawn();
		new Boxes.End(game, new Rect(10, -6, 2, 2)).spawn();
		// Decoration
		new Boxes.Wall(game.getLayer(1), new Rect(-2, 2, 10, 4)).spawn();
		new Boxes.Text(game.getLayer(2), -1.5, -0.5, "BACKGROUND", 100).spawn();
		new Boxes.Wall(game.getLayer(3), new Rect(3, -2, 13, 5)).spawn();
		// Instructions
		new Boxes.Text(game.getLayer(1), -1, 6, "Arrow keys", 30).spawn();
		new Boxes.Text(game.getLayer(1), -0.5, 6.6, "to move", 30).spawn();
		new Boxes.Text(game.getLayer(1), 13, -0.5, "Press space to", 30).spawn();
		new Boxes.Text(game.getLayer(1), 13.5, -0.1, "switch players", 30).spawn();
		new Boxes.Text(game.getLayer(2), 5.6, -7, "Both players", 20).spawn();
		new Boxes.Text(game.getLayer(2), 6, -6.6, "must be touching", 20).spawn();
		new Boxes.Text(game.getLayer(2), 5.4, -6.2, "the end to continue", 20).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -1, 0);
		game.player1.spawn();
		game.player2 = new Boxes.Player(game, game.getLayer(0), 1, 0);
		game.player2.spawn();
	}
	public static void level1(Game game) {
		new Boxes.Text(game.getLayer(1), -1, 2, "1", 80).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(-2, 5, 5, 1)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(3, 3, 1, 2)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(9, 3, 3, 1)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(12, 6, 2, 1)).spawn();
		{
			Boxes.Door door = new Boxes.Door(game.getLayer(0), new Rect(14, -4, 1, 3), 14, 0);
			door.spawn();
			new Boxes.Button(game.getLayer(0), 13, 6, door).spawn();
			new Boxes.Button(game.getLayer(0), 8, -6, door).spawn();
		}
		new Boxes.Wall(game.getLayer(0), new Rect(18, -3, 1, 1)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(7, -6, 3, 1)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(2, -9, 3, 1)).spawn();
		new Boxes.End(game, new Rect(2, -12, 2, 2)).spawn();
		// Background
		for (int i = 1; i < 5; i++) {
			new Boxes.Wall(game.getLayer(i), new Rect(-2, 5, 5, 1)).spawn();
			new Boxes.Wall(game.getLayer(i), new Rect(2, -9, 3, 1)).spawn();
		}
		new Boxes.Wall(game.getLayer(5), new Rect(-2, -9, 7, 15)).spawn();
		new Boxes.Wall(game.getLayer(3), new Rect(9, -4, 11, 8)).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -1, 0);
		game.player1.spawn();
		game.player2 = new Boxes.Player(game, game.getLayer(0), 1, 0);
		game.player2.spawn();
	}
	public static void level2(Game game) {
		new Boxes.Text(game.getLayer(1), -3.5, 4, "2", 80).spawn();
		// Create both layer
		ListCombination<Box> bothLayer;
		{
			ArrayList<ArrayList<Box>> bothLayerC = new ArrayList<ArrayList<Box>>();
			bothLayerC.add(game.getLayer(0));
			bothLayerC.add(game.getLayer(1));
			bothLayer = new ListCombination<Box>(bothLayerC);
		}
		new Boxes.Wall(game.getLayer(0), new Rect(-2, 5, 5, 1)).spawn(); // Starting platform
		new Boxes.Wall(game.getLayer(1), new Rect(-2, 5, 11, 1)).spawn();
		new Boxes.PhysicsObject(bothLayer, new Rect(4, 0, 1, 1)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(10, 2, 1, 1)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(9, 4, 9, 1)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(8.5, 7, 10, 1)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(19, 6.5, 1, 1)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(8.5, 7, 2, 1)).spawn();
		{
			Boxes.Door door = new Boxes.Door(bothLayer, new Rect(11, -2.5, 1, 8), 11, 2);
			door.spawn();
			new Boxes.Button(bothLayer, 9.5, 7, door).spawn();
		}
		new Boxes.Wall(game.getLayer(0), new Rect(12, 0, 2, 1)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(13.5, 1, 2, 1)).spawn();
		new Boxes.End(game, new Rect(12.5, -2.5, 2, 2)).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -1, 0);
		game.player1.spawn();
		game.player2 = new Boxes.Player(game, game.getLayer(1), 1, 0);
		game.player2.spawn();
	}
	public static void level3(Game game) {
		new Boxes.Text(game.getLayer(1), 6, 4, "3", 80).spawn();
		// Create both layer
		ListCombination<Box> bothLayer;
		{
			ArrayList<ArrayList<Box>> bothLayerC = new ArrayList<ArrayList<Box>>();
			bothLayerC.add(game.getLayer(0));
			bothLayerC.add(game.getLayer(1));
			bothLayer = new ListCombination<Box>(bothLayerC);
		}
		// Platform
		new Boxes.Wall(bothLayer, new Rect(-2, 5, 5, 1)).spawn(); // starting platform
		new Box(bothLayer, new Rect(-2, 5, 5, 6), PhysicsState.NONE).spawn(); // to cover the opening
		new Boxes.Wall(bothLayer, new Rect(-2, 2, 32, 1)).spawn(); // roof
		new Boxes.Wall(bothLayer, new Rect(-2, 2, 1, 9)).spawn(); // left wall
		Boxes.Wall belt = (new Boxes.Wall(game.getLayer(1), new Rect(-2, 10, 32, 1)) { // conveyor belt
			public void tick() {
				for (Box b : world) {
					if (b.physics != PhysicsState.PHYSICS) continue;
					if (this.getHead().colliderect(b.getFeet())) {
						b.vx += 0.006;
					}
				}
				super.tick();
			}
		});
		belt.spawn();
		new Boxes.Spawner(game, 120, () -> {
			return new Box(bothLayer, new Rect(0, 7, 2, 2), PhysicsState.PHYSICS) {
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
		new Boxes.Wall(game.getLayer(1), new Rect(8, 2.5, 1, 4)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(8, 6.5, 1, 4)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(11, 2.5, 1, 4)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(11, 6.5, 1, 4)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(14, 2.5, 1, 4)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(14, 6.5, 1, 4)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(18, 2, 2, 4.5)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(18, 6.5, 2, 1.5)).spawn();
		// End
		new Boxes.End(game, new Rect(24, 7.5, 2, 2)).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -0.5, 3.5);
		game.player1.spawn();
		game.player1.setrespawn();
		game.player2 = new Boxes.Player(game, game.getLayer(1), 1.5, 3.5);
		game.player2.spawn();
		game.player2.setrespawn();
	}
	public static void level4(Game game) {
		new Boxes.Text(game.getLayer(1), 2, 6.5, "4", 80).spawn();
		// Create both layer
		ListCombination<Box> bothLayer;
		{
			ArrayList<ArrayList<Box>> bothLayerC = new ArrayList<ArrayList<Box>>();
			bothLayerC.add(game.getLayer(0));
			bothLayerC.add(game.getLayer(1));
			bothLayer = new ListCombination<Box>(bothLayerC);
		}
		// Platform
		new Boxes.Wall(game.getLayer(0), new Rect(-2, 5, 11, 1)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(-2, 5, 7, 1)).spawn();
		(new Boxes.PhysicsObject(bothLayer, new Rect(3, 4, 1, 1)) {
			public void tick() {
				this.vy -= 0.01;
				super.tick();
				if (this.touchingGround == false) this.vx *= 1.1;
				else this.vx *= 0.95;
			}
		}).spawn();
		// Dip
		new Boxes.Wall(game.getLayer(1), new Rect(5, 2.5, 1, 1)).spawn(); // floating block
		new Boxes.Wall(game.getLayer(1), new Rect(4, 5, 1, 3)).spawn(); // left wall
		new Boxes.Wall(game.getLayer(1), new Rect(4, 7, 6, 1)).spawn(); // floor
		new Boxes.Wall(game.getLayer(1), new Rect(9, 4, 1, 4)).spawn(); // right wall
		// Platforms middle
		new Boxes.Wall(bothLayer, new Rect(9, 4, 3, 4)).spawn();
		new Boxes.Wall(bothLayer, new Rect(11, 7, 9, 1)).spawn(); // ground
		new Boxes.Wall(bothLayer, new Rect(20, 0, 7, 8)).spawn(); // right wall
		new Boxes.Wind(game.getLayer(1), new Rect(16, -1, 1, 8), 0, -0.03).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(13.5, 0, 2, 1)).spawn(); // floating block
		new Boxes.InvisibleWind(game.getLayer(1), new Rect(16, -15, 3, 10), 0, 0.01).spawn(); // deceleration zone
		new Boxes.Wind(game.getLayer(1), new Rect(12.5, 6, 1, 1), 0.02, 0).spawn(); // softlock prevention I
		new Boxes.Wind(game.getLayer(1), new Rect(18.5, 6, 1, 1), -0.02, 0).spawn(); // softlock prevention II
		new Boxes.InvisibleWind(game.getLayer(0), new Rect(21, -8, 3, 7), -0.0003, 0).spawn(); // anger prevention
		// Door
		{
			Boxes.Door door = new Boxes.Door(game.getLayer(1), new Rect(29, -7, 5, 1), 29, 0);
			door.spawn();
			new Boxes.Button(game.getLayer(0), 22.5, 0, door).spawn();
		}
		new Boxes.End(game, new Rect(34, -2.5, 2, 2)).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(1), -1, 2.5);
		game.player1.spawn();
		game.player2 = new Boxes.Player(game, game.getLayer(1), 1, 2.5);
		game.player2.spawn();
	}
	public static void level5(Game game) {
		new Boxes.Text(game.getLayer(1), 0, -1, "5", 80).spawn();
		// Platform
		new Boxes.Wall(game.getLayer(0), new Rect(-2, 5, 5, 1)).spawn();
		new Boxes.Wall(game.getLayer(0), new Rect(4, 1, 4, 1)).spawn();
		{
			Boxes.Door door = new Boxes.Door(game.getLayer(0), new Rect(-7, 0, 1, 1), -3, 2.5);
			door.spawn();
			new Boxes.Button(game.getLayer(0), 5, 1, door).spawn();
		}
		new Boxes.Wall(game.getLayer(0), new Rect(-6, 0, 2, 1)).spawn();
		{
			Boxes.Door door = new Boxes.Door(game.getLayer(0), new Rect(-4, 0, 1, 1), 3, -2);
			door.spawn();
			new Boxes.Button(game.getLayer(0), -5, 0, door).spawn();
		}
		new Boxes.Wall(game.getLayer(0), new Rect(-2, -4, 5, 1)).spawn();
		{
			Boxes.Door door = new Boxes.Door(game.getLayer(0), new Rect(-3, -4, 1, 1), -5.5, -2.5);
			door.spawn();
			new Boxes.Button(game.getLayer(0), -1, -4, door).spawn();
		}
		new Boxes.End(game, new Rect(-1, -10, 2, 2)).spawn();
		// Decorations
		new Boxes.Wall(game.getLayer(2), new Rect(-3, -5, 6, 11)).spawn();
		new Boxes.Wall(game.getLayer(3), new Rect(-7, -1.5, 15, 3)).spawn();
		new Boxes.Wall(game.getLayer(3), new Rect(-2, -12.5, 4, 5)).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -1, 3);
		game.player1.spawn();
		game.player1.respawnY += 6;
		game.player2 = new Boxes.Player(game, game.getLayer(0), 1, 3);
		game.player2.spawn();
		game.player2.respawnY += 6;
	}
	public static void level6(Game game) {
		new Boxes.Text(game.getLayer(1), -6, 1, "6", 80).spawn();
		// Platform
		new Boxes.Wall(game.getLayer(0), new Rect(-4, 5, 6, 1)).spawn(); // spawn platform
		new Boxes.PhysicsObject(game.getLayer(0), new Rect(-3, 2.1, 1, 1)).spawn(); // block bottom
		new Boxes.Wall(game.getLayer(0), new Rect(4, 0, 1, 6)).spawn(); // wall on right side
		{
			// Floor 1
			Boxes.Door door1 = new Boxes.Door(game.getLayer(0), new Rect(2, 5, 2, 1), 2, 0);
			door1.spawn();
			// Floor 2
			Boxes.Door door2 = new Boxes.Door(game.getLayer(0), new Rect(2, 0, 2, 1), 2, -5);
			door2.spawn();
			// Floor 0
			Boxes.Door door0 = new Boxes.Door(game.getLayer(0), new Rect(2, 10, 2, 1), 2, 5);
			door0.spawn();
			// Floor 1 (left side)
			Boxes.Door door1l = new Boxes.Door(game.getLayer(0), new Rect(-6, 5, 2, 1), -6, 0);
			door1l.spawn();
			// button
			new Boxes.Button(game.getLayer(0), 7, 2, new SwitchHandler[] {
				door1, door2, door0, door1l
			}).spawn();
		}
		new Boxes.Wall(game.getLayer(0), new Rect(5, 2, 4, 1)).spawn(); // hole floor
		new Boxes.Wind(game.getLayer(0), new Rect(5, 1, 1, 1), 0, -0.1).spawn(); // hole wind left
		new Boxes.Wall(game.getLayer(0), new Rect(-4, 0, 6, 1)).spawn(); // above spawn platform
		new Boxes.Wall(game.getLayer(0), new Rect(4, -4, 2, 1)).spawn(); // floor 2 extra platform right
		new Boxes.Wind(game.getLayer(0), new Rect(2, -6, 2, 1), 0.02, 0).spawn(); // floor 2 extra platform wind
		new Boxes.PhysicsObject(game.getLayer(0), new Rect(-1, -6, 1, 1)).spawn(); // block top
		{
			// Far left platform middle part
			Boxes.Door door = new Boxes.Door(game.getLayer(0), new Rect(-15.5, 6, 3, 1), -15.5, -1);
			door.spawn();
			// button (floor 2 extra platform)
			new Boxes.Button(game.getLayer(0), 5, -4, door).spawn();
		}
		{
			// Far left platform right part
			Boxes.Door door = new Boxes.Door(game.getLayer(0), new Rect(-12.5, 6, 3, 1), -12.5, -1);
			door.spawn();
			// button (left part)
			new Boxes.Button(game.getLayer(0), -17, -1, door).spawn();
		}
		new Boxes.Wall(game.getLayer(0), new Rect(-19, -1, 3, 1)).spawn(); // far left platform left part
		new Boxes.End(game, new Rect(-19, -5, 2, 2)).spawn();
		// Decoration
		new Boxes.Wall(game.getLayer(1), new Rect(-4, 0, 9, 6)).spawn();
		new Boxes.Wall(game.getLayer(1), new Rect(4, -4, 2, 7)).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, game.getLayer(0), -1, 3);
		game.player1.spawn();
		game.player1.respawnY += 6;
		game.player2 = new Boxes.Player(game, game.getLayer(0), 1, 3);
		game.player2.spawn();
		game.player2.respawnY += 6;
	}
	public static void level7(Game game) {
		new Boxes.Text(game.getLayer(1), -2, -1, "7", 80).spawn();
		// Create multilayer objects
		ListCombination<Box> bothLayerFront;
		{
			ArrayList<ArrayList<Box>> bothLayerC = new ArrayList<ArrayList<Box>>();
			bothLayerC.add(game.getLayer(0));
			bothLayerC.add(game.getLayer(1));
			bothLayerFront = new ListCombination<Box>(bothLayerC);
		}
		ListCombination<Box> bothLayerBack;
		{
			ArrayList<ArrayList<Box>> bothLayerC = new ArrayList<ArrayList<Box>>();
			bothLayerC.add(game.getLayer(1));
			bothLayerC.add(game.getLayer(2));
			bothLayerBack = new ListCombination<Box>(bothLayerC);
		}
		ListCombination<Box> threeLayer;
		{
			ArrayList<ArrayList<Box>> bothLayerC = new ArrayList<ArrayList<Box>>();
			bothLayerC.add(game.getLayer(0));
			bothLayerC.add(game.getLayer(1));
			bothLayerC.add(game.getLayer(2));
			threeLayer = new ListCombination<Box>(bothLayerC);
		}
		// Platforms
		new Boxes.Wall(threeLayer, new Rect(-4, -2, 6, 1)).spawn(); // top left starting platform
		new Boxes.Wall(threeLayer, new Rect(1, 7, 8, 1)).spawn(); // bottom right starting platform
		new Boxes.Wall(threeLayer, new Rect(2, -6, 4, 1)).spawn(); // top left roof
		// Elevator
		new Boxes.Wall(game.getLayer(0), new Rect(2, -2, 3, 1)).spawn(); // top left elevator entrance
		new Boxes.Wall(game.getLayer(0), new Rect(2, 4, 3, 1)).spawn(); // bottom right elevator entrance
		new Boxes.Wall(threeLayer, new Rect(1, -1, 1, 8)).spawn(); // left wall
		new Boxes.Wall(threeLayer, new Rect(5, -5, 1, 10)).spawn(); // right wall
		new Boxes.Wind(game.getLayer(2), new Rect(2, -2, 1, 9), 0, -0.1).spawn(); // left wind
		// Objects
		new Boxes.PhysicsObject(bothLayerBack, new Rect(-3, -3, 1, 1)).spawn(); // top left block
		new Boxes.Wall(threeLayer, new Rect(8, 8, 4, 1)).spawn(); // bottom right hole bottom
		new Boxes.Wall(threeLayer, new Rect(11, 7, 4, 1)).spawn(); // bottom far right platform
		{
			Boxes.Door doorTopLeft = new Boxes.Door(threeLayer, new Rect(-6, 6, 2, 1), -6, -1.5);
			doorTopLeft.spawn();
			Boxes.Door doorBottomRight = new Boxes.Door(game.getLayer(2), new Rect(7.5, -5, 1, 1), 7.5, 4);
			doorBottomRight.spawn();
			new Boxes.Button(game.getLayer(2), 10, 8, new SwitchHandler[] {
				doorTopLeft, doorBottomRight
			}).spawn();
		}
		// new Boxes.PhysicsObject(bothLayerBack, new Rect(-3, -3, 1, 1)).spawn(); // another top left block?
		new Boxes.PhysicsObject(bothLayerBack, new Rect(7.5, -6, 1, 1)).spawn(); // top right block
		new Boxes.Wall(threeLayer, new Rect(-8, -1, 2, 1)).spawn(); // top far left platform
		{
			Boxes.Door door = new Boxes.Door(threeLayer, new Rect(-1, -8, 3, 1), -1, -4);
			door.spawn(); // top left on top of roof
			new Boxes.Button(game.getLayer(2), -7, -1, door).spawn();
		}
		new Boxes.End(game, new Rect(13, 3.5, 2, 2)).spawn();
		// Player Setup
		game.player1 = new Boxes.Player(game, bothLayerFront, -1, -5);
		game.player1.spawn();
		game.player1.setrespawn();
		game.player2 = new Boxes.Player(game, bothLayerFront, 6, 5);
		game.player2.spawn();
		game.player2.setrespawn();
	}
}
