import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public Window() {
        setTitle("PACMAN GAME");
        getContentPane().setBackground(Color.gray.brighter());
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel display = new JPanel();
        display.setLayout(new FlowLayout());
        display.setBackground(Color.gray.brighter());
        display.setPreferredSize(new Dimension(Math.max(420,Game.WIDTH), Game.HEIGHT + 40 + 10));
        Game.START();
        display.add(new Menu(420,40));
        display.add(new GameDisplay());
        add(display);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
