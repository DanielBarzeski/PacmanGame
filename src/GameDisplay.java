import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameDisplay extends JPanel {
    public GameDisplay() {
        setBackground(Color.black);
        run();
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        Object[][] keyActions = {
                {"LEFT", "moveLeft", (Runnable) () -> Game.CONTROL_BOARD().pacman.goLeft()},
                {"RIGHT", "moveRight", (Runnable) () -> Game.CONTROL_BOARD().pacman.goRight()},
                {"UP", "moveUp", (Runnable) () -> Game.CONTROL_BOARD().pacman.goUp()},
                {"DOWN", "moveDown", (Runnable) () -> Game.CONTROL_BOARD().pacman.goDown()}
        };

        for (Object[] keyAction : keyActions) {
            String key = (String) keyAction[0];
            String actionName = (String) keyAction[1];
            Runnable action = (Runnable) keyAction[2];
            inputMap.put(KeyStroke.getKeyStroke(key), actionName);
            actionMap.put(actionName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!Game.isFINISHED() && !Game.isPAUSED() && Game.CONTROL_BOARD().pacman.isRunning())
                        action.run();
                }
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Game.CONTROL_BOARD().drawGame(g);
    }

    public void run() {
        final int[] counter = {0, 0};
        new Timer(80, e -> {
            if (!Game.isFINISHED() && !Game.isPAUSED()) {
                setPreferredSize(new Dimension(Game.getWIDTH(), Game.getHEIGHT()));
                revalidate();
                Game.CONTROL_BOARD().updateRules();
                if (counter[0] == 4) {
                    Game.CONTROL_BOARD().updateFood();
                    Game.CONTROL_BOARD().movePacman();
                    counter[0] = 0;
                }
                if (counter[1] == 9) {
                    Game.CONTROL_BOARD().moveGhosts();
                    counter[1] = 0;
                }
                counter[0]++;
                counter[1]++;
            }
            repaint();
        }).start();
    }
}