import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private static boolean FINISHED, PAUSED;
    private final static char[][] MAP = readCharArrayFromFile();
    public final static int CELL_SIZE = 17,
            WIDTH = MAP[0].length * CELL_SIZE,
            HEIGHT = MAP.length * CELL_SIZE;
    private final static int LEVEL = 2;
    private static Board BOARD;

    public static void START() {
        BOARD = new Board(MAP);
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

    public static Board CONTROL_BOARD() {
        return BOARD;
    }
    private static char[][] readCharArrayFromFile(){
        ArrayList<char[]> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("file/level_"+LEVEL))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.toCharArray());
            }
        }
        catch (IOException e) {
            System.out.println("Error reading file: " + "file/level_"+ LEVEL);
            System.exit(0);
        }
        char[][] charArray = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            charArray[i] = lines.get(i);
        }

        return charArray;
    }
}