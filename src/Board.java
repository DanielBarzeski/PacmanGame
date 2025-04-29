import java.awt.*;

public class Board extends BoardHelper {
    private boolean update = true;

    public Board(byte[][] map) {
        super(map);
    }

    public boolean isUpdating() {
        return update;
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
            if (!Ghost.isSCARED() && ghost.isKilled())
                ghost.revive();
            if (pacman.collision(ghost)) {
                if (Ghost.isSCARED()) {
                    ghost.kill();
                    food.addToScore(20);
                    SoundManager.playEatingGhostSound();
                } else {
                    SoundManager.playEatingPacmanSound();
                    pacman.spriteBounds.x = 48;
                    update = false;
                    new Thread(() -> {
                        try {
                            Thread.sleep(2200);
                        } catch (InterruptedException ignored) {
                        }
                        for (Ghost g: ghosts) {
                            g.reset();
                        }
                        pacman.kill();
                        update = true;
                    }).start();
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
        if (!walls.collision(newPacLocation)) {
            pacman.setCurrentDirection(pacman.getNewDirection());
            pacman.setLocation(newPacLocation);
        } else if (!walls.collision(curPacLocation))
            pacman.setLocation(curPacLocation);
    }

    public void moveGhosts() {
        if (!pacman.isStaying()) {
            if (Ghost.getDELAY() == 7) {
                for (Ghost ghost : ghosts) {
                    moveGhost(ghost);
                }
            }
            Ghost.setDELAY(Ghost.getDELAY() + 1);
            if (Ghost.getDELAY() > 7) Ghost.setDELAY(0);
        }
    }

    private void moveGhost(Ghost ghost) {
        if (!ghost.isKilled()) {
            go(ghost);
            ghost.setLocation(new Point(
                    ghost.getLocation().x + ghost.getCurrentDirection().x,
                    ghost.getLocation().y + ghost.getCurrentDirection().y)
            );
        }
    }
}
