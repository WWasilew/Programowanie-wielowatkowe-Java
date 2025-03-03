import javax.swing.*;
import java.awt.*;

public class RainFrame extends JFrame {
    private final RainPanel rainPanel;
    private final RainManager rainManager;
    private final Thread rainThread;

    public RainFrame() {
        setTitle("Rain Animation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        rainPanel = new RainPanel();
        rainPanel.setBackground(Color.GRAY);
        add(rainPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Start");
        //JButton stopButton = new JButton("Stop");

        rainManager = new RainManager(rainPanel);
        rainThread = new Thread(rainManager);

        startButton.addActionListener(e -> {
            if (!rainThread.isAlive()) {
                new Thread(rainManager).start();
            }
        });

        /*
        stopButton.addActionListener(e -> {
            rainManager.stop();
            rainPanel.stopRain();
        }); */

        controlPanel.add(startButton);
        //controlPanel.add(stopButton);

        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}