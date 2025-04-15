import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.*;

public class BoardHelper extends BoardFactory {

    public BoardHelper(byte[][] map) {
        super(map);
    }

    protected void go(Ghost ghost) {
        Point next;
        if (!Ghost.isSCARED()) {
            if (pacman.isNextTo(ghost)) {
                ghost.goTo(pacman.getLocation());
                return;
            }
            next = findShortestPath(ghost);
        } else {
            if (pacman.isNextTo(ghost) || pacman.collision(ghost)) {
                ghost.stay();
                return;
            }
            next = findFarthestMove(ghost);
        }
        if (next == null) {
            ArrayList<Point> possibleMoves = getNeighbors(ghost.getLocation(), ghost);
            if (possibleMoves.isEmpty())
                ghost.stay();
            else {
                Collections.shuffle(possibleMoves);
                ghost.goTo(possibleMoves.getFirst());
            }
        } else
            ghost.goTo(next);
    }

    /// Greedy search algorithm
    private Point findFarthestMove(Ghost ghost) {
        ArrayList<Point> neighbors = getNeighbors(ghost.getLocation(), ghost);
        Point farthestPoint = null;
        double maxDistance = -1;
        for (Point neighbor : neighbors) {
            double distance = neighbor.distance(pacman.getLocation());
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestPoint = neighbor;
            }
        }
        return farthestPoint;
    }

    /// BFS algorithm
    private Point findShortestPath(Ghost ghost) {
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        Set<Point> visited = new HashSet<>();
        queue.add(ghost.getLocation());
        visited.add(ghost.getLocation());
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(pacman.getLocation()))
                break;
            for (Point neighbor : getNeighbors(current, ghost)) {
                if (!visited.contains(neighbor)) {
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

    private ArrayList<Point> getNeighbors(Point point, Ghost ghost) {
        ArrayList<Point> neighbors = new ArrayList<>();
        neighbors.add(new Point(point.x + 1, point.y));
        neighbors.add(new Point(point.x - 1, point.y));
        neighbors.add(new Point(point.x, point.y + 1));
        neighbors.add(new Point(point.x, point.y - 1));
        for (int i = 0; i < neighbors.size(); i++) {
            Point neighbor = neighbors.get(i);
            if (walls.collision(neighbor) || ghost.collision(ghosts, neighbor) || outOfMap(neighbor)) {
                neighbors.remove(i);
                i--;
            }
        }
        return neighbors;
    }
}
