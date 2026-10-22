package gamestates;

import entities.PlayerCharacter;
import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.PlayerConstants.IDLE;

public class PlayerSelection extends State implements Statemethods {

    private BufferedImage backgroundImg, backgroundImgPink;
    private int menuX, menuY, menuWidth, menuHeight;
    private MenuButton playButton;
    private int playerIndex = 0;

    private CharacterAnimation[] characterAnimations;


    public PlayerSelection(Game game) {
        super(game);

        loadButtons();
        loadBackground();
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);

        loadCharAnimations();
    }

    private void loadCharAnimations() {
        characterAnimations = new CharacterAnimation[3];
        int i = 0;
        characterAnimations[i++] = new CharacterAnimation(PlayerCharacter.PIRATE);
        characterAnimations[i++] = new CharacterAnimation(PlayerCharacter.ORC);
        characterAnimations[i++] = new CharacterAnimation(PlayerCharacter.SOLDIER);

    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2 - (int) (7 * Game.SCALE);
        menuY = (int) (25 * Game.SCALE);
    }

    private void loadButtons() {
        int buttonX = Game.GAME_WIDTH / 2 - (int) (7 * Game.SCALE);
        playButton = new MenuButton(buttonX, (int) (340 * Game.SCALE), 0, Gamestate.PLAYING);
    }

    @Override
    public void update() {
        playButton.update();
        for (CharacterAnimation ca : characterAnimations)
            ca.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPink, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

        playButton.draw(g);


        //Center
        drawChar(g, playerIndex, menuX + menuWidth / 2, menuY + menuHeight / 2, true);

        //Left
        drawChar(g, playerIndex - 1, menuX, menuY + menuHeight / 2, false);

        //Right
        drawChar(g, playerIndex + 1, menuX + menuWidth, menuY + menuHeight / 2, false);

    }

    private void drawChar(Graphics g, int index, int x, int y, boolean isCenter) {
        int actualIndex = index;
        if (actualIndex < 0)
            actualIndex = characterAnimations.length - 1;
        else if (actualIndex >= characterAnimations.length)
            actualIndex = 0;
        characterAnimations[actualIndex].draw(g, x, y, isCenter);
        drawStats(g, characterAnimations[actualIndex].getPc(), x, y, isCenter);
    }

    private void drawStats(Graphics g, PlayerCharacter pc, int x, int y, boolean isCenter) {
        Graphics2D g2d = (Graphics2D) g;
        Object prevAA = g2d.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int fontSize = isCenter ? (int) (12 * Game.SCALE) : (int) (10 * Game.SCALE);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();

        String hpText = "Máu tối đa: " + pc.getMaxHealth();
        String atkText = "Sức tấn công: " + pc.getAttackDamage();

        int w1 = fm.stringWidth(hpText);
        int w2 = fm.stringWidth(atkText);
        int boxW = Math.max(w1, w2) + (int) (14 * Game.SCALE);
        int lineHeight = fm.getHeight();
        int boxH = lineHeight * 2 + (int) (8 * Game.SCALE);

        int boxX = x - boxW / 2;
        int boxY = isCenter ? y + (int) (110 * Game.SCALE / 2f) : y + (int) (78 * Game.SCALE / 2f);

        // Nền khung thông số
        g2d.setColor(new Color(0, 0, 0, 185));
        g2d.fillRoundRect(boxX, boxY, boxW, boxH, (int) (6 * Game.SCALE), (int) (6 * Game.SCALE));

        // Viền khung
        if (isCenter) {
            g2d.setColor(new Color(255, 215, 0, 220));
            g2d.setStroke(new BasicStroke(2f));
        } else {
            g2d.setColor(new Color(220, 220, 220, 140));
            g2d.setStroke(new BasicStroke(1.5f));
        }
        g2d.drawRoundRect(boxX, boxY, boxW, boxH, (int) (6 * Game.SCALE), (int) (6 * Game.SCALE));

        int textY1 = boxY + (int) (4 * Game.SCALE) + fm.getAscent();
        int textY2 = textY1 + lineHeight;

        // Vẽ thông số máu tối đa (màu đỏ cam / coral nổi bật)
        int textX1 = x - w1 / 2;
        g2d.setColor(Color.BLACK);
        g2d.drawString(hpText, textX1 + 1, textY1 + 1);
        g2d.setColor(new Color(255, 110, 110));
        g2d.drawString(hpText, textX1, textY1);

        // Vẽ thông số sức tấn công (màu vàng kim)
        int textX2 = x - w2 / 2;
        g2d.setColor(Color.BLACK);
        g2d.drawString(atkText, textX2 + 1, textY2 + 1);
        g2d.setColor(new Color(255, 220, 80));
        g2d.drawString(atkText, textX2, textY2);

        if (prevAA != null)
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, prevAA);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (isIn(e, playButton))
            playButton.setMousePressed(true);


    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (isIn(e, playButton)) {
            if (playButton.isMousePressed()) {

                game.getPlaying().setPlayerCharacter(characterAnimations[playerIndex].getPc());
                game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());

                playButton.applyGamestate();
            }

        }

        resetButtons();
    }

    private void resetButtons() {
        playButton.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        playButton.setMouseOver(false);


        if (isIn(e, playButton))
            playButton.setMouseOver(true);


    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
            deltaIndex(-1);
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
            deltaIndex(1);
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    private void deltaIndex(int i) {
        playerIndex += i;
        if (playerIndex < 0)
            playerIndex = characterAnimations.length - 1;
        else if (playerIndex >= characterAnimations.length)
            playerIndex = 0;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public class CharacterAnimation {
        private final PlayerCharacter pc;
        private int aniTick, aniIndex;
        private final BufferedImage[][] animations;
        private int scale;

        public CharacterAnimation(PlayerCharacter pc) {
            this.pc = pc;
            this.scale = (int) (Game.SCALE + 6);
            animations = LoadSave.loadAnimations(pc);
        }

        public void draw(Graphics g, int drawX, int drawY, boolean isCenter) {
            int currentScale = scale;
            if (pc == PlayerCharacter.PIRATE) {
                currentScale = isCenter ? scale : (int)(scale * 0.5f);
            } else {
                currentScale = isCenter ? (int)(scale * 1.5f) : scale;
            }

            int w = pc.spriteW * currentScale;
            int h = pc.spriteH * currentScale;

            g.drawImage(animations[pc.getRowIndex(IDLE)][aniIndex],
                    drawX - w / 2,
                    drawY - h / 2,
                    w,
                    h,
                    null);
        }

        public void update() {
            aniTick++;
            if (aniTick >= ANI_SPEED) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= pc.getSpriteAmount(IDLE)) {
                    aniIndex = 0;

                }
            }
        }

        public PlayerCharacter getPc() {
            return pc;
        }
    }
}
