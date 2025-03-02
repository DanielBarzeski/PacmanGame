import java.awt.*;

public class Pacman extends Character {
    private Point newDirection;
    private int life;

    public Pacman(int startX, int startY) {
        super(startX, startY);
        this.newDirection = new Point(startX, startY);
        life = 3;
        setSprite(Picture.PACMAN);
    }

    public void kill() {
        stop();
        life--;
        setLocation(new Point(getStartPoint().x, getStartPoint().y));
        setCurrentDirection(new Point());
        stay();
    }

    public void draw(Graphics g) {
        getSpriteBounds().x += 16;
        if (getSpriteBounds().x == getSprite().getWidth())
            getSpriteBounds().x = 0;
        if (getCurrentDirection().equals(new Point())) {
            getSpriteBounds().y = 0;
            getSpriteBounds().x = 0;
        } else if (getCurrentDirection().y == -1)
            getSpriteBounds().y = 0;
        else if (getCurrentDirection().x == 1 && getCurrentDirection().y == 0)
            getSpriteBounds().y = 16;
        else if (getCurrentDirection().y == 1)
            getSpriteBounds().y = 32;
        else if (getCurrentDirection().x == -1)
            getSpriteBounds().y = 48;
        g.drawImage(getSprite().getSubimage(getSpriteBounds().x, getSpriteBounds().y, getSpriteBounds().width, getSpriteBounds().height),
                getLocation().x * Game.CELL_SIZE, getLocation().y * Game.CELL_SIZE,
                Game.CELL_SIZE + 1, Game.CELL_SIZE + 1, null);

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

    public boolean isNextTo(Ghost ghost) {
        int dx = Math.abs(ghost.getLocation().x - getLocation().x);
        int dy = Math.abs(ghost.getLocation().y - getLocation().y);
        return dx + dy == 1;
    }

    public boolean collision(Ghost ghost) {
        return getLocation().equals(ghost.getLocation());
    }


}

