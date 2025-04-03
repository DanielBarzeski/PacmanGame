import java.awt.*;
import java.util.ArrayList;

public class Border {
    public final ArrayList<Point> walls = new ArrayList<>();

    public boolean collision(Point location) {
        for (Point wall : walls) {
            if (wall.equals(location))
                return true;
        }
        return false;
    }

    public void draw(Graphics g) {
        for (Point wall : walls) {
            g.setColor(Color.cyan.darker().darker());
            g.fillRoundRect(
                    wall.x * Game.CELL_SIZE, wall.y * Game.CELL_SIZE,
                    Game.CELL_SIZE - 1, Game.CELL_SIZE - 1, 5, 5);
            g.setColor(Color.cyan);
            g.drawRoundRect(
                    wall.x * Game.CELL_SIZE, wall.y * Game.CELL_SIZE,
                    Game.CELL_SIZE - 1, Game.CELL_SIZE - 1, 5, 5);
        }
    }
}
