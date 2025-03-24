import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Picture {
    public static final BufferedImage
            HEART = getBufImage("heart.png"),
            CHERRY = getBufImage("cherry.png"),
            APPLE = getBufImage("apple.png"),
            PACMAN = getBufImage("pacman.png"),
            GHOST = getBufImage("ghost.png"),
            GHOST_SCARED_BLUE = getBufImage("ghost_scared_blue.png"),
            GHOST_SCARED_WHITE = getBufImage("ghost_scared_white.png"),
            WINNING = getBufImage("winning.png"),
            LOSING = getBufImage("losing.png");


    private static BufferedImage getBufImage(String pathname) {
        try {
            return ImageIO.read(new File("images/" + pathname));
        } catch (IOException e) {
            return null;
        }
    }
    public static Color randomColor() {
        return new Color(75 + (int) (Math.random() * 156), 75 + (int) (Math.random() * 156), 75 + (int) (Math.random() * 156));
    }

    public static BufferedImage replaceColor(BufferedImage image, Color replacement) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb, true);

                if (isSpecificRed(color) && !isTransparent(color)) {
                    result.setRGB(x, y, replacement.getRGB());
                } else {
                    result.setRGB(x, y, rgb);
                }
            }
        }

        return result;
    }

    private static boolean isSpecificRed(Color color) {
        return color.getRed() > 120 && color.getGreen() < 100 && color.getBlue() < 100;
    }

    private static boolean isTransparent(Color color) {
        return color.getAlpha() == 0;
    }
}
