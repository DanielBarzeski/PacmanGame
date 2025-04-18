import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Ghost extends GameCharacter {
    private static final Color[] COLORS = new Color[]{
            Color.orange, Color.green, Color.magenta, Color.cyan,
            Color.pink, Color.green.darker(), Color.magenta.darker(), Color.cyan.darker(),
    };
    private final Color color;
    private Boolean delay;
    private boolean killed;
    private BufferedImage tempSprite;
    private static int SCARED_TIME, INDEX;
    private static boolean SCARED;

    public Ghost(int startX, int startY) {
        super(startX, startY);
        this.color = COLORS[INDEX++ % COLORS.length];
        this.delay = false;
        this.killed = false;
        this.tempSprite = null;
        changeSprite(Picture.GHOST);
    }

    public void changeSprite(BufferedImage sprite) {
        if (this.tempSprite == null || !this.tempSprite.equals(sprite)) {
            if (sprite == Picture.GHOST) setSprite(Picture.colored(sprite, this.color));
            else setSprite(sprite);
            this.tempSprite = sprite;
        }
    }

    public boolean collision(ArrayList<Ghost> with, Point forNewLocation) {
        for (Ghost ghost : with) {
            if (ghost != this && forNewLocation.equals(ghost.getLocation()))
                return true;
        }
        return false;
    }

    public void goTo(Point location) {
        if (location.x > getLocation().x)
            setCurrentDirection(new Point(1, 0));
        else if (location.x < getLocation().x)
            setCurrentDirection(new Point(-1, 0));
        else if (location.y > getLocation().y)
            setCurrentDirection(new Point(0, 1));
        else if (location.y < getLocation().y)
            setCurrentDirection(new Point(0, -1));
    }

    public void draw(Graphics g) {
        updateBounds();
        int delayX = getCurrentDirection().x * Game.CELL_SIZE / 2 * (isDelay().hashCode() / Boolean.hashCode(true));
        int delayY = getCurrentDirection().y * Game.CELL_SIZE / 2 * (isDelay().hashCode() / Boolean.hashCode(true));
        g.drawImage(
                getSprite().getSubimage(spriteBounds.x, spriteBounds.y, spriteBounds.width, spriteBounds.height),
                getLocation().x * Game.CELL_SIZE - delayX,
                getLocation().y * Game.CELL_SIZE - delayY,
                Game.CELL_SIZE, Game.CELL_SIZE,
                null
        );
    }

    public void stay() {
        setCurrentDirection(new Point());
    }

    public void reset() {
        setLocation(new Point(getStartPoint().x, getStartPoint().y));
        stay();
        spriteBounds.y = 16;
    }

    public void kill() {
        reset();
        killed = true;
    }

    public boolean isKilled() {
        return killed;
    }

    public void revive() {
        killed = false;
    }

    public static int getScaredTime() {
        return SCARED_TIME;
    }

    public static void setScaredTimer(int scaredTime) {
        SCARED_TIME = scaredTime;
    }

    public Boolean isDelay() {
        return delay;
    }

    public void setDelay(Boolean delay) {
        this.delay = delay;
    }

    public static boolean isSCARED() {
        return SCARED;
    }

    public static void setSCARED(boolean SCARED) {
        Ghost.SCARED = SCARED;
    }
}
