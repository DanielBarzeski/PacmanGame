import java.awt.*;

public class Board extends BoardHelper {
    public Board(char[][] map) {
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
        pacman.run();
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
                    for (Ghost ghostValue : ghosts) {
                        ghostValue.reset();
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
        if (border.collision(newPacLocation))
            newPacLocation = null;
        if (border.collision(curPacLocation))
            curPacLocation = null;
        if (newPacLocation != null) {
            pacman.setCurrentDirection(pacman.getNewDirection());
            curPacLocation = newPacLocation;
        }
        if (curPacLocation != null) {
            pacman.setLocation(curPacLocation);
        }
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
