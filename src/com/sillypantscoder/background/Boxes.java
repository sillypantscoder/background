package com.sillypantscoder.background;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

import com.sillypantscoder.background.screen.EndingAnimation;
import com.sillypantscoder.background.screen.GameScreen;
import com.sillypantscoder.background.screen.MapScreen;
import com.sillypantscoder.background.screen.OpeningAnimation;
import com.sillypantscoder.utils.Rect;
import com.sillypantscoder.utils.Utils;
import com.sillypantscoder.windowlib.Surface;

/**
 * This class contains a bunch of classes to help you create boxes easier.
 */
public class Boxes {
	/**
	 * Non-solid text.
	 */
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
	/**
	 * The player.
	 */
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
	/**
	 * A fixed wall.
	 */
	public static class Wall extends Box {
		public Wall(List<Box> world, Rect rect) {
			super(world, rect, PhysicsState.FIXED);
		}
	}
	/**
	 * A moveable physics object.
	 */
	public static class PhysicsObject extends Box {
		public PhysicsObject(List<Box> world, Rect rect) {
			super(world, rect.move(0, -1), PhysicsState.PHYSICS);
		}
	}
	/**
	 * (Unused) This class is supposed to represent a ball, by drawing a circle instead of a
	 *  square, and removing friction. However, the physics still treats it as a box.
	 */
	public static class Ball extends PhysicsObject {
		public Ball(List<Box> world, double x, double y, double size) {
			super(world, new Rect(x, y, size, size));
		}
		public void draw(Surface s, Rect drawRect) {
			s.drawCircle(Color.BLACK, drawRect);
		}
		public void hzDamp() {}
	}
	/**
	 * A button that can open one or more doors.
	 */
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
			this.pressed = this.isPressed();
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
		public boolean isPressed() {
			return this.getAbovePhysicsBoxes(0).size() > 0;
		}
		/**
		 * This interface represents anything that can be activated and deactivated.
		 */
		public static interface SwitchHandler {
			public void activate();
			public void deactivate();
		}
	}
	/**
	 * A moving platform.
	 */
	public static class MovingPlatform extends Box {
		public HashSet<Box> attached;
		public MovingPlatform(List<Box> world, Rect rect) {
			super(world, rect, PhysicsState.FIXED);
			this.attached = new HashSet<Box>();
		}
		public void tick() {
			super.tick();
			// Remember previous position
			double previousX = this.rect.x;
			double previousY = this.rect.y;
			HashSet<Box> aboveBoxes = this.getAbovePhysicsBoxes(5);
			HashSet<Box> belowBoxes = this.getBelowPhysicsBoxes();
			// Set pos
			this.move();
			// Find which entities to move
			HashSet<Box> moving = new HashSet<Box>();
			if (this.rect.y <= previousY) {
				moving.addAll(aboveBoxes);
				for (Box b : attached) {
					moving.addAll(b.getAbovePhysicsBoxes(5));
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
		public void move() {}
	}
	/**
	 * A moving platform. Its normal position is described by the provided rect,
	 *  and its new position is passed into the constructor.
	 */
	public static class Door extends MovingPlatform implements Button.SwitchHandler {
		public double oldX;
		public double oldY;
		public double newX;
		public double newY;
		public double amt;
		public boolean activated;
		public Door(List<Box> world, Rect rect, double newX, double newY) {
			super(world, rect);
			oldX = rect.x;
			oldY = rect.y;
			this.newX = newX;
			this.newY = newY;
		}
		public void move() {
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
	/**
	 * The end of a level.
	 */
	public static class End extends Box {
		public Game game;
		public End(Game game, List<Box> world, double x, double y) {
			super(world, new Rect(x, y, 2, 2), PhysicsState.NONE);
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
			MainWindow window = game.screen.window;
			if (window.screen instanceof GameScreen gamescreen) {
				if (game.player1.rect.colliderect_strict(rect)) {
					if (game.player2.rect.colliderect_strict(rect)) {
						// Level is complete
						game.screen.levelCompleted = true;
						Level level = Levels.levels[game.level];
						level.bestTime = level.bestTime == -1 ? game.timer : Math.min(level.bestTime, game.timer);
						SaveData.save();
						window.screen = new EndingAnimation(window, gamescreen, new OpeningAnimation(window, new MapScreen(window, game.level)));
					}
				}
			}
		}
	}
	/**
	 * An invisible object that spawns another object every `frames` frames.
	 */
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
				if (b.physics == PhysicsState.PHYSICS) b.cancelFromCollision();
			}
		}
	}
	/**
	 * An invisible wind zone.
	 */
	public static class InvisibleWind extends Box {
		public double amtX;
		public double amtY;
		public double padding;
		public InvisibleWind(List<Box> world, Rect rect, double amtX, double amtY) {
			super(world, rect, PhysicsState.NONE);
			this.amtX = amtX;
			this.amtY = amtY;
			this.padding = 0.3;
		}
		public InvisibleWind withPadding(double padding) {
			this.padding = padding;
			return this;
		}
		public void draw(Surface s, Rect drawRect, double brightness) {}
		public void tick() {
			super.tick();
			// Move objects
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
	/**
	 * A visible wind zone.
	 */
	public static class Wind extends InvisibleWind {
		public static int gridSize = 5;
		public double offsetX;
		public double offsetY;
		public Surface bg;
		public boolean updatedBrightness = false;
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
			if (!updatedBrightness) {
				bg = bg.scaleValues(getColor(brightness, 1/4d).getRed() / 255f);
				updatedBrightness = true;
			}
			s.blit(
				bg.crop((int)(offsetX + gridSize), (int)(offsetY + gridSize), (int)(drawRect.w), (int)(drawRect.h))
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
	public static class SecretCoin extends Box {
		public static Surface coinImage = makeCoinImage();
		public Game game;
		public SecretCoin(Game game, List<Box> world, double x, double y) {
			super(world, new Rect(x - 0.25, y - 0.25, 0.5, 0.5), PhysicsState.NONE);
			this.game = game;
		}
		public void draw(Surface s, Rect drawRect, double brightness) {
			s.blit(coinImage, (int)(drawRect.centerX()) - (coinImage.get_width() / 2), (int)(drawRect.centerY()) - (coinImage.get_height() / 2));
		}
		public void tick() {
			super.tick();
			// Check for collision
			Level currentLevel = Levels.levels[game.level];
			if (game.player1.rect.colliderect_strict(rect)) {
				if (game.player2.rect.colliderect_strict(rect)) {
					// Get the coin
					currentLevel.bestCoinTime = currentLevel.bestCoinTime == -1 ? game.timer : Math.min(currentLevel.bestCoinTime, game.timer);
					this.remove();
					SaveData.save();
					// Spawn particles
					new CoinGetParticle(game, world, this.rect.centerX(), this.rect.centerY(), 0.4, 0.1, 0.04).spawn();
					new CoinGetParticle(game, world, this.rect.centerX(), this.rect.centerY(), 0, 0.3, 0.01).spawn();
				}
			}
		}
		public static Surface makeCoinImage() {
			Surface s = new Surface(30, 30, new Color(0, 0, 0, 0));
			s.drawCircle(new Color(0, 0, 0, 25), s.get_width() / 2, s.get_width() / 2, s.get_width() / 2);
			Surface t = Surface.renderText((int)(s.get_width() * 0.5), "C", new Color(0, 0, 0, 25));
			s.blit(t, (s.get_width() / 2) - (t.get_width() / 2), (s.get_height() / 2) - (t.get_height() / 2));
			return s;
		}
		public static class CoinGetParticle extends Box {
			public double rad;
			public double v;
			public double a;
			public double av;
			public CoinGetParticle(Game game, List<Box> world, double x, double y, double rad, double v, double av) {
				super(world, Rect.fromCenter(x, y, 1, 1), PhysicsState.NONE);
				this.rad = rad;
				this.v = v;
				this.a = 1;
				this.av = av;
			}
			public void draw(Surface s, Rect drawRect, double brightness) {
				Rect r = drawRect.withSize(drawRect.size() * rad);
				s.drawCircle(new Color(0, 0, 0, (int)(this.a * 255)), r, 3);
			}
			public void tick() {
				super.tick();
				// Animation
				this.rad += this.v;
				this.v *= 0.9;
				this.a -= this.av;
				if (this.a < 0) this.remove();
			}
		}
	}
}
