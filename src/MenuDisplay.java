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
        pause.addActionListener(e -> {
            if (Game.isPAUSED())
                Game.CONTINUE();
            else
                Game.PAUSE();
        });
        JButton restart = new JButton("restart");
        restart.setBackground(Color.orange);
        restart.setFocusable(false);
        restart.addActionListener(e -> {
            Game.START();
            pause.setVisible(true);
            revalidate();
            repaint();
        });
        JButton last = new JButton("last");
        last.setFocusable(false);
        last.setBackground(Color.orange);
        last.addActionListener(e -> {
            Game.setLEVEL(Game.getLEVEL() - 1);
            restart.doClick();
        });
        JButton next = new JButton("next");
        next.setFocusable(false);
        next.setBackground(Color.orange);
        next.addActionListener(e -> {
            Game.setLEVEL(Game.getLEVEL() + 1);
            restart.doClick();
        });
        add(restart);
        add(last);
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
        new Timer(100, e -> {
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
