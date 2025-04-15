import java.awt.*;
import java.awt.image.BufferedImage;

public class GameCharacter {
    private final Point startPoint;
    private Point location, currentDirection;
    private BufferedImage sprite;
    protected final Rectangle spriteBounds;

    public GameCharacter(int startX, int startY) {
        this.startPoint = new Point(startX, startY);
        this.location = new Point(startX, startY);
        this.currentDirection = new Point();
        this.spriteBounds = new Rectangle(16, 16, 16, 16);
    }

    protected void updateBounds() {
        spriteBounds.x += 16;
        if (spriteBounds.x == getSprite().getWidth())
            spriteBounds.x = 0;
        if (currentDirection.y == -1)
            spriteBounds.y = 0;
        else if (currentDirection.x == 1)
            spriteBounds.y = 16;
        else if (currentDirection.y == 1)
            spriteBounds.y = 32;
        else if (currentDirection.x == -1)
            spriteBounds.y = 48;
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

    public boolean isStaying() {
        return currentDirection.equals(new Point());
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

