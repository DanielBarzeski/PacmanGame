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
        stop();
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
        updateBounds();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(getSprite().getSubimage(spriteBounds.x, spriteBounds.y, spriteBounds.width, spriteBounds.height),
                getLocation().x * Game.CELL_SIZE, getLocation().y * Game.CELL_SIZE,
                Game.CELL_SIZE + 1, Game.CELL_SIZE + 1, null);

    }

    private void updateBounds() {
        spriteBounds.x += 16;
        if (spriteBounds.x == getSprite().getWidth())
            spriteBounds.x = 0;
        if (getCurrentDirection().equals(new Point())) {
            spriteBounds.y = 0;
            spriteBounds.x = 0;
        } else if (getCurrentDirection().y == -1)
            spriteBounds.y = 0;
        else if (getCurrentDirection().x == 1 && getCurrentDirection().y == 0)
            spriteBounds.y = 16;
        else if (getCurrentDirection().y == 1)
            spriteBounds.y = 32;
        else if (getCurrentDirection().x == -1)
            spriteBounds.y = 48;
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

