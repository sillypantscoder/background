package com.sillypantscoder.background;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;

import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.windowlib.Surface;

/**
 * A box in the world.
 */
public class Box {
	public static enum PhysicsState {
		NONE,
		FIXED,
		PHYSICS
	}
	/**
	 * The world that this box is a part of.
	 */
	public List<Box> world;
	/**
	 * A rect object describing the position and size of this box.
	 */
	public Rect rect;
	/**
	 * Whether this object has physics or interacts with other boxes.
	 */
	public PhysicsState physics;
	/**
	 * Whether this box is on stable ground.
	 */
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
	/**
	 * Draw this box to the screen.
	 * @param s The surface to draw to.
	 * @param drawRect The rectangle that should be drawn.
	 * @param brightness
	 */
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
	/**
	 * Get the boxes that are supported by this box.
	 */
	public HashSet<Box> getAbovePhysicsBoxes(int maxLayers) {
		HashSet<Box> boxes = new HashSet<Box>();
		for (Box b : world) {
			if (boxes.contains(b)) continue;
			if (b.physics != PhysicsState.PHYSICS) continue;
			if (b.getFeet().colliderect(this.getHead())) {
				boxes.add(b);
				if (maxLayers >= 0) boxes.addAll(b.getAbovePhysicsBoxes(maxLayers - 1));
			}
		}
		return boxes;
	}
	public HashSet<Box> getAboveWalls() {
		HashSet<Box> boxes = new HashSet<Box>();
		for (Box b : world) {
			if (b.getFeet().colliderect(this.getHead())) {
				if (b.physics == PhysicsState.PHYSICS) {
					boxes.addAll(b.getAboveWalls());
				}
				if (b.physics == PhysicsState.FIXED) {
					boxes.add(b);
				}
			}
		}
		return boxes;
	}
	/**
	 * Get the boxes that are supporting this box.
	 */
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
	/**
	 * Check whether this box is touching the ground.
	 */
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
	/**
	 * Move one step ahead in the physics simulation.
	 */
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
	/**
	 * Apply air friction.
	 */
	public void hzDamp() {
		this.vx *= 0.9;
	}
	/**
	 * Get a list of all the boxes that this box can interact with.
	 */
	public List<Box> getInteractions() {
		return world;
	}
	/**
	 * Handle collisions on the X axis.
	 */
	public void collideX() {
		Rect newRect = this.rect.move(this.vx, 0);
		boolean canContinue = true;
		for (Box box : getInteractions()) {
			if (box == this) continue;
			// Check for collision
			if (! box.rect.colliderect_strict(newRect)) continue;
			if (box.physics == PhysicsState.PHYSICS) {
				collideXWithPhysicsBox(box);
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
	public void collideXWithPhysicsBox(Box box) {
		this.vx /= 2;
		box.vx += this.vx;
	}
	/**
	 * Handle collisions on the Y axis.
	 */
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
	/**
	 * This method is called when the box falls into the void.
	 */
	public void fallVoid() {
		this.remove();
	}
	/**
	 * Check whether the box is colliding with anything, and remove this box if so.
	 */
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
