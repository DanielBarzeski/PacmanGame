import javax.swing.*;

public class Window extends JFrame {
    private Board board = new Board();
    public Window() {
        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setSize(board.getSCREEN_WIDTH()+40, board.getSCREEN_HEIGHT()+60);
        //
        add(board);
        //
        setLocationRelativeTo(null);

    }

    public Board getBoard() {
        return board;
    }
}
