import java.awt.*;
import java.util.ArrayList;

public class Food {
    public final ArrayList<Point> apples, cherries;
    private int score;
    private Thread timer;

    public Food() {
        this.apples = new ArrayList<>();
        this.cherries = new ArrayList<>();
        this.score = 0;
        this.timer = null;
    }

    public void draw(Graphics g) {
        for (Point apple : apples) {
            g.drawImage(Picture.APPLE, apple.x * Game.CELL_SIZE + Game.CELL_SIZE / 4 + 1,
                    apple.y * Game.CELL_SIZE + Game.CELL_SIZE / 4 + 1,
                    Game.CELL_SIZE / 2, Game.CELL_SIZE / 2, null
            );
        }
        for (Point cherry : cherries) {
            g.drawImage(Picture.CHERRY, cherry.x * Game.CELL_SIZE + 1,
                    cherry.y * Game.CELL_SIZE + 1,
                    Game.CELL_SIZE - 2, Game.CELL_SIZE - 2, null
            );
        }
    }

    public void interactWith(Pacman pacman) {
        for (int i = 0; i < apples.size(); i++) {
            Point apple = apples.get(i);
            if (pacman.getLocation().equals(apple)) {
                apples.remove(apple);
                addToScore(10);
                SoundManager.playMunchSound();
                i--;
            }
        }
        for (int i = 0; i < cherries.size(); i++) {
            Point cherry = cherries.get(i);
            if (pacman.getLocation().equals(cherry)) {
                Ghost.setScaredTimer(Ghost.getScaredTime() + 11);
                if (timer == null) {
                    timer = getTimer();
                    timer.start();
                }
                Ghost.setSCARED(true);
                cherries.remove(cherry);
                addToScore(30);
                SoundManager.playEatingFruitSound();
                i--;
            }
        }
    }

    private Thread getTimer() {
        return new Thread(() -> {
            while (Ghost.getScaredTime() >= 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                if (!Game.isPAUSED())
                    Ghost.setScaredTimer(Ghost.getScaredTime() - 1);
            }
            Ghost.setSCARED(false);
            timer = null;
        });
    }

    public void addToScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }
}
