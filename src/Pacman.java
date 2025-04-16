import java.awt.*;

public class Pacman extends GameCharacter {
    private Point newDirection;
    private int life;

    public Pacman(int startX, int startY) {
        super(startX, startY);
        stay();
        life = 3;
        setSprite(Picture.PACMAN);
    }

    public void kill() {
        life--;
        setLocation(new Point(getStartPoint().x, getStartPoint().y));
        setCurrentDirection(new Point());
        stay();
    }

    public boolean isNextTo(Ghost ghost) {
        int dx = Math.abs(ghost.getLocation().x - getLocation().x);
        int dy = Math.abs(ghost.getLocation().y - getLocation().y);
        return dx + dy == 1;
    }

    public boolean collision(Ghost ghost) {
        return getLocation().equals(ghost.getLocation());
    }

    public void draw(Graphics g) {
        update();
        g.drawImage(getSprite().getSubimage(spriteBounds.x, spriteBounds.y, spriteBounds.width, spriteBounds.height),
                getLocation().x * Game.CELL_SIZE, getLocation().y * Game.CELL_SIZE,
                Game.CELL_SIZE + Game.CELL_SIZE / 16,
                Game.CELL_SIZE + Game.CELL_SIZE / 16, null
        );
    }

    private void update() {
        if (isStaying()) spriteBounds.setLocation(0, 0);
        else updateBounds();
    }

    public void stay() {
        newDirection = new Point();
    }

    public void goUp() {
        newDirection = new Point(0, -1);
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

    public Point getNewDirection() {
        return newDirection;
    }

    public int getLife() {
        return life;
    }
}

