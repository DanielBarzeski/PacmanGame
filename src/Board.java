import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.*;
import java.awt.Color;

public class Board {
    private final char[][] map;
    private final ArrayList<Point> walls, points, specialPoints;
    private final ArrayList<Character> ghosts;
    private Character pacman;
    private boolean pacmanIsAfraid, slow;
    private int score, time, life;
    private Thread timer;
    private BufferedImage heart, gameOver, cherry, apple;

    public Board(char[][] map) {
        this.map = map;
        walls = new ArrayList<>();
        points = new ArrayList<>();
        specialPoints = new ArrayList<>();
        ghosts = new ArrayList<>();
        pacmanIsAfraid = true;
        slow = false;
        score = 0;
        life = 3;
        time = -1;
        timer = null;
        scanMap();
    }

    public Character getPacman() {
        return pacman;
    }

    private Thread getTimer() {
        return new Thread(() -> {
            while (time >= 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                if (!Game.isPAUSED())
                    time--;
            }
            pacmanIsAfraid = true;
            timer = null;
        });
    }

    private BufferedImage getBufImage(String pathname) {
        try {
            return ImageIO.read(new File(pathname));
        } catch (IOException e) {
            return null;
        }
    }

    private void scanMap() {
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.ORANGE);
        colors.add(Color.GREEN);
        colors.add(Color.MAGENTA);
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] == '0') // every zero represent a food point.
                    points.add(new Point(col, row));
                else if (map[row][col] == '1') // every one represent a wall.
                    walls.add(new Point(col, row));
                else if (map[row][col] == '2') { // every two represent a ghost.
                    ghosts.add(new Character(col, row, "file/ghost.png", colors.getLast()));
                    colors.removeLast();
                    if (colors.isEmpty())
                        colors.add(new Color(100 + (int) (Math.random() * 156), 100 + (int) (Math.random() * 156), 100 + (int) (Math.random() * 156)));
                } else if (map[row][col] == '4') // four represent a pacman.
                    pacman = new Character(col, row, "file/pacman.png", Color.yellow);
                else if (map[row][col] == '5') // five represent a special food point.
                    specialPoints.add(new Point(col, row));
                // every three represent nothing.
            }
        }
        if (pacman == null) {
            System.out.println("Game need pacman!");
            System.exit(0);
        }
        heart = getBufImage("file/heart.png");
        gameOver = getBufImage("file/gameOver.png");
        apple = getBufImage("file/apple.png");
        cherry = getBufImage("file/cherry.png");
    }

    public void drawScore(Graphics g) {
        g.setColor(Color.white);
        g.drawString("Score: " + score, 170, 20);
        if (time >= 0)
            g.drawString("scared for: " + time + "s", 240, 20);
        int x = 340;
        for (int i = 0; i < life; i++) {
            g.drawImage(heart, x + 24 * i, 7, 20, 20, null);
        }

    }

    public void draw(Graphics g) {
        drawFood(g);
        if (pacmanIsAfraid)
            pacman.draw(g);
        drawGhosts(g);
        if (!pacmanIsAfraid)
            pacman.draw(g);
        drawWalls(g);
        if (Game.isFINISHED()) {
            g.drawImage(gameOver, Game.WIDTH / 2 - 50, Game.HEIGHT / 2 - 60, 100, 100, null);
        }
    }

    private void drawGhosts(Graphics g) {
        for (Character ghost : ghosts) {
            ghost.draw(g);
        }
    }

    private void drawFood(Graphics g) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (pacman.getLocation().equals(point)) {
                points.remove(point);
                score += 10;
                SoundManager.playMunchSound();
                i--;
            }
            g.drawImage(apple, point.x * Game.CELL_SIZE + 2,
                    point.y * Game.CELL_SIZE + 2,
                    Game.CELL_SIZE - 3, Game.CELL_SIZE - 3, null
            );
        }
        for (int i = 0; i < specialPoints.size(); i++) {
            Point specialPoint = specialPoints.get(i);
            if (pacman.getLocation().equals(specialPoint)) {
                time += 11;
                if (timer == null) {
                    timer = getTimer();
                    timer.start();
                }
                pacmanIsAfraid = false;
                specialPoints.remove(specialPoint);
                score += 30;
                SoundManager.playEatingFruitSound();
                i--;
            }
            g.drawImage(cherry, specialPoint.x * Game.CELL_SIZE + 1,
                    specialPoint.y * Game.CELL_SIZE + 1,
                    Game.CELL_SIZE - 2, Game.CELL_SIZE - 2, null
            );
        }
        if (specialPoints.isEmpty() && points.isEmpty()) {
            points.add(new Point(-1, -1));
            gameOver = getBufImage("file/winning.png");
            time = -1;
            Game.END();
        }
    }

    private void drawWalls(Graphics g) {
        int change = 0;
        for (Point wall : walls) {
            change++;
            if (change % 2 == 0)
                g.setColor(Color.cyan);
            else
                g.setColor(Color.green);
            g.drawRoundRect(wall.x * Game.CELL_SIZE,
                    wall.y * Game.CELL_SIZE,
                    Game.CELL_SIZE - 1, Game.CELL_SIZE - 1, 5, 5);
        }
    }

    public void update() {
        for (int i = 0; i < ghosts.size(); i++) {
            Character ghost = ghosts.get(i);
            if (ghost.getLocation().equals(pacman.getLocation())) {
                if (pacmanIsAfraid) {
                    SoundManager.playDeathSound();
                    Game.PAUSE();
                    life--;
                    for (Character GHOST : ghosts) {
                        GHOST.setToStartLocation();
                        GHOST.stay();
                    }
                    pacman.setToStartLocation();
                    pacman.stay();
                } else {
                    ghost.setToStartLocation();
                    score += 20;
                    SoundManager.playEatingGhostSound();
                }
            }
        }
        if (life == 0) {
            life--;
            time = -1;
            Game.END();
            return;
        }
        if (!Game.isFINISHED() && !Game.isPAUSED()) {
            movePacman();
            if (!slow) {
                for (Character ghost : ghosts) {
                    moveGhost(ghost);
                }
            }
            slow = !slow;
        }
    }

    private void movePacman() {
        Point newPacLocation = new Point(
                pacman.getLocation().x + pacman.getNewDirection().x,
                pacman.getLocation().y + pacman.getNewDirection().y
        );
        Point curPacLocation = new Point(
                pacman.getLocation().x + pacman.getCurrentDirection().x,
                pacman.getLocation().y + pacman.getCurrentDirection().y
        );
        adjustBorders(newPacLocation);
        adjustBorders(curPacLocation);
        for (Point wall : walls) {
            if (wall.equals(newPacLocation)) {
                newPacLocation = null;
            }
            if (wall.equals(curPacLocation)) {
                curPacLocation = null;
            }
        }
        if (newPacLocation != null) {
            pacman.setCurrentDirection(pacman.getNewDirection());
            curPacLocation = newPacLocation;
        }
        if (curPacLocation != null) {
            pacman.setLocation(curPacLocation);
        }
    }

    private void adjustBorders(Point location) {
        if (location.x < 0)
            location.x = map[0].length - 1;
        if (location.y < 0)
            location.y = map.length - 1;
        if (location.x >= map[0].length)
            location.x = 0;
        if (location.y >= map.length)
            location.y = 0;
    }

    private void moveGhost(Character ghost) {
        go(ghost);
        Point newGhostLocation = new Point(
                ghost.getLocation().x + ghost.getNewDirection().x,
                ghost.getLocation().y + ghost.getNewDirection().y
        );
        ghost.setCurrentDirection(ghost.getNewDirection());
        ghost.setLocation(newGhostLocation);
    }

    private void go(Character ghost) {
        Point next;
        if (pacmanIsAfraid) {
            ghost.setSprite("file/ghost.png", true);
            if (isNextToPacman(ghost)) {
                goToPacman(pacman.getLocation(), ghost);
                return;
            }
            next = findShortestPath(ghost);
        } else {
            if (isNextToPacman(ghost)) {
                ghost.setSprite("file/ghost_scared_white.png", false);
                ghost.stay();
                return;
            }
            ghost.setSprite("file/ghost_scared_blue.png", false);
            next = findFarthestMove(ghost);
        }
        if (next == null) {
            ArrayList<Point> possibleMoves = getNeighbors(ghost.getLocation());
            Collections.shuffle(possibleMoves);
            for (Point move : possibleMoves) {
                if (canMove(move, ghost)) {
                    next = move;
                    break;
                }
            }
        }
        if (next != null)
            goToPacman(next, ghost);
        else
            ghost.stay();
    }

    private void goToPacman(Point location, Character ghost) {
        if (location.x > ghost.getLocation().x)
            ghost.goRight();
        else if (location.x < ghost.getLocation().x)
            ghost.goLeft();
        else if (location.y > ghost.getLocation().y)
            ghost.goDown();
        else if (location.y < ghost.getLocation().y)
            ghost.goUp();
    }

    private Point findFarthestMove(Character ghost) {
        ArrayList<Point> neighbors = getNeighbors(ghost.getLocation());
        Point farthestPoint = null;
        double maxDistance = -1;
        for (Point neighbor : neighbors) {
            if (canMove(neighbor, ghost)) {
                double distance = neighbor.distance(pacman.getLocation());
                if (distance > maxDistance) {
                    maxDistance = distance;
                    farthestPoint = neighbor;
                }
            }
        }
        return farthestPoint;
    }

    private Point findShortestPath(Character ghost) {
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        Set<Point> visited = new HashSet<>();
        queue.add(ghost.getLocation());
        visited.add(ghost.getLocation());
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(pacman.getLocation()))
                break;
            for (Point neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor) && canMove(neighbor, ghost)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }
        Point current = pacman.getLocation();
        while (cameFrom.containsKey(current) && !cameFrom.get(current).equals(ghost.getLocation()))
            current = cameFrom.get(current);
        if (current.equals(ghost.getLocation()) || current.equals(pacman.getLocation()))
            return null;
        return current;
    }

    private ArrayList<Point> getNeighbors(Point point) {
        ArrayList<Point> neighbors = new ArrayList<>();
        neighbors.add(new Point(point.x + 1, point.y));
        neighbors.add(new Point(point.x - 1, point.y));
        neighbors.add(new Point(point.x, point.y + 1));
        neighbors.add(new Point(point.x, point.y - 1));
        return neighbors;
    }

    private boolean canMove(Point newLocation, Character ghost) {
        if (newLocation.x < 0 || newLocation.y < 0 || newLocation.x >= map[0].length || newLocation.y >= map.length)
            return false;
        for (Point wall : walls) {
            if (wall.equals(newLocation))
                return false;
        }
        for (Character GHOST : ghosts) {
            if (GHOST != ghost && GHOST.getLocation().equals(newLocation))
                return false;
        }
        return true;
    }

    public boolean isNextToPacman(Character ghost) {
        int dx = Math.abs(ghost.getLocation().x - pacman.getLocation().x);
        int dy = Math.abs(ghost.getLocation().y - pacman.getLocation().y);
        return dx + dy == 1;
    }

}
