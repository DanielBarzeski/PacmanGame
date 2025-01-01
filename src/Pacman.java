import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Pacman {
    private final int startX, startY;
    private BufferedImage close, left1, left2, right1, right2, down1, down2, up1, up2;
    private Point location, direction, newDirection;
    private int health;
    private Boolean change;
    private boolean eaten, running;

    public Pacman(int x, int y) {
        startX = x;
        startY = y;
        setLocation(new Point(x, y));
        eaten = true;
        health = 3;
        direction = new Point();
        newDirection = new Point(-1, -1);
        loadImages();
        change = true;
    }

    public void draw(Graphics g) {
        BufferedImage image1;
        BufferedImage image2;
        if (direction.y == -1) {
            image1 = up1;
            image2 = up2;
        } else if (direction.y == 1) {
            image1 = down1;
            image2 = down2;
        } else if (direction.x == -1) {
            image1 = left1;
            image2 = left2;
        } else if (direction.x == 1) {
            image1 = right1;
            image2 = right2;
        } else {
            g.drawImage(close, getLocation().x * Game.CELL_SIZE + 1, getLocation().y * Game.CELL_SIZE + 1, Game.CELL_SIZE - 2, Game.CELL_SIZE - 2, null);
            return;
        }
        if (change) {
            g.drawImage(close, getLocation().x * Game.CELL_SIZE + 1, getLocation().y * Game.CELL_SIZE + 1, Game.CELL_SIZE - 2, Game.CELL_SIZE - 2, null);
            change = false;
        } else {
            g.drawImage(image1, getLocation().x * Game.CELL_SIZE + 1, getLocation().y * Game.CELL_SIZE + 1, Game.CELL_SIZE - 2, Game.CELL_SIZE - 2, null);
            change = null;
        }
        if (change == null) {
            g.drawImage(image2, getLocation().x * Game.CELL_SIZE + 1, getLocation().y * Game.CELL_SIZE + 1, Game.CELL_SIZE - 2, Game.CELL_SIZE - 2, null);
            change = true;
        }
    }

    public void move() {
        if (direction.x != 0 || direction.y != 0)
            running = true;
        Point newLocation = new Point(getLocation().x + direction.x, getLocation().y + direction.y);
        if (!newLocation.equals(newDirection) && canMove(newLocation))
            setLocation(newLocation);
    }

    private boolean canMove(Point point) {
        if (point.x < 0)
            point.x = Game.MATRIX[0].length - 1;
        if (point.y < 0)
            point.y = Game.MATRIX.length - 1;
        if (point.x >= Game.MATRIX[0].length)
            point.x = 0;
        if (point.y >= Game.MATRIX.length)
            point.y = 0;
        for (Point wall : Board.getWALLS()) {
            if (point.equals(wall)) {
                this.newDirection = point;
                return false;
            }
        }
        return true;
    }

    public void goUp() {
        direction.move(0, -1);
    }

    public void goDown() {
        direction.move(0, 1);
    }

    public void goLeft() {
        direction.move(-1, 0);
    }

    public void goRight() {
        direction.move(1, 0);
    }

    public int getHealth() {
        return health;
    }

    public void removeHealth() {
        this.health--;
    }

    public boolean isEaten() {
        return eaten;
    }

    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }

    public void stop() {
        running = false;
        direction = new Point();
    }

    public boolean isRunning() {
        return running;
    }
    private void loadImages(){
        try {
            left1 = ImageIO.read(new File("images/pacman_open_left.png"));
            left2 = ImageIO.read(new File("images/pacman_fullOpen_left.png"));
            right1 = ImageIO.read(new File("images/pacman_open_right.png"));
            right2 = ImageIO.read(new File("images/pacman_fullOpen_right.png"));
            down1 = ImageIO.read(new File("images/pacman_open_down.png"));
            down2 = ImageIO.read(new File("images/pacman_fullOpen_down.png"));
            up1 = ImageIO.read(new File("images/pacman_open_up.png"));
            up2 = ImageIO.read(new File("images/pacman_fullOpen_up.png"));
            close = ImageIO.read(new File("images/pacman_close.png"));
        } catch (IOException ignored) {
        }
    }
    public Point getLocation() {
        return location;
    }

    public void setStartLocation() {
        setLocation(new Point(startX, startY));
    }

    private void setLocation(Point location) {
        this.location = location;
    }
}
