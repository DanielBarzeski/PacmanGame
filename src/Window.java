import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Window extends JFrame {
    public Window() {
        setTitle("PACMAN GAME");
        setIconImage(Objects.requireNonNull(Picture.GHOST).getSubimage(16, 16, 16, 16));
        getContentPane().setBackground(Color.yellow);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        JPanel display = new JPanel();
        display.setBackground(Color.blue.darker().darker());
        display.setLayout(new FlowLayout());
        int[] curLevel = new int[]{0};
        new Timer(50, e -> {
            if (curLevel[0] != Game.getLEVEL()) {
                display.setPreferredSize(new Dimension(
                        Math.max(GameDisplay.getWIDTH(), 690) + 10,
                        GameDisplay.getHEIGHT() + 55));
                pack();
                setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2, 0);
                curLevel[0] = Game.getLEVEL();
            }
        }).start();
        Game.setLEVEL(2);
        Game.START();
        display.add(new MenuDisplay(690, 40));
        display.add(new GameDisplay());
        add(display);
        setVisible(true);
    }
}
