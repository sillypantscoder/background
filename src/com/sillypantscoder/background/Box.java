package com.sillypantscoder.background;

import java.awt.Color;
import java.util.ArrayList;

import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;

public class Box {
	public static enum PhysicsState {
		NONE,
		FIXED,
		PHYSICS
	}
	public ArrayList<Box> world;
	public Rect rect;
	public PhysicsState physics;
	public boolean touchingGround;
	public double vx;
	public double vy;
	public Box(ArrayList<Box> world, Rect rect, PhysicsState physics) {
		this.world = world;
		this.rect = rect;
		this.physics = physics;
		this.vx = 0;
		this.vy = 0;
	}
	public void spawn() {
		this.world.add(this);
	}
	public void remove() {
		this.world.remove(this);
	}
	public void draw(Surface s, Rect drawRect, double brightness) {
		s.drawRect(new Color((int)(brightness), (int)(brightness), (int)(brightness)), drawRect);
	}
	public Rect getFeet() {
		final int padding = 5;
		return new Rect(rect.left() + padding, rect.bottom(), rect.width() - padding, padding);
	}
	public Rect getHead() {
		final int padding = 5;
		return new Rect(rect.left() + padding, rect.top() - padding, rect.width() - padding, padding * 2);
	}
	public void checkTouchingGround() {
		Rect feet = getFeet();
		this.touchingGround = false;
		for (int i = 0; i < world.size(); i++) {
			Box box = world.get(i);
			if (box == this) continue;
			if (box.physics == PhysicsState.NONE) continue;
			if (feet.colliderect(box.rect)) {
				this.touchingGround = true;
			}
		}
	}
	public void tick() {
		if (this.physics == PhysicsState.PHYSICS) {
			// V
			this.vy += 0.5;
			this.vy *= 0.98;
			this.hzDamp();
			// Touching ground?
			checkTouchingGround();
			// Collision
			collideX();
			collideY();
		} else {
			// Move rect without collision
			this.rect = this.rect.move(this.vx, this.vy);
		}
		// Too low!
		if (rect.top() > 1000) {
			fallVoid();
		}
	}
	public void hzDamp() {
		this.vx *= 0.9;
	}
	public void collideX() {
		Rect newRect = this.rect.move(this.vx, 0);
		boolean canContinue = true;
		for (int i = 0; i < world.size(); i++) {
			Box box = world.get(i);
			if (box == this) continue;
			// Check for collision
			if (! box.rect.colliderect(newRect)) continue;
			if (box.physics == PhysicsState.PHYSICS) {
				this.vx /= 2;
				box.vx += this.vx;
				canContinue = false;
			} else if (box.physics == PhysicsState.FIXED) {
				this.vx *= -0.25;
				canContinue = false;
			}
		}
		if (canContinue) {
			this.rect = newRect;
		}
	}
	public void collideY() {
		Rect newRect = this.rect.move(0, this.vy);
		boolean canContinue = true;
		for (int i = 0; i < world.size(); i++) {
			Box box = world.get(i);
			if (box == this) continue;
			// Check for collision
			if (! box.rect.colliderect(newRect)) continue;
			if (box.physics == PhysicsState.PHYSICS) {
				canContinue = false;
				this.vy /= 2;
				box.vy += this.vy;
				// Align X motion
				double diff = (this.vx - box.vx) / 8;
				this.vx -= diff;
				box.vx += diff;
			} else if (box.physics == PhysicsState.FIXED) {
				this.vy = 0;
				canContinue = false;
			}
		}
		if (canContinue) {
			this.rect = newRect;
		}
	}
	public void fallVoid() {
		this.remove();
	}
	public void cancelFromCollision() {
		for (int i = 0; i < world.size(); i++) {
			Box box = world.get(i);
			if (box == this) continue;
			// Check for collision
			if (box.rect.colliderect(this.rect)) {
				world.remove(this);
				return;
			}
		}
	}
}
