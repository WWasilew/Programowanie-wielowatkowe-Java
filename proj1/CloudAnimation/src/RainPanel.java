import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

class RainPanel extends JPanel {
    private final List<RainDrop> drops = new CopyOnWriteArrayList<>(); // bezpieczne iterowanie w wielu watkach taka inna lista panowie
    private final List<Thread> threads = new ArrayList<>();
    private static BufferedImage cloudImage;
    private final int cloudX = 110, cloudY = 10;
    private final int cloudWidth = 600, cloudHeight = 400;

    static {
        try {
            cloudImage = ImageIO.read(new File("images/cloud.png"));
        } catch (IOException e) {
            System.err.println("Nie mozna zaladowac obrazka chmury.");
        }
    }

    public RainPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
    }

    public void addRainDrop() {
        int dropX = cloudX + new Random().nextInt(cloudWidth - 100) + 50;
        int dropY = cloudY + cloudHeight - 100 + new Random().nextInt(20);
        RainDrop drop = new RainDrop(dropX, dropY, this);
        drops.add(drop);
        Thread thread = new Thread(drop);
        threads.add(thread);
        thread.start();
    }

    public synchronized void removeRainDrop(RainDrop drop) {
        drops.remove(drop);
    }

    public synchronized void stopRain() {
        for (RainDrop drop : drops) {
            drop.stop();
        }
        for (Thread thread : threads) {
            thread.interrupt();
        }
        drops.clear();
        threads.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (cloudImage != null) {
            g.drawImage(cloudImage, cloudX, cloudY, cloudWidth, cloudHeight, null);
        }

        for (RainDrop drop : drops) {
            drop.draw(g);
        }
    }
}