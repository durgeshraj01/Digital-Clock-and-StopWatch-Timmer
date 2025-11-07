import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class StopWatch extends JFrame {
    private JLabel clockLabel;
    private JLabel stopwatchLabel;
    private JButton startButton, stopButton, resetButton, lapButton;
    private Timer stopwatchTimer, clockTimer;
    private long startTime = 0;
    private long elapsedTime = 0;
    private boolean isRunning = false;
    private Queue<String> lapTimes = new LinkedList<>();
    private JTextArea lapTextArea;

    public StopWatch() {
        setTitle("Clock & Stopwatch");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Clock Panel
        JPanel clockPanel = new JPanel(new BorderLayout());
        clockLabel = new JLabel("", SwingConstants.CENTER);
        clockLabel.setFont(new Font("Arial", Font.BOLD, 40));
        clockPanel.add(clockLabel, BorderLayout.CENTER);
        clockPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(clockPanel, BorderLayout.NORTH);

        // Stopwatch Panel
        JPanel stopwatchPanel = new JPanel(new BorderLayout(10, 10));
        stopwatchLabel = new JLabel("00:00:00.000", SwingConstants.CENTER);
        stopwatchLabel.setFont(new Font("Arial", Font.BOLD, 60));

        stopwatchPanel.add(stopwatchLabel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        resetButton = new JButton("Reset");
        lapButton = new JButton("Lap");

        Font buttonFont = new Font("Arial", Font.BOLD, 30);
        startButton.setFont(buttonFont);
        stopButton.setFont(buttonFont);
        resetButton.setFont(buttonFont);
        lapButton.setFont(buttonFont);

        Color greenColor = new Color(0, 200, 0); // Medium green

        startButton.setBackground(greenColor);

        stopButton.setBackground(greenColor);

        resetButton.setBackground(greenColor);

        lapButton.setBackground(greenColor);

        // Make buttons same size
        Dimension buttonSize = new Dimension(800, 400);
        startButton.setPreferredSize(buttonSize);
        stopButton.setPreferredSize(buttonSize);
        resetButton.setPreferredSize(buttonSize);
        lapButton.setPreferredSize(buttonSize);

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(lapButton);

        stopwatchPanel.add(buttonPanel, BorderLayout.CENTER);

        // Lap Times Panel
        lapTextArea = new JTextArea();
        lapTextArea.setEditable(false);
        lapTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(lapTextArea);
        scrollPane.setPreferredSize(new Dimension(350, 150));
        stopwatchPanel.add(scrollPane, BorderLayout.SOUTH);

        add(stopwatchPanel, BorderLayout.CENTER);

        // Initialize timers
        initClock();
        initStopwatch();

        // Button actions
        startButton.addActionListener(e -> startStopwatch());
        stopButton.addActionListener(e -> stopStopwatch());
        resetButton.addActionListener(e -> resetStopwatch());
        lapButton.addActionListener(e -> recordLapTime());

        pack(); // Size the window appropriately
        setLocationRelativeTo(null); // Center the window
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized
    }

    private void initClock() {
        clockTimer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            clockLabel.setText(sdf.format(new Date()));
        });
        clockTimer.start();
    }

    private void initStopwatch() {
        stopwatchTimer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                elapsedTime = currentTime - startTime;
                updateStopwatchLabel(elapsedTime);
            }
        });
    }

    private void updateStopwatchLabel(long time) {
        long hours = time / 3600000;
        long minutes = (time % 3600000) / 60000;
        long seconds = (time % 60000) / 1000;
        long millis = time % 1000;

        String formattedTime = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
        stopwatchLabel.setText(formattedTime);
    }

    private void startStopwatch() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime;
            stopwatchTimer.start();
            isRunning = true;
        }
    }

    private void stopStopwatch() {
        if (isRunning) {
            stopwatchTimer.stop();
            isRunning = false;
        }
    }

    private void resetStopwatch() {
        stopwatchTimer.stop();
        elapsedTime = 0;
        startTime = 0;
        isRunning = false;
        stopwatchLabel.setText("00:00:00.000");
        lapTimes.clear();
        lapTextArea.setText("");
    }

    private void recordLapTime() {
        if (isRunning) {
            String lapTime = stopwatchLabel.getText();
            lapTimes.add(lapTime);

            // Keep only the last 10 lap times (using queue to manage size)
            if (lapTimes.size() > 10) {
                lapTimes.remove();
            }

            // Update lap times display
            StringBuilder lapDisplay = new StringBuilder();
            int lapNumber = 1;
            for (String lap : lapTimes) {
                lapDisplay.append("Lap ").append(lapNumber++).append(": ").append(lap).append("\n");
            }
            lapTextArea.setText(lapDisplay.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StopWatch app = new StopWatch();
            app.setVisible(true);
        });
    }
}
