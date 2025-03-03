import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class RainManager {
    private final RainPanel panel;
    private final ScheduledExecutorService scheduler;
    private final Random random = new Random();

    public RainManager(RainPanel panel) {
        this.panel = panel;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            panel.addRainDrop();
        }, 0, random.nextInt(200) + 100, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdownNow();
        panel.stopRain();
    }
}
