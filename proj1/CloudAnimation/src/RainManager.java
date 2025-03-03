import java.util.Random;

class RainManager implements Runnable {
    private final RainPanel panel;
    private boolean running = true;
    private final Random random = new Random();

    public RainManager(RainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(random.nextInt(200) + 100);
                panel.addRainDrop();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running = false;
    }
}