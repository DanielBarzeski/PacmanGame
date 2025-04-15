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
                    var ref = new Object() {
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ignored) {
                            }
                            for (Ghost value : ghosts) {
                                value.reset();
                            }
                            pacman.kill();
                            update = true;
                            thread = null;
                        });
                    };
                    ref.thread.start();
                    return;
                }
            }
        }
    }

    public void updateFood() {
        food.interactWith(pacman);
    }

    public void movePacman() {
        if (!Ghost.isSCARED()) {
            for (Ghost ghost : ghosts) {
                if (!ghost.isDelay() && (pacman.isNextTo(ghost) || pacman.collision(ghost)))
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
        if (!walls.collision(newPacLocation)) {
            pacman.setCurrentDirection(pacman.getNewDirection());
            pacman.setLocation(newPacLocation);
        } else if (!walls.collision(curPacLocation))
            pacman.setLocation(curPacLocation);
    }

    public void moveGhosts() {
        if (!pacman.isStaying()) {
            for (Ghost ghost : ghosts) {
                moveGhost(ghost);
            }
        }
    }

    private void moveGhost(Ghost ghost) {
        if (!ghost.isDelay() && !ghost.isKilled()) go(ghost);
        else ghost.stay();
        ghost.setLocation(new Point(
                ghost.getLocation().x + ghost.getCurrentDirection().x,
                ghost.getLocation().y + ghost.getCurrentDirection().y)
        );
        ghost.setDelay(!ghost.isDelay());
    }
}
