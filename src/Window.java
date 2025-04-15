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
        int menuWidth = 690, menuHeight = 40;
        int[] curLevel = new int[]{0};
        new Timer(50, _ -> {
            if (curLevel[0] != Game.getLEVEL()) {
                display.setPreferredSize(new Dimension(
                        Math.max(GameDisplay.getWIDTH(), menuWidth) + 10,
                        GameDisplay.getHEIGHT() + menuHeight + 15));
                pack();
                setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2, 0);
                curLevel[0] = Game.getLEVEL();
            }
        }).start();
        Game.setLEVEL(2);
        Game.START();
        display.add(new MenuDisplay(menuWidth, menuHeight));
        display.add(new GameDisplay());
        add(display);
        setVisible(true);
    }
}
