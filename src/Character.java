import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Character {
    private final Point startPoint;
    private BufferedImage sprite;
    private final Rectangle spriteBounds;
    private Point location, currentDirection, newDirection;
    private final Color color;
    private String pathname = "";

    public Character(int startX, int startY, String pathname, Color color) {
        this.startPoint = new Point(startX, startY);
        this.location = new Point(startX, startY);
        this.currentDirection = new Point();
        this.newDirection = new Point();
        this.color = color;
        setSprite(pathname, true);
        spriteBounds = new Rectangle(0, 16, 16, 16);
    }


    private BufferedImage replaceColor(BufferedImage image, Color replacement) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb, true);

                if (isSpecificRed(color) && !isTransparent(color)) {
                    result.setRGB(x, y, replacement.getRGB());
                } else {
                    result.setRGB(x, y, rgb);
                }
            }
        }

        return result;
    }

    private static boolean isSpecificRed(Color color) {
        return color.getRed() > 120 && color.getGreen() < 100 && color.getBlue() < 100;
    }

    private static boolean isTransparent(Color color) {
        return color.getAlpha() == 0;
    }

    public void setSprite(String pathname, boolean replace) {
        if (!this.pathname.equals(pathname)) {
            try {
                sprite = ImageIO.read(new File(pathname));
                if (replace) {
                    sprite = replaceColor(sprite, this.color);
                }
            } catch (IOException ignored) {
            }
            this.pathname = pathname;
        }
    }

    public void setToStartLocation() {
        this.newDirection = new Point();
        this.currentDirection = new Point();
        this.location = new Point(startPoint.x, startPoint.y);
    }

    public void draw(Graphics g) {
        spriteBounds.x += 16;
        if (spriteBounds.x == sprite.getWidth())
            spriteBounds.x = 0;
        if (currentDirection.y == -1)
            spriteBounds.y = 0;
        else if ((currentDirection.x == 1 || currentDirection.x == 0) && currentDirection.y == 0)
            spriteBounds.y = 16;
        else if (currentDirection.y == 1)
            spriteBounds.y = 32;
        else if (currentDirection.x == -1)
            spriteBounds.y = 48;
        g.drawImage(sprite.getSubimage(spriteBounds.x, spriteBounds.y, spriteBounds.width, spriteBounds.height),
                location.x * Game.CELL_SIZE, location.y * Game.CELL_SIZE,
                Game.CELL_SIZE, Game.CELL_SIZE, null);
    }

    public void goUp() {
        newDirection = new Point(0, -1);
    }

    public void stay() {
        newDirection = new Point();
    }

    public void goDown() {
        newDirection = new Point(0, 1);
    }

    public void goLeft() {
        newDirection = new Point(-1, 0);
    }

    public void goRight() {
        newDirection = new Point(1, 0);
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

    public Point getNewDirection() {
        return newDirection;
    }
}
