package com.sillypantscoder.background;

import java.awt.Color;

import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.utils.Utils;
import com.sillypantscoder.windowlib.Surface;

public class Boxes {
	public static class Player extends Box {
		public Player(Game world, double x, double y) {
			super(world, new Rect(x, y, 50, 50), PhysicsState.PHYSICS);
		}
		public void draw(Surface s) {
			double cameraX = world.cameraX;
			double cameraY = world.cameraY;
			Rect drawRect = this.rect.move(-cameraX, -cameraY);
			if (this == world.getPlayer()) {
				s.drawRect(Color.BLACK, drawRect);
			} else {
				s.drawRect(Color.BLACK, drawRect, 2);
			}
		}
		public void fallVoid() {
			double oldX = this.rect.x;
			double oldY = this.rect.y;
			this.rect.x = 50;
			this.rect.y = -400;
			this.vx = 0;
			// Instant camera
			world.cameraX += this.rect.x - oldX;
			world.cameraY += this.rect.y - oldY;
		}
	}
	public static class Wall extends Box {
		public Wall(Game world, Rect rect) {
			super(world, rect, PhysicsState.FIXED);
		}
	}
	public static class PhysicsObject extends Box {
		public PhysicsObject(Game world, Rect rect) {
			super(world, rect.move(0, -1), PhysicsState.PHYSICS);
		}
	}
	public static class Ball extends PhysicsObject {
		public Ball(Game world, double x, double y, double size) {
			super(world, new Rect(x, y, size, size));
		}
		public void draw(Surface s) {
			double cameraX = world.cameraX;
			double cameraY = world.cameraY;
			Rect drawRect = this.rect.move(-cameraX, -cameraY);
			s.drawCircle(Color.BLACK, drawRect);
		}
		public void hzDamp() {}
	}
	public static class Cannon extends Box {
		public Cannon(Game world, double x, double y) {
			super(world, new Rect(x, y + 40, 50, 10), PhysicsState.NONE);
		}
		public void draw(Surface s) {
			double cameraX = world.cameraX;
			double cameraY = world.cameraY;
			Rect drawRect = new Rect(rect.x, rect.y - 40, rect.w, rect.h + 40).move(-cameraX, -cameraY);
			s.drawRect(Color.BLACK, drawRect);
		}
		public void tick() {
			super.tick();
			// Add ball
			Ball b = new Ball(world, this.rect.centerX(), this.rect.centerY() - 40, 10);
			world.boxes.add(b);
			b.cancelFromCollision();
			// Accelerate ball
			double diffX = b.rect.centerX() - world.getPlayer().rect.centerX();
			double diffY = b.rect.centerY() - world.getPlayer().rect.centerY();
			double dist = Math.sqrt((diffX * diffX) + (diffY * diffY));
			diffX /= dist;
			diffY /= dist;
			b.vx = diffX * -15;
			b.vy = diffY * -15;
		}
	}
	public static class Button extends Box {
		public boolean pressed;
		public int length;
		public SwitchHandler handler;
		public Button(Game world, double x, double y, SwitchHandler handler) {
			super(world, new Rect(x - 25, y, 50, 0), PhysicsState.FIXED);
			this.handler = handler;
		}
		public void tick() {
			super.tick();
			// Check for pressing
			boolean oldState = this.pressed;
			this.pressed = false;
			for (int i = 0; i < world.boxes.size(); i++) {
				if (world.boxes.get(i).physics != Box.PhysicsState.PHYSICS) continue;
				if (world.boxes.get(i).getFeet().colliderect(getHead())) {
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
		public Door(Game world, Rect rect, double newX, double newY) {
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
		public Target(Game world, Rect rect) {
			super(world, rect, PhysicsState.NONE);
		}
		public void draw(Surface s) {
			double cameraX = world.cameraX;
			double cameraY = world.cameraY;
			Rect drawRect = this.rect.move(-cameraX, -cameraY);
			for (int offset : new int[] { -20, -10, 0, 0, 0, 0, 0, 10, 20 }) {
				s.drawRect(new Color(0, 0, 0, 50), drawRect.move(offset, offset));
			}
		}
		public void tick() {
			super.tick();
			// Check for win
			if (world.endingAnimation == 0) {
				if (world.player1.rect.colliderect(rect)) {
					if (world.player2.rect.colliderect(rect)) {
						world.endingAnimation = 1;
					}
				}
			}
		}
	}
}
