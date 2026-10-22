package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private Game game;

	public GamePanel(Game game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;
		setPanelSize();
		setFocusable(true);
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
	}

	private boolean cursorVisible = true;

	public void setCursorVisibility(boolean visible) {
		if (this.cursorVisible == visible) return;
		this.cursorVisible = visible;

		if (visible) {
			setCursor(java.awt.Cursor.getDefaultCursor());
		} else {
			java.awt.image.BufferedImage cursorImg = new java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_ARGB);
			java.awt.Cursor blankCursor = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new java.awt.Point(0, 0), "blank cursor");
			setCursor(blankCursor);
		}
	}

	public void updateGame() {

	}

	private int xOffset;
	private int yOffset;
	private float scaleFactor = 1.0f;

	public int getxOffset() {
		return xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public float getScaleFactor() {
		return scaleFactor;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
		g2.setColor(java.awt.Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());

		float scaleX = (float) getWidth() / GAME_WIDTH;
		float scaleY = (float) getHeight() / GAME_HEIGHT;
		scaleFactor = Math.min(scaleX, scaleY);

		int scaledWidth = (int) (GAME_WIDTH * scaleFactor);
		int scaledHeight = (int) (GAME_HEIGHT * scaleFactor);

		xOffset = (getWidth() - scaledWidth) / 2;
		yOffset = (getHeight() - scaledHeight) / 2;

		g2.translate(xOffset, yOffset);
		g2.scale(scaleFactor, scaleFactor);

		game.render(g);
	}

	public Game getGame() {
		return game;
	}

}