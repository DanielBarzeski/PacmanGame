import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameDisplay extends JPanel {
    private static int WIDTH, HEIGHT;

    public GameDisplay() {
        setBackground(Color.black);
        run();
        setupKeyBindings();
    }

    public void run() {
        final int[] timeCounter = {0};
        new Timer(70, _ -> {
            if (Game.board().isUpdating()) {
                update(timeCounter);
                repaint();
            }
        }).start();
    }

    private void update(int[] timeCounter) {
        if (!Game.isFINISHED() && !Game.isPAUSED()) {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            revalidate();
            Game.board().updateRules();
            Game.board().moveGhosts();
            if (timeCounter[0] == 4) {
                Game.board().updateFood();
                Game.board().movePacman();
                timeCounter[0] = 0;
            }
            timeCounter[0]++;
        }
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        Object[][] keyActions = {
                {"LEFT", "moveLeft", (Runnable) () -> Game.board().pacman.goLeft()},
                {"RIGHT", "moveRight", (Runnable) () -> Game.board().pacman.goRight()},
                {"UP", "moveUp", (Runnable) () -> Game.board().pacman.goUp()},
                {"DOWN", "moveDown", (Runnable) () -> Game.board().pacman.goDown()}
        };

        for (Object[] keyAction : keyActions) {
            String key = (String) keyAction[0];
            String actionName = (String) keyAction[1];
            Runnable action = (Runnable) keyAction[2];
            inputMap.put(KeyStroke.getKeyStroke(key), actionName);
            actionMap.put(actionName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!Game.isFINISHED() && !Game.isPAUSED()) {
                        action.run();
                    }
                }
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Game.board().drawGame(g);
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static void setWIDTH(int WIDTH) {
        GameDisplay.WIDTH = WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static void setHEIGHT(int HEIGHT) {
        GameDisplay.HEIGHT = HEIGHT;
    }
}