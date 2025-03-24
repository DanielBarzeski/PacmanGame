import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Ghost extends Character {
    private BufferedImage sprite;
    private final Color color;
    private static int SCARED_TIME;
    private static boolean SCARED;

    public Ghost(int startX, int startY) {
        super(startX, startY);
        this.color = Picture.randomColor();
        this.sprite = null;
        SCARED_TIME = -1;
        SCARED = false;
        changeSprite(Picture.GHOST, true);
    }

    public static int getScaredTime() {
        return SCARED_TIME;
    }

    public static void setScaredTime(int scaredTime) {
        SCARED_TIME = scaredTime;
    }

    public static boolean isSCARED() {
        return SCARED;
    }

    public static void setSCARED(boolean SCARED) {
        Ghost.SCARED = SCARED;
    }

    public boolean collision(Character other) {
        return getLocation().equals(other.getLocation());
    }

    public boolean collision(Point forNewLocation, ArrayList<Ghost> with) {
        for (Ghost ghost : with) {
            if (ghost != this && forNewLocation.equals(ghost.getLocation()))
                return true;
        }
        return false;
    }

    public void reset() {
        setLocation(new Point(getStartPoint().x, getStartPoint().y));
        stay();
    }

    public void kill() {
        setLocation(new Point(getStartPoint().x, getStartPoint().y));
        stop();
    }

    public void changeSprite(BufferedImage sprite, boolean replace) {
        if (this.sprite == null || !this.sprite.equals(sprite)) {
            setSprite(sprite);
            if (getSprite() != null && replace)
                setSprite(Picture.replaceColor(getSprite(), this.color));
            this.sprite = sprite;
        }
    }

    public void draw(Graphics g) {
        spriteBounds.x += 16;
        if (spriteBounds.x == getSprite().getWidth())
            spriteBounds.x = 0;
        if (getCurrentDirection().y == -1)
            spriteBounds.y = 0;
        else if ((getCurrentDirection().x == 1 || getCurrentDirection().x == 0) && getCurrentDirection().y == 0)
            spriteBounds.y = 16;
        else if (getCurrentDirection().y == 1)
            spriteBounds.y = 32;
        else if (getCurrentDirection().x == -1)
            spriteBounds.y = 48;
        g.drawImage(getSprite().getSubimage(spriteBounds.x, spriteBounds.y, spriteBounds.width, spriteBounds.height),
                getLocation().x * Game.CELL_SIZE, getLocation().y * Game.CELL_SIZE,
                Game.CELL_SIZE, Game.CELL_SIZE, null);
    }

    public void stay() {
        setCurrentDirection(new Point());
    }

    public void goTo(Point location) {
        if (isRunning()) {
            if (location.x > getLocation().x)
                setCurrentDirection(new Point(1, 0));
            else if (location.x < getLocation().x)
                setCurrentDirection(new Point(-1, 0));
            else if (location.y > getLocation().y)
                setCurrentDirection(new Point(0, 1));
            else if (location.y < getLocation().y)
                setCurrentDirection(new Point(0, -1));
        }
    }
}
