package com.sillypantscoder.background;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;

import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;

public class Box {
	public static enum PhysicsState {
		NONE,
		FIXED,
		PHYSICS
	}
	public List<Box> world;
	public Rect rect;
	public PhysicsState physics;
	public boolean touchingGround;
	public double vx;
	public double vy;
	public boolean ticked;
	public Box(List<Box> world, Rect rect, PhysicsState physics) {
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
	public static Color getColor(double brightness) { return new Color((int)(brightness), (int)(brightness), (int)(brightness)); }
	public void draw(Surface s, Rect drawRect, double brightness) {
		s.drawRect(getColor(brightness), drawRect);
	}
	public Rect getFeet() {
		final double padding = 1/10d;
		return new Rect(rect.left(), rect.bottom(), rect.width(), padding);
	}
	public Rect getHead() {
		final double padding = 1/10d;
		return new Rect(rect.left(), rect.top() - padding, rect.width(), padding * 2);
	}
	public HashSet<Box> getAbovePhysicsBoxes(boolean recursive) {
		HashSet<Box> boxes = new HashSet<Box>();
		for (Box b : world) {
			if (b.physics != PhysicsState.PHYSICS) continue;
			if (b.getFeet().colliderect(this.getHead())) {
				boxes.add(b);
				if (recursive) boxes.addAll(b.getAbovePhysicsBoxes(true));
			}
		}
		return boxes;
	}
	public HashSet<Box> getBelowPhysicsBoxes() {
		HashSet<Box> boxes = new HashSet<Box>();
		for (Box b : world) {
			if (b.physics != PhysicsState.PHYSICS) continue;
			if (b.getHead().colliderect(this.getFeet())) {
				boxes.add(b);
			}
		}
		return boxes;
	}
	public void checkTouchingGround() {
		Rect feet = getFeet();
		this.touchingGround = false;
		for (int i = 0; i < world.size(); i++) {
			Box box = world.get(i);
			if (box == this) continue;
			if (box.physics == PhysicsState.NONE) continue;
			if (feet.colliderect(box.rect) && (box.touchingGround == true || box.physics == PhysicsState.FIXED)) {
				this.touchingGround = true;
			}
		}
	}
	public void tick() {
		if (this.physics == PhysicsState.PHYSICS) {
			// V
			this.vy += 0.012;
			this.vy *= 0.99;
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
		if (rect.top() > 20) {
			fallVoid();
		}
	}
	public void hzDamp() {
		this.vx *= 0.9;
	}
	public List<Box> getInteractions() {
		return world;
	}
	public void collideX() {
		Rect newRect = this.rect.move(this.vx, 0);
		boolean canContinue = true;
		for (Box box : getInteractions()) {
			if (box == this) continue;
			// Check for collision
			if (! box.rect.colliderect_strict(newRect)) continue;
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
		for (Box box : getInteractions()) {
			if (box == this) continue;
			// Check for collision
			if (! box.rect.colliderect_strict(newRect)) continue;
			if (box.physics == PhysicsState.PHYSICS) {
				canContinue = false;
				this.vy /= 2;
				box.vy += this.vy;
				// Align X motion
				this.vx += box.vx / 8;
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
			if (box.physics == PhysicsState.NONE) continue;
			// Check for collision
			if (box.rect.colliderect(this.rect)) {
				world.remove(this);
				return;
			}
		}
	}
}
