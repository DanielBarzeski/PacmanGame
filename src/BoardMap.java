import java.awt.*;
import java.util.ArrayList;

public class BoardMap {
    protected final byte[][] map;
    protected final Pacman pacman;
    protected final ArrayList<Ghost> ghosts;
    protected final Walls walls;
    protected final Food food;

    public BoardMap(byte[][] map) {
        Ghost.setScaredTimer(-1);
        Ghost.setSCARED(false);
        this.map = map;
        this.walls = new Walls();
        this.ghosts = new ArrayList<>();
        this.food = new Food();
        Point point = new Point();
        scanMap(point);
        this.pacman = new Pacman(point.x, point.y);
    }

    private void scanMap(Point point) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] == '0')      // every 0 represent an apple.
                    food.apples.add(new Point(col, row));
                else if (map[row][col] == '1') // every 1 represent a wall.
                    walls.add(new Point(col, row));
                else if (map[row][col] == '2') // every 2 represent a ghost.
                    ghosts.add(new Ghost(col, row));
                else if (map[row][col] == '4') // 4 represent a pacman.
                    point.move(col, row);
                else if (map[row][col] == '5') // every 5 represent a cherry.
                    food.cherries.add(new Point(col, row));
                // every 3 represent nothing.
            }
        }
    }

    public boolean outOfMap(Point point) {
        return (point.x < 0 || point.y < 0 || point.x >= map[0].length || point.y >= map.length);
    }

    public void adjustToMap(Point location) {
        if (location.x < 0)
            location.x = map[0].length - 1;
        if (location.y < 0)
            location.y = map.length - 1;
        if (location.x >= map[0].length)
            location.x = 0;
        if (location.y >= map.length)
            location.y = 0;
    }
}
