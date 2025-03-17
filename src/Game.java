import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private static boolean FINISHED, PAUSED, WON;
    public static final int CELL_SIZE = 17;
    private static int WIDTH, HEIGHT, LEVEL; // max 4
    private static Board BOARD;

    public static void START() {
        byte[][] map = readByteArrayFromFile();
        WIDTH = map[0].length * CELL_SIZE;
        HEIGHT = map.length * CELL_SIZE;
        BOARD = new Board(map);
        PAUSED = false;
        FINISHED = false;
        WON = false;
        System.out.println("THE GAME HAS STARTED!");
        SoundManager.rewindBackgroundMusic();
        SoundManager.playBackgroundMusic();
    }

    public static void END(boolean winning) {
        WON = winning;
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

    public static boolean isWON() {
        return WON;
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

    private static byte[][] readByteArrayFromFile() {
        ArrayList<byte[]> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("levels/level_" + LEVEL))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.getBytes());
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + "levels/level_" + LEVEL);
            System.exit(0);
        }

        byte[][] byteArray = new byte[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            byteArray[i] = lines.get(i);
        }

        return byteArray;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static int getLEVEL() {
        return LEVEL;
    }

    public static void setLEVEL(int level) {
        if (level >= 0 && level <= 4)
            Game.LEVEL = level;
    }
}