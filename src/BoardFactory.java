import java.awt.*;

public class BoardFactory extends BoardMap {

    public BoardFactory(byte[][] map) {
        super(map);
    }

    public void drawGame(Graphics g) {
        food.draw(g);
        drawGhosts(g);
        pacman.draw(g);
        border.draw(g);
        if (Game.isFINISHED()) {
            Rectangle r = new Rectangle(Game.getWIDTH() / 2 - 50, Game.getHEIGHT() / 2 - 60, 100, 100);
            if (Game.isWON())
                g.drawImage(Picture.WINNING, r.x, r.y, r.width, r.height, null);
            else
                g.drawImage(Picture.LOSING, r.x, r.y, r.width, r.height, null);
        }
    }

    protected void drawGhosts(Graphics g) {
        for (Ghost ghost : ghosts) {
            if (!Ghost.isSCARED())
                ghost.changeSprite(Picture.GHOST, true);
            else if (pacman.isNextTo(ghost) || pacman.collision(ghost))
                ghost.changeSprite(Picture.GHOST_SCARED_WHITE, false);
            else
                ghost.changeSprite(Picture.GHOST_SCARED_BLUE, false);
            ghost.draw(g);
        }
    }

    public void drawMenu(Graphics g) {
        g.setColor(Color.yellow);
        g.fillRoundRect(320, 5, 165, 30, 10, 10);
        g.setColor(Color.black);
        g.drawString("Score: " + food.getScore(), 323, 20);
        if (Ghost.getScaredTime() >= 0)
            g.drawString("Scared for: " + Ghost.getScaredTime() + "s", 385, 20);
        int x = 505;
        for (int i = 0; i < pacman.getLife(); i++) {
            g.drawImage(Picture.HEART, x + 24 * i, 7, 20, 20, null);
        }
    }
}
