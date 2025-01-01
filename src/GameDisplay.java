import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameDisplay extends JPanel implements Runnable {
    public GameDisplay() {
        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        setBackground(Color.black);
        new Thread(this).start();
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(130);
                } catch (InterruptedException ignored) {
                }
                repaint();
            }
        }).start();
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        Object[][] keyActions = {
                {"LEFT", "moveLeft", (Runnable) () -> Game.controlBoard().getPacman().goLeft()},
                {"RIGHT", "moveRight", (Runnable) () -> Game.controlBoard().getPacman().goRight()},
                {"UP", "moveUp", (Runnable) () -> Game.controlBoard().getPacman().goUp()},
                {"DOWN", "moveDown", (Runnable) () -> Game.controlBoard().getPacman().goDown()}
        };

        for (Object[] keyAction : keyActions) {
            String key = (String) keyAction[0];
            String actionName = (String) keyAction[1];
            Runnable action = (Runnable) keyAction[2];
            inputMap.put(KeyStroke.getKeyStroke(key), actionName);
            actionMap.put(actionName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!Game.isFINISHED() && !Game.isPAUSED())
                        action.run();
                }
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Game.controlBoard().draw(g);
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(390);
            } catch (InterruptedException ignored) {
            }
            if (!Game.isFINISHED() && !Game.isPAUSED())
                Game.controlBoard().update();
        }
    }
}