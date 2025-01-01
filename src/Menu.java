import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel implements Runnable {
    private final JButton pause, restart;

    public Menu(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.darkGray);
        pause = new JButton("pause");
        pause.addActionListener(e -> {
            if (Game.isPAUSED())
                Game.CONTINUE();
            else
                Game.PAUSE();
        });
        add(pause);
        restart = new JButton("restart");
        restart.addActionListener(e -> {
            Game.START();
            remove(restart);
            add(pause);
            revalidate();
            repaint();
        });
        new Thread(this).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Game.controlBoard().drawScore(g);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("message: " + e.getMessage());
            }
            if (Game.isFINISHED()) {
                remove(pause);
                add(restart);
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
