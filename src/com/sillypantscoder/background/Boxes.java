package com.sillypantscoder.background;

import java.awt.Color;
import java.util.List;
import java.util.function.Supplier;

import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.utils.Utils;
import com.sillypantscoder.windowlib.Surface;

public class Boxes {
	public static class Text extends Box {
		public String text;
		public int textSize;
		public Text(List<Box> world, int x, int y, String text, int textSize) {
			super(world, new Rect(x, y, 0, 0), PhysicsState.NONE);
			this.text = text;
			this.textSize = textSize;
		}
		public void draw(Surface s, Rect drawRect, double brightness) {
			Surface t = Surface.renderText(textSize, text, getColor(brightness));
			s.blit(t, (int)(drawRect.x), (int)(drawRect.y));
		}
	}
	public static class Player extends Box {
		public Game game;
		public double respawnX = 50;
		public double respawnY = -400;
		public Player(Game game, List<Box> world, double x, double y) {
			super(world, new Rect(x, y, 50, 50), PhysicsState.PHYSICS);
			this.game = game;
		}
		public void draw(Surface s, Rect drawRect, double brightness) {
			if (this == game.getPlayer()) {
				s.drawRect(getColor(brightness), drawRect);
			} else {
				s.drawRect(getColor(brightness), drawRect, 2);
			}
		}
		public void fallVoid() {
			double oldX = this.rect.x;
			double oldY = this.rect.y;
			this.rect.x = respawnX;
			this.rect.y = respawnY;
			this.vx = 0;
			// Instant camera
			if (this == game.getPlayer()) {
				game.cameraX += this.rect.x - oldX;
				game.cameraY += this.rect.y - oldY;
			}
		}
		public void setrespawn() {
			respawnX = this.rect.x;
			respawnY = this.rect.y;
		}
	}
	public static class Wall extends Box {
		public Wall(List<Box> world, Rect rect) {
			super(world, rect, PhysicsState.FIXED);
		}
	}
	public static class PhysicsObject extends Box {
		public PhysicsObject(List<Box> world, Rect rect) {
			super(world, rect.move(0, -1), PhysicsState.PHYSICS);
		}
	}
	public static class Ball extends PhysicsObject {
		public Ball(List<Box> world, double x, double y, double size) {
			super(world, new Rect(x, y, size, size));
		}
		public void draw(Surface s, Rect drawRect) {
			s.drawCircle(Color.BLACK, drawRect);
		}
		public void hzDamp() {}
	}
	public static class Button extends Box {
		public boolean pressed;
		public int length;
		public SwitchHandler handler;
		public Button(List<Box> world, double x, double y, SwitchHandler handler) {
			super(world, new Rect(x - 25, y, 50, 0), PhysicsState.FIXED);
			this.handler = handler;
		}
		public void tick() {
			super.tick();
			// Check for pressing
			boolean oldState = this.pressed;
			this.pressed = false;
			for (int i = 0; i < world.size(); i++) {
				if (world.get(i).physics != Box.PhysicsState.PHYSICS) continue;
				if (world.get(i).getFeet().colliderect(getHead())) {
					this.pressed = true;
				}
			}
			// Handler
			if (handler != null) {
				if (this.pressed != oldState) {
					if (this.pressed) handler.activate();
					else handler.deactivate();
				}
			}
			// Move
			if (pressed) {
				if (length > 1) {
					length--;
					this.rect.y += 1;
					this.rect.h -= 1;
				}
			} else {
				if (length < 10) {
					length++;
					this.rect.y -= 1;
					this.rect.h += 1;
				}
			}
		}
		public static interface SwitchHandler {
			public void activate();
			public void deactivate();
		}
	}
	public static class Door extends Box implements Button.SwitchHandler {
		public double oldX;
		public double oldY;
		public double newX;
		public double newY;
		public double amt;
		public boolean activated;
		public Door(List<Box> world, Rect rect, double newX, double newY) {
			super(world, rect, PhysicsState.FIXED);
			oldX = rect.x;
			oldY = rect.y;
			this.newX = newX;
			this.newY = newY;
		}
		public void tick() {
			super.tick();
			// Move amt
			if (this.activated) {
				if (this.amt < 1) {
					this.amt += 1/32d;
				}
			} else {
				if (this.amt > 0) {
					this.amt -= 1/32d;
				}
			}
			// Set pos
			double anim_amt = Utils.ease_in_out(this.amt);
			this.rect.x = this.oldX + ((this.newX - this.oldX) * anim_amt);
			this.rect.y = this.oldY + ((this.newY - this.oldY) * anim_amt);
		}
		public void activate() {
			this.activated = true;
		}
		public void deactivate() {
			this.activated = false;
		}
	}
	public static class Target extends Box {
		public Game game;
		public Target(Game game, Rect rect) {
			super(game.getLayer(0), rect, PhysicsState.NONE);
			this.game = game;
		}
		public void draw(Surface s, Rect drawRect, double brightness) {
			for (int offset : new int[] { -10, 0, 0, 0, 0, 0, 10 }) {
				s.drawRect(new Color(0, 0, 0, 50), drawRect.move(offset, offset * 2));
			}
		}
		public void tick() {
			super.tick();
			// Check for win
			if (game.endingAnimation == 0) {
				if (game.player1.rect.colliderect_strict(rect)) {
					if (game.player2.rect.colliderect_strict(rect)) {
						game.endingAnimation = 1;
					}
				}
			}
		}
	}
	public static class Spawner extends Box {
		public int ticks;
		public int maxTicks;
		public Supplier<Box> supplier;
		public Spawner(Game game, int frames, Supplier<Box> supplier) {
			super(game.getLayer(0), new Rect(0, 0, 0, 0), PhysicsState.NONE);
			ticks = frames - 1;
			maxTicks = frames;
			this.supplier = supplier;
		}
		public void draw(Surface s, Rect drawRect, double brightness) {}
		public void tick() {
			ticks += 1;
			if (ticks >= maxTicks) {
				ticks = 0;
				// Attempt to spawn
				Box b = supplier.get();
				b.spawn();
				b.cancelFromCollision();
			}
		}
	}
}
