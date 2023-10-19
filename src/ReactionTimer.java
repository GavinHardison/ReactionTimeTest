import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Random;
import javax.swing.*;

public class ReactionTimer implements ActionListener {

    private JFrame frame;
    private JButton startButton;
    private JButton reactButton;
    private JLabel average;
    private Long startTime;
    private Long stopTime;
    private int sum = 0;
    private int numberOfRuns = 0;
    private ReactionStates activeState = ReactionStates.STARTING;

    ReactionTimer() {
        frame = new JFrame("Reaction Time Test");
        startButton = new JButton("Press to start Reaction Time Test");
        reactButton = new JButton();
        average = new JLabel("Average Reaction Time: 0 ms");

        frame.setSize(500, 250);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(startButton);
        frame.add(reactButton);
        frame.add(average);

        startButton.setBounds(115, 10, 250, 30);
        startButton.addActionListener(this);

        reactButton.setBounds(40, 50, 400, 100);
        reactButton.addActionListener(this);
        reactButton.setBackground(Color.RED);
        reactButton.setOpaque(true);

        average.setBounds(10, 160, 250, 30);
    }

    public static void main(String[] args) throws InterruptedException {
        new ReactionTimer();
    }

    public static void waitingPeriod() {
        Random rand = new Random();
        try {
            Thread.sleep(rand.nextInt(5000 - 2000 + 1) + 2000);
        } catch (InterruptedException ex) {
            System.out.println("Interrupted Exception");
        }
    }

    public void changeTextAndColor(String text, Color backgroundColor, Color textColor) {
        reactButton.setText(text);
        reactButton.setBackground(backgroundColor);
        reactButton.setForeground(textColor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            if (!activeState.canChangeReactionState(ReactionStates.WAITING)) {
                return;
            }
            activeState = ReactionStates.WAITING;
            this.changeTextAndColor("Waiting...", Color.YELLOW, Color.BLACK);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    waitingPeriod();
                    if (!activeState.canChangeReactionState(ReactionStates.REACTING)) {
                        return;
                    }
                    activeState = ReactionStates.REACTING;
                    if (!activeState.equals(ReactionStates.REACTING)) {
                        return;
                    }
                    ReactionTimer.this.changeTextAndColor("Click!", Color.GREEN, Color.BLACK);
                    startTime = System.currentTimeMillis();
                }
            }).start();
        } else if (e.getSource() == reactButton) {
            if (!activeState.canChangeReactionState(ReactionStates.REACTED)) {
                if (!activeState.canChangeReactionState(ReactionStates.ERROR)) {
                    return;
                }
                activeState = ReactionStates.ERROR;
                this.changeTextAndColor("Error: Clicked too early", Color.RED, Color.WHITE);
                return;
            }
            activeState = ReactionStates.REACTED;
            stopTime = System.currentTimeMillis();
            long reactionTime = stopTime - startTime;
            startTime = null;
            stopTime = null;
            this.changeTextAndColor("Reaction time: " + reactionTime + " ms", Color.CYAN, Color.BLACK);
            numberOfRuns++;
            sum += reactionTime;
            double averageReaction = ((double)sum) / numberOfRuns;
            average.setText("Average Reaction Time: " + new DecimalFormat("###.00").format(averageReaction) + " ms");
        }
    }

}
