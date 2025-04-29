import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private static boolean FINISHED, PAUSED, WON;
    public static final int CELL_SIZE = 16;
    private static int LEVEL;
    private static Board BOARD;

    public static void START() {
        byte[][] map = readByteArrayFromFile();
        GameDisplay.setWIDTH(map[0].length * CELL_SIZE);
        GameDisplay.setHEIGHT(map.length * CELL_SIZE);
        BOARD = new Board(map);
        PAUSED = false;
        FINISHED = false;
        WON = false;
    }

    public static void RESTART() {
        if (FINISHED) {
            SoundManager.rewindBackgroundSound();
            SoundManager.playBackgroundSound();
        }
        START();
        System.out.println("THE GAME HAS STARTED!");
    }

    public static void END(boolean winning) {
        WON = winning;
        PAUSED = true;
        FINISHED = true;
        System.out.println("THE GAME IS FINISHED!");
        SoundManager.stopBackgroundSound();
    }

    private static byte[][] readByteArrayFromFile() {
        ArrayList<byte[]> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("levels/level_" + LEVEL))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.getBytes());
            }
        } catch (IOException e) {
            LEVEL = 0;
            return readByteArrayFromFile();
        }

        byte[][] byteArray = new byte[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            byteArray[i] = lines.get(i);
        }

        return byteArray;
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

    public static Board board() {
        return BOARD;
    }

    public static int getLEVEL() {
        return LEVEL;
    }

    public static void setLEVEL(int level) {
        Game.LEVEL = level;
    }
}