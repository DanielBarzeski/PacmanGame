import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SoundManager {
    private static final Clip
            backgroundSound = loadSound("background"),
            eatingAppleSound = loadSound("eatingApple"),
            eatingCherrySound = loadSound("eatingCherry"),
            eatingGhostSound = loadSound("eatingGhost"),
            eatingPacmanSound = loadSound("eatingPacman");

    private static Clip loadSound(String fileName) {
        try {
            File file = new File("audio/" + fileName + ".wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (Exception e) {
            System.out.println(fileName + " sound does not exist.");
            return null;
        }
    }

    public static void playBackgroundSound() {
        if (backgroundSound != null && !backgroundSound.isRunning()) {
            backgroundSound.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundSound.start();
        }
    }

    public static void stopBackgroundSound() {
        if (backgroundSound != null && backgroundSound.isRunning()) {
            backgroundSound.stop();
        }
    }

    public static void rewindBackgroundSound() {
        if (backgroundSound != null) {
            FloatControl volumeControl =
                    (FloatControl) backgroundSound.
                            getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-10.0f);
            backgroundSound.setFramePosition(0); // Rewind to the beginning
        }
    }

    public static void playEatingAppleSound() {
        if (eatingAppleSound != null) {
            if (eatingAppleSound.isRunning())
                return;
            eatingAppleSound.setFramePosition(0);
            eatingAppleSound.start();
        }
    }

    public static void playEatingCherrySound() {
        if (eatingCherrySound != null) {
            eatingCherrySound.setFramePosition(0);
            eatingCherrySound.start();
        }
    }

    public static void playEatingGhostSound() {
        if (eatingGhostSound != null) {
            eatingGhostSound.setFramePosition(0);
            eatingGhostSound.start();
        }
    }

    public static void playEatingPacmanSound() {
        if (eatingPacmanSound != null) {
            eatingPacmanSound.setFramePosition(0);
            eatingPacmanSound.start();
        }
    }
}
