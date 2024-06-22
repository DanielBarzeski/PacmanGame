import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

public class PacmanGame {
    private Window window = new Window();
    public PacmanGame() {
        System.out.println("the game is playing...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame ex = window;
            ex.setVisible(true);
        });
        playSound();
        System.out.println();
        System.out.println("the game ended..");
    }
    private void playSound(){
        int wait = 0;
        while (this.getWindow().getBoard().isInGame()){
            try {
                File soundFile = new File("images\\pacman_beginning.wav");
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(soundFile));
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);
                if (wait ==0){
                    Thread.sleep(1000);
                    wait++;
                }
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println("Error playing sound: " + e.getMessage());
            }
        }
    }

    public Window getWindow() {
        return window;
    }
}

