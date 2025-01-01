import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.Color;

public class Ghost {
    private Point location;
    private final int startX, startY;
    private BufferedImage image1, image2, scaredImage, left1, left2, right1, right2, down1, down2, up1, up2;
    private boolean left, right, up, down, change;
    private static boolean FRIGHTENED = false;

    public Ghost(int x, int y, Color color) {
        startX = x;
        startY = y;
        setLocation(new Point(x, y));
        loadImages(color);
    }

    public void draw(Graphics g) {
        if (FRIGHTENED)
            g.drawImage(scaredImage, getLocation().x * Game.CELL_SIZE + 1, getLocation().y * Game.CELL_SIZE + 1, Game.CELL_SIZE - 2, Game.CELL_SIZE - 2, null);
        else {
            checkImages();
            if (change)
                g.drawImage(image1, getLocation().x * Game.CELL_SIZE + 1, getLocation().y * Game.CELL_SIZE + 1, Game.CELL_SIZE - 2, Game.CELL_SIZE - 2, null);
            else
                g.drawImage(image2, getLocation().x * Game.CELL_SIZE + 1, getLocation().y * Game.CELL_SIZE + 1, Game.CELL_SIZE - 2, Game.CELL_SIZE - 2, null);
        }
        change = !change;
    }
    private void checkImages() {
        if (left) {
            image1 = left1;
            image2 = left2;
        } else if (right) {
            image1 = right1;
            image2 = right2;
        } else if (down) {
            image1 = down1;
            image2 = down2;
        } else if (up) {
            image1 = up1;
            image2 = up2;
        }
    }
    private void setLocation(Point location) {
        if (getLocation() != null) {
            left = getLocation().x - location.x == 1;
            right = location.x - getLocation().x == 1;
            down = location.y - getLocation().y == 1;
            up = getLocation().y - location.y == 1;
        }
        this.location = location;
    }

    public void move(Point pacmanLocation, ArrayList<Ghost> otherGhosts) {
        Point next;
        if (!FRIGHTENED) {
            if (isNextToPacman(pacmanLocation)) {
                setLocation(pacmanLocation);
                return;
            }
            next = findShortestPath(pacmanLocation, otherGhosts);
        } else
            next = findFarthestMove(pacmanLocation, otherGhosts);
        if (next != null && canMove(next, otherGhosts))
            setLocation(next);
        else
            goRandom(otherGhosts);
    }

    private Point findFarthestMove(Point pacmanLocation, ArrayList<Ghost> otherGhosts) {
        ArrayList<Point> neighbors = getNeighbors(getLocation());
        Point farthestPoint = null;
        double maxDistance = -1;

        for (Point neighbor : neighbors) {
            if (canMove(neighbor, otherGhosts)) {
                double distance = neighbor.distance(pacmanLocation);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    farthestPoint = neighbor;
                }
            }
        }

        return farthestPoint;
    }


    private Point findShortestPath(Point pacmanLocation, ArrayList<Ghost> otherGhosts) {
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        Set<Point> visited = new HashSet<>();

        queue.add(getLocation());
        visited.add(getLocation());

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(pacmanLocation))
                break;

            for (Point neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor) && canMove(neighbor, otherGhosts)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }

        Point current = pacmanLocation;
        while (cameFrom.containsKey(current) && !cameFrom.get(current).equals(getLocation()))
            current = cameFrom.get(current);
        if (current.equals(getLocation()) || current.equals(pacmanLocation))
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

    private boolean canMove(Point newLocation, ArrayList<Ghost> otherGhosts) {
        if (newLocation.x < 0 || newLocation.y < 0 || newLocation.x >= Game.MATRIX[0].length || newLocation.y >= Game.MATRIX.length)
            return false;
        for (Point wall : Board.getWALLS()) {
            if (wall.equals(newLocation))
                return false;
        }
        for (Ghost ghost : otherGhosts) {
            if (ghost != this && ghost.getLocation().equals(newLocation))
                return false;
        }
        return true;
    }

    public boolean isNextToPacman(Point pacmanLocation) {
        int dx = Math.abs(getLocation().x - pacmanLocation.x);
        int dy = Math.abs(getLocation().y - pacmanLocation.y);
        return dx + dy == 1;
    }

    private void goRandom(ArrayList<Ghost> otherGhosts) {
        ArrayList<Point> possibleMoves = getNeighbors(getLocation());
        Collections.shuffle(possibleMoves);
        for (Point move : possibleMoves) {
            if (canMove(move, otherGhosts)) {
                setLocation(move);
                return;
            }
        }
    }
    private void loadImages(Color color) {
        try {
            left1 = replaceColor(ImageIO.read(new File("images/ghost_left_1.png")), color);
            left2 = replaceColor(ImageIO.read(new File("images/ghost_left_2.png")), color);
            right1 = replaceColor(ImageIO.read(new File("images/ghost_right_1.png")), color);
            right2 = replaceColor(ImageIO.read(new File("images/ghost_right_2.png")), color);
            down1 = replaceColor(ImageIO.read(new File("images/ghost_down_1.png")), color);
            down2 = replaceColor(ImageIO.read(new File("images/ghost_down_2.png")), color);
            up1 = replaceColor(ImageIO.read(new File("images/ghost_up_1.png")), color);
            up2 = replaceColor(ImageIO.read(new File("images/ghost_up_2.png")), color);
            scaredImage = ImageIO.read(new File("images/scared_ghost.png"));
        } catch (IOException ignored) {
        }
        image1 = left1;
        image2 = left2;
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

    public static boolean isFrightened() {
        return FRIGHTENED;
    }

    public static void setFrightened(boolean FRIGHTENED) {
        Ghost.FRIGHTENED = FRIGHTENED;
    }

    public Point getLocation() {
        return location;
    }

    public void setStartLocation() {
        setLocation(new Point(startX, startY));
    }
}