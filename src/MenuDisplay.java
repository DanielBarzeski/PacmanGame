import javax.swing.*;
import java.awt.*;

public class MenuDisplay extends JPanel {
    private final JButton pause;

    public MenuDisplay(int width, int height) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.green);
        JButton restart = new JButton("restart");
        restart.setBackground(Color.orange);
        restart.addActionListener(e -> {
            Game.START();
            revalidate();
            repaint();
        });
        add(restart);
        pause = new JButton();
        pause.setBackground(Color.orange);
        pause.addActionListener(e -> {
            if (Game.isPAUSED())
                Game.CONTINUE();
            else
                Game.PAUSE();
        });
        add(pause);
        run();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Game.CONTROL_BOARD().drawMenu(g);
    }

    public void run() {
        new Timer(100, e -> {
            if (Game.isFINISHED())
                pause.setText("unavailable");
            else if (Game.isPAUSED()) {
                pause.setText("continue");
            } else {
                pause.setText("pause");
            }
            repaint();
        }).start();
    }
}
