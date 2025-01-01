import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private static boolean FINISHED, PAUSED;
    public final static char[][] MATRIX = readCharArrayFromFile();
    public final static int CELL_SIZE = 20, WIDTH = MATRIX[0].length*CELL_SIZE, HEIGHT = MATRIX.length*CELL_SIZE;
    private final static int LEVEL = 1;
    private static Board BOARD;

    public static void START() {
        BOARD = new Board();
        PAUSED = false;
        FINISHED = false;
        System.out.println("THE GAME HAS STARTED!");
        SoundManager.rewindBackgroundMusic();
        SoundManager.playBackgroundMusic();
    }

    public static void END() {
        PAUSED = true;
        FINISHED = true;
        System.out.println("THE GAME IS FINISHED!");
        SoundManager.stopBackgroundMusic();
    }

    public static void PAUSE() {
        PAUSED = true;
    }

    public static void CONTINUE() {
        PAUSED = false;
    }

    public static boolean isFINISHED() {
        return FINISHED;
    }

    public static boolean isPAUSED() {
        return PAUSED;
    }

    public static Board controlBoard() {
        return BOARD;
    }
    private static char[][] readCharArrayFromFile(){
        ArrayList<char[]> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("images/level_"+LEVEL))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.toCharArray());
            }
        }
        catch (IOException e) {
            System.out.println("Error reading file: " + "images/level_"+ LEVEL);
        }
        char[][] charArray = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            charArray[i] = lines.get(i);
        }

        return charArray;
    }
}