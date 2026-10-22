package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UrmButton;
import utilz.LoadSave;
import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.UI.URMButtons.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.CANNON_WIDTH_DEFAULT;
import static utilz.Constants.ObjectConstants.CANNON_HEIGHT_DEFAULT;

public class GameOptions extends State implements Statemethods {

	private AudioOptions audioOptions;
	private BufferedImage backgroundImg, optionsBackgroundImg;
	private BufferedImage boardImg;
	private MonsterAnimation crabAnim, sharkAnim, pinkstarAnim, cannonAnim;
	private int bgX, bgY, bgW, bgH;
	private int boardW, boardH;
	private int leftBoardX, rightBoardX, boardY;
	private UrmButton menuB;

	public GameOptions(Game game) {
		super(game);
		loadImgs();
		loadButton();
		audioOptions = game.getAudioOptions();
	}

	private void loadButton() {
		int menuX = (int) (387 * Game.SCALE);
		int menuY = (int) (325 * Game.SCALE);

		menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
	}

	private void loadImgs() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
		optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);

		bgW = (int) (optionsBackgroundImg.getWidth() * Game.SCALE);
		bgH = (int) (optionsBackgroundImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (33 * Game.SCALE);

		BufferedImage completedImg = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
		boardImg = completedImg.getSubimage(0, 92, 224, 112);

		loadMonsterAnimations();

		float boardScale = 2.15f;
		boardW = (int) (boardImg.getWidth() * boardScale);
		boardH = (int) (boardImg.getHeight() * boardScale);

		leftBoardX = (bgX - boardW) / 2;
		rightBoardX = bgX + bgW + (Game.GAME_WIDTH - (bgX + bgW) - boardW) / 2;
		boardY = bgY + (bgH - boardH) / 2;
	}

	private void loadMonsterAnimations() {
		// Quái Cua (Crabby): Row 0 = IDLE (9 frames), Row 1 = RUNNING (6 frames)
		BufferedImage crabAtlas = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
		BufferedImage[][] crabFrames = new BufferedImage[2][];
		crabFrames[IDLE] = new BufferedImage[GetSpriteAmount(CRABBY, IDLE)];
		for (int i = 0; i < crabFrames[IDLE].length; i++)
			crabFrames[IDLE][i] = crabAtlas.getSubimage(i * CRABBY_WIDTH_DEFAULT, IDLE * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
		crabFrames[RUNNING] = new BufferedImage[GetSpriteAmount(CRABBY, RUNNING)];
		for (int i = 0; i < crabFrames[RUNNING].length; i++)
			crabFrames[RUNNING][i] = crabAtlas.getSubimage(i * CRABBY_WIDTH_DEFAULT, RUNNING * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
		crabAnim = new MonsterAnimation(crabFrames, 2.1f, IDLE);

		// Quái Cá Mập (Shark): Row 0 = IDLE (8 frames), Row 1 = RUNNING (6 frames)
		BufferedImage sharkAtlas = LoadSave.GetSpriteAtlas(LoadSave.SHARK_ATLAS);
		BufferedImage[][] sharkFrames = new BufferedImage[2][];
		sharkFrames[IDLE] = new BufferedImage[GetSpriteAmount(SHARK, IDLE)];
		for (int i = 0; i < sharkFrames[IDLE].length; i++)
			sharkFrames[IDLE][i] = sharkAtlas.getSubimage(i * SHARK_WIDTH_DEFAULT, IDLE * SHARK_HEIGHT_DEFAULT, SHARK_WIDTH_DEFAULT, SHARK_HEIGHT_DEFAULT);
		sharkFrames[RUNNING] = new BufferedImage[GetSpriteAmount(SHARK, RUNNING)];
		for (int i = 0; i < sharkFrames[RUNNING].length; i++)
			sharkFrames[RUNNING][i] = sharkAtlas.getSubimage(i * SHARK_WIDTH_DEFAULT, RUNNING * SHARK_HEIGHT_DEFAULT, SHARK_WIDTH_DEFAULT, SHARK_HEIGHT_DEFAULT);
		sharkAnim = new MonsterAnimation(sharkFrames, 2.4f, IDLE);

		// Quái Sao Biển (Pinkstar): Row 0 = IDLE (8 frames), Row 1 = RUNNING (6 frames)
		BufferedImage pinkAtlas = LoadSave.GetSpriteAtlas(LoadSave.PINKSTAR_ATLAS);
		BufferedImage[][] pinkFrames = new BufferedImage[2][];
		pinkFrames[IDLE] = new BufferedImage[GetSpriteAmount(PINKSTAR, IDLE)];
		for (int i = 0; i < pinkFrames[IDLE].length; i++)
			pinkFrames[IDLE][i] = pinkAtlas.getSubimage(i * PINKSTAR_WIDTH_DEFAULT, IDLE * PINKSTAR_HEIGHT_DEFAULT, PINKSTAR_WIDTH_DEFAULT, PINKSTAR_HEIGHT_DEFAULT);
		pinkFrames[RUNNING] = new BufferedImage[GetSpriteAmount(PINKSTAR, RUNNING)];
		for (int i = 0; i < pinkFrames[RUNNING].length; i++)
			pinkFrames[RUNNING][i] = pinkAtlas.getSubimage(i * PINKSTAR_WIDTH_DEFAULT, RUNNING * PINKSTAR_HEIGHT_DEFAULT, PINKSTAR_WIDTH_DEFAULT, PINKSTAR_HEIGHT_DEFAULT);
		pinkstarAnim = new MonsterAnimation(pinkFrames, 2.4f, IDLE);

		// Pháo (Cannon): 7 frames
		BufferedImage cannonAtlas = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
		BufferedImage[][] cannonFrames = new BufferedImage[1][7];
		for (int i = 0; i < 7; i++)
			cannonFrames[0][i] = cannonAtlas.getSubimage(i * CANNON_WIDTH_DEFAULT, 0, CANNON_WIDTH_DEFAULT, CANNON_HEIGHT_DEFAULT);
		cannonAnim = new MonsterAnimation(cannonFrames, 2.3f, 0);
	}

	@Override
	public void update() {
		menuB.update();
		audioOptions.update();
		crabAnim.update();
		sharkAnim.update();
		pinkstarAnim.update();
		cannonAnim.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		drawMonsterBoards(g);

		g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);

		menuB.draw(g);
		audioOptions.draw(g);
	}

	private void drawMonsterBoards(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// Bảng bên trái: Quái Cua & Quái Cá Mập
		g2d.drawImage(boardImg, leftBoardX, boardY, boardW, boardH, null);
		drawMonsterCard(g2d, leftBoardX + 16, boardY, boardW / 2 - 16, boardH,
				crabAnim.getCurrentFrame(), crabAnim.getScale(), "Quái Cua",
				"Máu tối đa: " + GetMaxHealth(CRABBY),
				"Sức tấn công: " + GetEnemyDmg(CRABBY));
		drawMonsterCard(g2d, leftBoardX + boardW / 2, boardY, boardW / 2 - 16, boardH,
				sharkAnim.getCurrentFrame(), sharkAnim.getScale(), "Quái Cá Mập",
				"Máu tối đa: " + GetMaxHealth(SHARK),
				"Sức tấn công: " + GetEnemyDmg(SHARK));

		// Bảng bên phải: Quái Sao Biển & Pháo
		g2d.drawImage(boardImg, rightBoardX, boardY, boardW, boardH, null);
		drawMonsterCard(g2d, rightBoardX + 16, boardY, boardW / 2 - 16, boardH,
				pinkstarAnim.getCurrentFrame(), pinkstarAnim.getScale(), "Quái Sao Biển",
				"Máu tối đa: " + GetMaxHealth(PINKSTAR),
				"Sức tấn công: " + GetEnemyDmg(PINKSTAR));
		drawMonsterCard(g2d, rightBoardX + boardW / 2, boardY, boardW / 2 - 16, boardH,
				cannonAnim.getCurrentFrame(), cannonAnim.getScale(), "Pháo",
				null,
				"Sức tấn công: 25");
	}

	private void drawMonsterCard(Graphics2D g, int x, int y, int w, int h,
								 BufferedImage monsterImg, float mScale,
								 String name, String hp, String atk) {
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		int mW = (int) (monsterImg.getWidth() * mScale);
		int mH = (int) (monsterImg.getHeight() * mScale);

		Font nameFont = new Font("Arial", Font.BOLD, 16);
		Font statsFont = new Font("Arial", Font.BOLD, 14);

		g.setFont(nameFont);
		FontMetrics fmName = g.getFontMetrics();
		int nameH = fmName.getHeight();

		g.setFont(statsFont);
		FontMetrics fmStats = g.getFontMetrics();
		int statsLineH = fmStats.getHeight();

		int boxPadY = 6;
		int boxPadX = 14;
		// Chiều cao khung thông số chuẩn hóa đồng đều
		int boxH = 2 * statsLineH + boxPadY * 2;

		int maxTextW = fmStats.stringWidth(atk);
		if (hp != null) {
			maxTextW = Math.max(maxTextW, fmStats.stringWidth(hp));
		}
		int boxW = maxTextW + boxPadX * 2;

		int gap1 = 7;
		int gap2 = 7;
		int totalContentH = mH + gap1 + nameH + gap2 + boxH;

		// Căn chính giữa theo chiều dọc của bảng gỗ
		int startY = y + (h - totalContentH) / 2 + 6;

		// 1. Ảnh minh họa quái
		int mX = x + (w - mW) / 2;
		int mY = startY;
		g.drawImage(monsterImg, mX, mY, mW, mH, null);

		// 2. Tên quái
		int nameY = mY + mH + gap1 + fmName.getAscent();
		int nameX = x + (w - fmName.stringWidth(name)) / 2;
		g.setFont(nameFont);
		g.setColor(Color.BLACK);
		g.drawString(name, nameX + 1, nameY + 1);
		g.setColor(new Color(255, 235, 170));
		g.drawString(name, nameX, nameY);

		// 3. Khung thông số bo góc viền vàng như ảnh 3
		int boxX = x + (w - boxW) / 2;
		int boxY = mY + mH + gap1 + nameH + gap2;

		g.setColor(new Color(0, 0, 0, 195));
		g.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);

		g.setColor(new Color(255, 215, 0, 230));
		g.setStroke(new java.awt.BasicStroke(1.8f));
		g.drawRoundRect(boxX, boxY, boxW, boxH, 10, 10);

		// Vẽ chữ trong khung thông số
		g.setFont(statsFont);
		if (hp != null) {
			int t1Y = boxY + boxPadY + fmStats.getAscent();
			int t1X = x + (w - fmStats.stringWidth(hp)) / 2;
			g.setColor(Color.BLACK);
			g.drawString(hp, t1X + 1, t1Y + 1);
			g.setColor(new Color(255, 110, 110)); // Máu tối đa (màu đỏ cam / coral)
			g.drawString(hp, t1X, t1Y);

			int t2Y = t1Y + statsLineH;
			int t2X = x + (w - fmStats.stringWidth(atk)) / 2;
			g.setColor(Color.BLACK);
			g.drawString(atk, t2X + 1, t2Y + 1);
			g.setColor(new Color(255, 220, 80)); // Sức tấn công (màu vàng kim)
			g.drawString(atk, t2X, t2Y);
		} else {
			// Đối với pháo (1 dòng), căn giữa theo chiều dọc trong khung
			int tY = boxY + (boxH - statsLineH) / 2 + fmStats.getAscent();
			int tX = x + (w - fmStats.stringWidth(atk)) / 2;
			g.setColor(Color.BLACK);
			g.drawString(atk, tX + 1, tY + 1);
			g.setColor(new Color(255, 220, 80));
			g.drawString(atk, tX, tY);
		}
	}

	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuB)) {
			menuB.setMousePressed(true);
		} else
			audioOptions.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed())
				Gamestate.state = Gamestate.MENU;
		} else {
			audioOptions.mouseReleased(e);
			checkMonsterClick(e);
		}
		menuB.resetBools();
	}

	private void checkMonsterClick(MouseEvent e) {
		int cardW = boardW / 2 - 16;
		int card1X = leftBoardX + 16;
		int card2X = leftBoardX + boardW / 2;
		int card3X = rightBoardX + 16;
		int card4X = rightBoardX + boardW / 2;

		if (e.getY() >= boardY && e.getY() <= boardY + boardH) {
			if (e.getX() >= card1X && e.getX() <= card1X + cardW)
				crabAnim.toggleState();
			else if (e.getX() >= card2X && e.getX() <= card2X + cardW)
				sharkAnim.toggleState();
			else if (e.getX() >= card3X && e.getX() <= card3X + cardW)
				pinkstarAnim.toggleState();
			else if (e.getX() >= card4X && e.getX() <= card4X + cardW)
				cannonAnim.resetAnimation();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);

		if (isIn(e, menuB))
			menuB.setMouseOver(true);
		else
			audioOptions.mouseMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			Gamestate.state = Gamestate.MENU;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public static class MonsterAnimation {
		private BufferedImage[][] animations;
		private int state;
		private int aniTick, aniIndex;
		private float scale;

		public MonsterAnimation(BufferedImage[][] animations, float scale, int defaultState) {
			this.animations = animations;
			this.scale = scale;
			this.state = defaultState;
		}

		public void update() {
			aniTick++;
			if (aniTick >= ANI_SPEED) {
				aniTick = 0;
				aniIndex++;
				if (aniIndex >= animations[state].length) {
					aniIndex = 0;
				}
			}
		}

		public BufferedImage getCurrentFrame() {
			return animations[state][aniIndex];
		}

		public float getScale() {
			return scale;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			if (this.state != state && state < animations.length && animations[state] != null) {
				this.state = state;
				this.aniIndex = 0;
				this.aniTick = 0;
			}
		}

		public void toggleState() {
			if (animations.length > 1) {
				setState(state == IDLE ? RUNNING : IDLE);
			}
		}

		public void resetAnimation() {
			this.aniIndex = 0;
			this.aniTick = 0;
		}
	}

}
