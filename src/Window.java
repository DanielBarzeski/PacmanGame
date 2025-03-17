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
        JPanel display = new JPanel();
        display.setBackground(Color.blue.darker().darker());
        display.setPreferredSize(new Dimension(750, 640));
        display.setLayout(new FlowLayout());
        Game.setLEVEL(2);
        Game.START();
        display.add(new MenuDisplay(600, 40));
        display.add(new GameDisplay());
        add(display);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
