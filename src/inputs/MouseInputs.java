package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    private MouseEvent translate(MouseEvent e) {
        int newX = (int) ((e.getX() - gamePanel.getxOffset()) / gamePanel.getScaleFactor());
        int newY = (int) ((e.getY() - gamePanel.getyOffset()) / gamePanel.getScaleFactor());
        return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), newX, newY, e.getClickCount(), e.isPopupTrigger(), e.getButton());
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseDragged(MouseEvent e) {
        MouseEvent t = translate(e);
        switch (Gamestate.state) {
            case PLAYING -> gamePanel.getGame().getPlaying().mouseDragged(t);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseDragged(t);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseMoved(MouseEvent e) {
        MouseEvent t = translate(e);
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseMoved(t);
            case PLAYER_SELECTION -> gamePanel.getGame().getPlayerSelection().mouseMoved(t);
            case PLAYING -> gamePanel.getGame().getPlaying().mouseMoved(t);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseMoved(t);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseClicked(MouseEvent e) {
        MouseEvent t = translate(e);
        switch (Gamestate.state) {
            case PLAYING -> gamePanel.getGame().getPlaying().mouseClicked(t);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mousePressed(MouseEvent e) {
        MouseEvent t = translate(e);
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().mousePressed(t);
            case PLAYER_SELECTION -> gamePanel.getGame().getPlayerSelection().mousePressed(t);
            case PLAYING -> gamePanel.getGame().getPlaying().mousePressed(t);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mousePressed(t);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseReleased(MouseEvent e) {
        MouseEvent t = translate(e);
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseReleased(t);
            case PLAYER_SELECTION -> gamePanel.getGame().getPlayerSelection().mouseReleased(t);
            case PLAYING -> gamePanel.getGame().getPlaying().mouseReleased(t);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseReleased(t);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Not In use
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Not In use
    }

}