import java.awt.*;
import java.awt.image.BufferedImage;

public class Character {
    private boolean run;
    private final Point startPoint;
    private Point location, currentDirection;
    private BufferedImage sprite;
    protected final Rectangle spriteBounds;

    public Character(int startX, int startY) {
        this.startPoint = new Point(startX, startY);
        this.location = new Point(startX, startY);
        this.currentDirection = new Point();
        this.spriteBounds = new Rectangle(16, 16, 16, 16);
        this.run = true;
    }

    public boolean isRunning() {
        return run;
    }

    public void stop() {
        run = false;
    }

    public void run() {
        run = true;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = new Point(location);
    }

    public Point getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Point currentDirection) {
        this.currentDirection = currentDirection;
    }

    protected Point getStartPoint() {
        return startPoint;
    }

    protected BufferedImage getSprite() {
        return sprite;
    }

    protected void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }
}

