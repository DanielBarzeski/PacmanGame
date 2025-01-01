import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SoundManager {
    private static final Clip
            backgroundMusic = loadSound("audio/pacman_beginning.wav"),
            munchSound = loadSound("audio/pacman_eating.wav"),
            eatingFruitSound = loadSound("audio/pacman_eatFruit.wav"),
            eatingGhostSound = loadSound("audio/pacman_eatGhost.wav"),
            deathSound = loadSound("audio/pacman_death.wav");
    private static Clip loadSound(String fileName) {
        try {
            File file = new File(fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (Exception ignored) {
        }
        System.out.println(fileName+" sound does no exist.");
        return null;
    }
    public static void playBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        }
    }
    public static void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
    public static void rewindBackgroundMusic() {
        if (backgroundMusic != null) {
            FloatControl volumeControl =
                    (FloatControl) backgroundMusic.
                    getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-10.0f);
            backgroundMusic.setFramePosition(0); // Rewind to the beginning
        }
    }
    public static void playMunchSound() {
        if (munchSound != null) {
            munchSound.setFramePosition(0);
            munchSound.start();
        }
    }
    public static void playEatingFruitSound() {
        if (eatingFruitSound != null) {
            eatingFruitSound.setFramePosition(0);
            eatingFruitSound.start();
        }
    }

    public static void playEatingGhostSound() {
        if (eatingGhostSound != null) {
            eatingGhostSound.setFramePosition(0);
            eatingGhostSound.start();
        }
    }

    public static void playDeathSound() {
        if (deathSound != null) {
            deathSound.setFramePosition(0);
            deathSound.start();
        }
    }
}
