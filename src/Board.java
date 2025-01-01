import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Board {
    private static ArrayList<Point> WALLS;
    private final LinkedList<Point> points, specialPoints;
    private Pacman pacman;
    private final ArrayList<Ghost> ghosts;
    private int score, time;
    private Thread timer;
    private boolean slow, won;
    private BufferedImage life,gameOver, winning;

    public Board() {
        won = false;
        time = -1;
        score = 0;
        timer = null;
        Ghost.setFrightened(false);
        WALLS = new ArrayList<>();
        points = new LinkedList<>();
        specialPoints = new LinkedList<>();
        ghosts = new ArrayList<>();
        scanMatrix();
        try {
            life = ImageIO.read(new File("images/heart.png"));
            gameOver = ImageIO.read(new File("images/gameOver.png"));
            winning = ImageIO.read(new File("images/winning.png"));
        } catch (IOException ignored) {
        }
    }

    private void scanMatrix() {
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.MAGENTA);
        colors.add(Color.ORANGE);
        int index = 0;
        for (int rows = 0; rows < Game.MATRIX.length; rows++) {
            for (int cols = 0; cols < Game.MATRIX[0].length; cols++) {
                if (Game.MATRIX[rows][cols] == '0')
                    points.add(new Point(cols, rows));
                else if (Game.MATRIX[rows][cols] == '1')
                    WALLS.add(new Point(cols, rows));
                else if (Game.MATRIX[rows][cols] == '2') {
                    ghosts.add(new Ghost(cols, rows, colors.get(index)));
                    index++;
                    if (index >= 3)
                        colors.add(new Color(100 + (int) (Math.random() * 156), 100 + (int) (Math.random() * 156), 100 + (int) (Math.random() * 156)));
                } else if (Game.MATRIX[rows][cols] == '4')
                    pacman = new Pacman(cols, rows);
                else if (Game.MATRIX[rows][cols] == '5')
                    specialPoints.add(new Point(cols, rows));

            }
        }
        if (pacman == null){
            System.out.println("The game cant work without a pacman.");
            System.exit(0);
        }
    }

    public void drawScore(Graphics g) {
        g.setColor(Color.white);
        g.drawString("Score: " + score, 1, 20);
        if (time >= 0)
            g.drawString("scared for: " + time + "s", 80, 20);
        int x = 340;
        for (int i = 0; i < pacman.getHealth(); i++) {
            g.drawImage(life, x + 24 * i, 7, 20, 20, null);
        }

    }

    public void draw(Graphics g) {
        drawFood(g);
        if (!Ghost.isFrightened())
            pacman.draw(g);
        for (Ghost ghost : ghosts) {
            ghost.draw(g);
        }
        if (Ghost.isFrightened())
            pacman.draw(g);
        drawWalls(g);
        if (Game.isFINISHED()) {
            if (won)
                g.drawImage(winning,Game.WIDTH/2-50,Game.HEIGHT/2-60,100,100,null);
            else
                g.drawImage(gameOver,Game.WIDTH/2-50,Game.HEIGHT/2-60,100,100,null);

        }

    }

    private void drawFood(Graphics g) {
        for (int i = 0; i < points.size(); i++) {
            Point food = points.get(i);
            if (pacman.getLocation().equals(food)) {
                SoundManager.playMunchSound();
                points.remove(food);
                score += 10;
                i--;
            }
            g.setColor(Color.pink);
            g.fillOval(food.x * Game.CELL_SIZE + Game.CELL_SIZE / 5 * 2,
                    food.y * Game.CELL_SIZE + Game.CELL_SIZE / 5 * 2,
                    Game.CELL_SIZE / 5 - 1, Game.CELL_SIZE / 5 - 1
            );
            g.setColor(Color.white);
            g.drawOval(food.x * Game.CELL_SIZE + Game.CELL_SIZE / 5 * 2,
                    food.y * Game.CELL_SIZE + Game.CELL_SIZE / 5 * 2,
                    Game.CELL_SIZE / 5 - 1, Game.CELL_SIZE / 5 - 1
            );

        }
        for (int i = 0; i < specialPoints.size(); i++) {
            Point specialPoint = specialPoints.get(i);
            if (pacman.getLocation().equals(specialPoint)) {
                SoundManager.playEatingFruitSound();
                time += 11;
                if (timer == null) {
                    timer = getTimer();
                    timer.start();
                }
                Ghost.setFrightened(true);
                specialPoints.remove(specialPoint);
                score += 30;
                i--;
            }
            g.setColor(Color.pink);
            g.fillOval(specialPoint.x * Game.CELL_SIZE + Game.CELL_SIZE / 4,
                    specialPoint.y * Game.CELL_SIZE + Game.CELL_SIZE / 4,
                    Game.CELL_SIZE / 2 - 1, Game.CELL_SIZE / 2 - 1
            );
            g.setColor(Color.white);
            g.drawOval(specialPoint.x * Game.CELL_SIZE + Game.CELL_SIZE / 4,
                    specialPoint.y * Game.CELL_SIZE + Game.CELL_SIZE / 4,
                    Game.CELL_SIZE / 2 - 1, Game.CELL_SIZE / 2 - 1
            );

        }


    }

    private void drawWalls(Graphics g) {
        for (Point wall : WALLS) {
            g.setColor(Color.cyan);
            g.drawRoundRect(wall.x * Game.CELL_SIZE,
                    wall.y * Game.CELL_SIZE,
                    Game.CELL_SIZE - 1, Game.CELL_SIZE - 1, 5, 5);
        }
    }
    public synchronized void update() {
            for (Ghost ghost : ghosts) {
                if (areColliding(pacman, ghost)) {
                    if (pacman.isEaten()) {
                        SoundManager.playDeathSound();
                        pacman.removeHealth();
                        if (pacman.getHealth() == 0) {
                            Game.END();
                            time = -1;
                            return;
                        }
                        pacman.stop();
                        pacman.setStartLocation();
                        for (Ghost GHOST : ghosts) {
                            GHOST.setStartLocation();
                        }
                        Game.PAUSE();
                    } else {
                        SoundManager.playEatingGhostSound();
                        ghost.setStartLocation();
                        score += 30;
                    }
                }
                if (pacman.isRunning() && slow)
                    ghost.move(pacman.getLocation(), ghosts);
            }
            slow = !slow;
            pacman.move();
            if (time == 0)
                Ghost.setFrightened(false);
            pacman.setEaten(!Ghost.isFrightened());
            if ((points.isEmpty() && specialPoints.isEmpty())) {
                Game.END();
                time = -1;
                won = true;
            }
    }
    private boolean areColliding(Pacman pacman, Ghost ghost) {
        int dx = Math.abs(ghost.getLocation().x - pacman.getLocation().x);
        int dy = Math.abs(ghost.getLocation().y - pacman.getLocation().y);
        return dx + dy == 0;
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
            timer = null;
        });
    }


    public static ArrayList<Point> getWALLS() {
        return WALLS;
    }

    public Pacman getPacman() {
        return pacman;
    }

}