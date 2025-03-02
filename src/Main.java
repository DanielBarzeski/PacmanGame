import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Window window = new Window();
        javax.swing.SwingUtilities.invokeLater(() -> window.setVisible(true));
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}