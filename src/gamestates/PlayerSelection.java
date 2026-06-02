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
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            deltaIndex(1);
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            deltaIndex(-1);
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
