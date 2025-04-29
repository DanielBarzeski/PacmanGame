import java.awt.*;

public class BoardFactory extends BoardMap {

    public BoardFactory(byte[][] map) {
        super(map);
    }

    public void drawGame(Graphics g) {
        food.draw(g);
        if (Ghost.isSCARED()) {
            drawGhosts(g);
            pacman.draw(g);
        } else {
            pacman.draw(g);
            drawGhosts(g);
        }
        walls.draw(g);
        if (Game.isFINISHED()) {
            g.drawImage(Game.isWON() ? Picture.WINNING : Picture.LOSING,
                    GameDisplay.getWIDTH() / 2 - 50, GameDisplay.getHEIGHT() / 2 - 60,
                    100, 100, null
            );
        }
    }

    private void drawGhosts(Graphics g) {
        for (Ghost ghost : ghosts) {
            if (!Ghost.isSCARED())
                ghost.changeSprite(Picture.GHOST);
            else if (pacman.isNextTo(ghost) || pacman.collision(ghost))
                ghost.changeSprite(Picture.GHOST_SCARED_WHITE);
            else
                ghost.changeSprite(Picture.GHOST_SCARED_BLUE);
            ghost.draw(g);
        }
    }

    public void drawMenu(Graphics g) {
        g.setColor(Color.yellow);
        g.fillRoundRect(400, 5, 165, 30, 10, 10);
        g.setColor(Color.magenta);
        g.fillRoundRect(300, 5, 80, 30, 10, 10);
        g.setColor(Color.black);
        g.drawString("Level " + Game.getLEVEL(), 320, 25);
        g.drawString("Score: " + food.getScore(), 415, 25);
        if (Ghost.getScaredTime() >= 0) {
            g.drawImage(Picture.GHOST_SCARED_BLUE.getSubimage(0, 0, 16, 16),
                    492, 10, 20, 20, null);
            g.drawString(" -> " + Ghost.getScaredTime() + "s", 512, 25);
        }
        for (int i = 0; i < pacman.getLife(); i++) {
            g.drawImage(Picture.HEART, 585 + 24 * i, 7, 20, 20, null);
        }
    }
}
