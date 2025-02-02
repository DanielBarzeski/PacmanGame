import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameDisplay extends JPanel implements Runnable {
    public GameDisplay() {
        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        setBackground(Color.black);
        new Thread(this).start();
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        Object[][] keyActions = {
                {"LEFT", "moveLeft", (Runnable) () -> Game.CONTROL_BOARD().getPacman().goLeft()},
                {"RIGHT", "moveRight", (Runnable) () -> Game.CONTROL_BOARD().getPacman().goRight()},
                {"UP", "moveUp", (Runnable) () -> Game.CONTROL_BOARD().getPacman().goUp()},
                {"DOWN", "moveDown", (Runnable) () -> Game.CONTROL_BOARD().getPacman().goDown()},
                {"S", "stay", (Runnable) () -> Game.CONTROL_BOARD().getPacman().stay()}
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
        Game.CONTROL_BOARD().draw(g);
    }
    @Override
    public void run() {
        int counter = 0;
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            if (counter == 3) {
                Game.CONTROL_BOARD().update();
                counter = 0;
            }
            repaint();
            counter++;
        }
    }
}
