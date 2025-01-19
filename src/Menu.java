import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel implements Runnable {
    private final JButton pause, restart;

    public Menu(int width, int height) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.darkGray);
        restart = new JButton("restart");
        restart.addActionListener(e -> {
            Game.START();
            revalidate();
            repaint();
        });
        add(restart);
        pause = new JButton("pause");
        pause.addActionListener(e -> {
            if (Game.isPAUSED())
                Game.CONTINUE();
            else
                Game.PAUSE();
        });
        add(pause);
        new Thread(this).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Game.CONTROL_BOARD().drawScore(g);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            if (Game.isFINISHED()) {
                setBackground(Color.green.darker());
                revalidate();
            }
            else if (Game.isPAUSED()) {
                setBackground(Color.magenta.darker().darker());
                pause.setText("continue");
            } else {
                setBackground(Color.blue);
                pause.setText("pause");
            }
            repaint();
        }
    }
}
