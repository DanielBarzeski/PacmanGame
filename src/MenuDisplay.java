import javax.swing.*;
import java.awt.*;

public class MenuDisplay extends JPanel {
    private final JButton pause;

    public MenuDisplay(int width, int height) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.green);
        pause = new JButton();
        pause.setBackground(Color.orange);
        pause.addActionListener(_ -> {
            if (Game.isPAUSED())
                Game.CONTINUE();
            else
                Game.PAUSE();
        });
        JButton restart = new JButton("restart");
        restart.setBackground(Color.orange);
        restart.setFocusable(false);
        restart.addActionListener(_ -> {
            Game.START();
            pause.setVisible(true);
            revalidate();
            repaint();
        });
        JButton previous = new JButton("prev");
        previous.setFocusable(false);
        previous.setBackground(Color.orange);
        previous.addActionListener(_ -> {
            Game.setLEVEL(Game.getLEVEL() - 1);
            restart.doClick();
        });
        JButton next = new JButton("next");
        next.setFocusable(false);
        next.setBackground(Color.orange);
        next.addActionListener(_ -> {
            Game.setLEVEL(Game.getLEVEL() + 1);
            restart.doClick();
        });
        add(restart);
        add(previous);
        add(next);
        add(pause);
        run();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Game.board().drawMenu(g);
    }

    public void run() {
        new Timer(100, _ -> {
            if (Game.isFINISHED())
                pause.setVisible(false);
            else if (Game.isPAUSED()) {
                pause.setText("continue");
            } else {
                pause.setText("pause");
            }
            repaint();
        }).start();
    }
}
