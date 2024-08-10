package com.sillypantscoder.windowlib;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.sillypantscoder.windowlib.Surface.DummyImageObserver;

/**
 * A panel that automatically redraws itself.
 */
public class RepaintingPanel extends JPanel {
	private static final long serialVersionUID = 7148504528835036003L;
	protected static JFrame frame;
	public BiFunction<Integer, Integer, BufferedImage> painter;
	public Consumer<String> keyDown;
	public Consumer<String> keyUp;
	public BiConsumer<Integer, Integer> mouseMoved;
	public BiConsumer<Integer, Integer> mouseDown;
	public BiConsumer<Integer, Integer> mouseUp;
	/**
	* Called by the runtime system whenever the panel needs painting.
	*/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			g.drawImage(painter.apply(getWidth(), getHeight()), 0, 0, new DummyImageObserver());
		} catch (Throwable t) {
			t.printStackTrace();
			try {
				// set debugger on next line
				Thread.sleep(Duration.ofSeconds(1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	// public abstract void mouseClicked(MouseEvent e);
	// public abstract void mouseMoved(MouseEvent e);
	// public abstract void keyPressed(KeyEvent e);
	public static void startAnimation() {
		SwingWorker<Object, Object> sw = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() throws Exception {
				while (true) {
					frame.revalidate();
					frame.getContentPane().repaint();
					Thread.sleep(16);
				}
			}
		};
		sw.execute();
	}
	public void run(String title, Surface icon, int width, int height) {
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.setIconImage(icon.img);
		frame.setVisible(true);
		startAnimation();
		// Start mouse listener
		myMouseListener ml = new myMouseListener(this);
		this.addMouseListener(ml);
		mouseMotionListener mml = new mouseMotionListener(this);
		this.addMouseMotionListener(mml);
		// Add keyboard listener
		RepaintingPanel thePanel = this;
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e){ thePanel.keyDown.accept(KeyEvent.getKeyText(e.getKeyCode())); }
			@Override
			public void keyReleased(KeyEvent e) { thePanel.keyUp.accept(KeyEvent.getKeyText(e.getKeyCode())); }
			@Override
			public void keyTyped(KeyEvent e) { }
		});
	}
}
class myMouseListener implements MouseListener {
	protected RepaintingPanel srcPanel;
	public myMouseListener(RepaintingPanel p) {
		srcPanel = p;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) { }

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0) {
		srcPanel.mouseDown.accept(arg0.getX(), arg0.getY());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		srcPanel.mouseUp.accept(arg0.getX(), arg0.getY());
	}

}
class mouseMotionListener implements MouseMotionListener {
	protected RepaintingPanel srcPanel;
	public mouseMotionListener(RepaintingPanel p) {
		srcPanel = p;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) { }

	@Override
	public void mouseMoved(MouseEvent arg0) {
		srcPanel.mouseMoved.accept(arg0.getX(), arg0.getY());
	}
}