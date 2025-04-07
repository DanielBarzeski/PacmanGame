import java.awt.*;

public class Board extends BoardHelper {
    public Board(byte[][] map) {
        super(map);
    }

    public void updateRules() {
        if (pacman.getLife() == 0) {
            Game.END(false);
            return;
        }
        if (food.apples.isEmpty() && food.cherries.isEmpty()) {
            Game.END(true);
            return;
        }
        for (int i = 0; i < ghosts.size(); i++) {
            Ghost ghost = ghosts.get(i);
            if (ghost.collision(pacman)) {
                if (Ghost.isSCARED()) {
                    ghost.kill();
                    food.addToScore(20);
                    SoundManager.playEatingGhostSound();
                } else {
                    SoundManager.playDeathSound();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                    for (Ghost value : ghosts) {
                        value.reset();
                    }
                    pacman.kill();
                    return;
                }
            }
        }
    }

    public void updateFood() {
        food.interactWith(pacman);
    }

    public void movePacman() {
        pacman.run();
        if (!Ghost.isSCARED()) {
            for (Ghost ghost : ghosts) {
                if (pacman.isNextTo(ghost))
                    return;
            }
        }
        Point newPacLocation = new Point(
                pacman.getLocation().x + pacman.getNewDirection().x,
                pacman.getLocation().y + pacman.getNewDirection().y
        );
        Point curPacLocation = new Point(
                pacman.getLocation().x + pacman.getCurrentDirection().x,
                pacman.getLocation().y + pacman.getCurrentDirection().y
        );
        adjustToMap(newPacLocation);
        adjustToMap(curPacLocation);
        if (!border.collision(newPacLocation)) {
            pacman.setCurrentDirection(pacman.getNewDirection());
            curPacLocation = newPacLocation;
        }
        if (!border.collision(curPacLocation))
            pacman.setLocation(curPacLocation);
    }

    public void moveGhosts() {
        if (!pacman.getCurrentDirection().equals(new Point())) {
            for (Ghost ghost : ghosts) {
                moveGhost(ghost);
                if (!Ghost.isSCARED())
                    ghost.run();
            }
        }
    }

    private void moveGhost(Ghost ghost) {
        if (ghost.isRunning()) {
            go(ghost);
            ghost.setLocation(new Point(
                    ghost.getLocation().x + ghost.getCurrentDirection().x,
                    ghost.getLocation().y + ghost.getCurrentDirection().y)
            );
        }
    }
}
