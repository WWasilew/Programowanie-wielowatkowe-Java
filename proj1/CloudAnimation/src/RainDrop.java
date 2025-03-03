import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

class RainDrop implements Runnable {
    private int x, y;
    private final int speed;
    private final RainPanel panel;
    private boolean running = true;
    private static BufferedImage dropImage;

    static {
        try {
            dropImage = ImageIO.read(new File("images/rainDrop.png"));
        } catch (IOException e) {
            System.err.println("Nie można załadować obrazka kropli.");
        }
    }

    public RainDrop(int x, int startY, RainPanel panel) {
        this.x = x;
        this.y = startY;
        this.speed = new Random().nextInt(5) + 2;
        this.panel = panel;
    }

    @Override
    public void run() {
        while (running) {
            y += speed;
            panel.repaint();

            if (y > panel.getHeight()) {
                running = false;
                panel.removeRainDrop(this);
                break;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running = false;
    }

    public void draw(Graphics g) {
        if (dropImage != null) {
            g.drawImage(dropImage, x, y, 50, 50, null);
        }
    }
}