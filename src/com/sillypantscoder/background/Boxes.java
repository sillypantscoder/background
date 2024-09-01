package com.sillypantscoder.background;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.utils.Utils;
import com.sillypantscoder.windowlib.Surface;

public class Boxes {
	public static class Text extends Box {
		public String text;
		public int textSize;
		public Text(List<Box> world, double x, double y, String text, int textSize) {
			super(world, new Rect(x, y, Surface.renderText(textSize, text, Color.BLACK).get_width() / 50d, Surface.renderText(textSize, text, Color.BLACK).get_height() / 50d), PhysicsState.NONE);
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
		public double respawnX = 1;
		public double respawnY = -8;
		public Player(Game game, List<Box> world, double x, double y) {
			super(world, new Rect(x, y, 1, 1), PhysicsState.PHYSICS);
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
				game.cameraX += (this.rect.x - oldX) * 50;
				game.cameraY += (this.rect.y - oldY) * 50;
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
		public SwitchHandler[] handlers;
		public Button(List<Box> world, double x, double y, SwitchHandler handler) {
			super(world, new Rect(x - 0.5, y, 1, 1d/64), PhysicsState.FIXED);
			this.handlers = new SwitchHandler[] {
				handler
			};
			if (handler == null) this.handlers = new SwitchHandler[0];
		}
		public Button(List<Box> world, double x, double y, SwitchHandler[] handlers) {
			super(world, new Rect(x - 0.5, y, 1, 1d/64), PhysicsState.FIXED);
			this.handlers = handlers;
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
			for (SwitchHandler handler : this.handlers) {
				if (this.pressed != oldState) {
					if (this.pressed) handler.activate();
					else handler.deactivate();
				}
			}
			// Move
			if (pressed) {
				if (length > 1) {
					length--;
					this.rect.y += 1d/64;
					this.rect.h -= 1d/64;
				}
			} else {
				if (length < 10) {
					length++;
					this.rect.y -= 1d/64;
					this.rect.h += 1d/64;
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
		public HashSet<Box> attached;
		public Door(List<Box> world, Rect rect, double newX, double newY) {
			super(world, rect, PhysicsState.FIXED);
			oldX = rect.x;
			oldY = rect.y;
			this.newX = newX;
			this.newY = newY;
			this.attached = new HashSet<Box>();
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
			// Remember previous position
			double previousX = this.rect.x;
			double previousY = this.rect.y;
			HashSet<Box> aboveBoxes = this.getAbovePhysicsBoxes(true);
			HashSet<Box> belowBoxes = this.getBelowPhysicsBoxes();
			// Set pos
			double anim_amt = Utils.ease_in_out(this.amt);
			this.rect.x = this.oldX + ((this.newX - this.oldX) * anim_amt);
			this.rect.y = this.oldY + ((this.newY - this.oldY) * anim_amt);
			// Find which entities to move
			HashSet<Box> moving = new HashSet<Box>();
			if (this.rect.y <= previousY) {
				moving.addAll(aboveBoxes);
				for (Box b : attached) {
					moving.addAll(b.getAbovePhysicsBoxes(true));
				}
			} else {
				moving.addAll(belowBoxes);
				for (Box b : attached) {
					moving.addAll(b.getBelowPhysicsBoxes());
				}
			}
			for (Box b : attached) {
				moving.add(b);
			}
			// Move entities
			double diffX = this.rect.x - previousX;
			double diffY = this.rect.y - previousY;
			for (Box b : moving) {
				b.rect.x += diffX;
				b.rect.y += diffY;
			}
		}
		public void activate() {
			this.activated = true;
		}
		public void deactivate() {
			this.activated = false;
		}
	}
	public static class End extends Box {
		public Game game;
		public End(Game game, Rect rect) {
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
						game.levelCompleted = true;
						game.endingAnimation = 30;
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
	public static class InvisibleWind extends Box {
		public double amtX;
		public double amtY;
		public InvisibleWind(List<Box> world, Rect rect, double amtX, double amtY) {
			super(world, rect, PhysicsState.NONE);
			this.amtX = amtX;
			this.amtY = amtY;
		}
		public void draw(Surface s, Rect drawRect, double brightness) {}
		public void tick() {
			super.tick();
			// Move objects
			double padding = 0.3;
			Rect r = new Rect(this.rect.x + padding, this.rect.y + padding, this.rect.w - (padding * 2), this.rect.h - (padding * 2));
			for (Box box : world) {
				if (box.physics != PhysicsState.PHYSICS) continue;
				if (box.rect.colliderect(r)) {
					box.vx += amtX;
					box.vy += amtY;
				}
			}
		}
	}
	public static class Wind extends InvisibleWind {
		public static int gridSize = 5;
		public double offsetX;
		public double offsetY;
		public Surface bg;
		public Wind(List<Box> world, Rect rect, double amtX, double amtY) {
			super(world, rect, amtX, amtY);
			// Create bg image
			bg = new Surface((int)(rect.w * 50) + (gridSize * 2), (int)(rect.h * 50) + (gridSize * 2), new Color(0, 0, 0, 0));
			for (int x = 0; x < bg.get_width(); x += 2) {
				for (int y = 0; y < bg.get_height(); y += 2) {
					bg.drawRect(Color.WHITE, x * gridSize, y * gridSize, gridSize, gridSize);
					// bg.drawRect(Color.WHITE, (x + 1) * gridSize, (y + 1) * gridSize, gridSize, gridSize);
				}
			}
			offsetX = 0.1d * gridSize;
			offsetY = 0.1d * gridSize;
		}
		public static Color getColor(double brightness, double multiplier) {
			int realbrightness = (int)(255 - ((255 - brightness) * multiplier));
			return new Color(realbrightness, realbrightness, realbrightness, 255);
		}
		public void draw(Surface s, Rect drawRect, double brightness) {
			s.blit(
				bg.crop((int)(offsetX + gridSize), (int)(offsetY + gridSize), (int)(drawRect.w), (int)(drawRect.h))
				.scaleValues(getColor(brightness, 1/4d).getRed() / 255f)
			, (int)(drawRect.x), (int)(drawRect.y));
			s.drawRect(getColor(brightness, 2/4d), drawRect, 3);
		}
		public void tick() {
			super.tick();
			offsetX -= amtX * 20;
			offsetY -= amtY * 20;
			offsetX %= gridSize * 2;
			offsetY %= gridSize * 2;
		}
	}
}
