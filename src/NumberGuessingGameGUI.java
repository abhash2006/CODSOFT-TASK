import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NumberGuessingGameGUI extends JFrame {

    private int secretNumber;
    private int attempts;
    private final int maxAttempts = 7;
    private final int lowerBound = 1;
    private final int upperBound = 100;
    private int score = 0;
    private int roundsPlayed = 0;

    private JTextField guessField;
    private JLabel messageLabel;
    private JLabel attemptLabel;
    private JLabel scoreLabel;
    private JButton guessButton;

    public NumberGuessingGameGUI() {
        setTitle("🎮 Number Guessing Game");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250, 255, 250)); // Light background

        initGame();

        JLabel title = new JLabel("🎯 Guess the Number!", SwingConstants.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        title.setForeground(new Color(40, 40, 90));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        centerPanel.setBackground(new Color(240, 255, 255));

        messageLabel = new JLabel("🤔 I'm thinking of a number between " + lowerBound + " and " + upperBound, SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        attemptLabel = new JLabel("🔁 Attempts left: " + maxAttempts, SwingConstants.CENTER);
        attemptLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        scoreLabel = new JLabel("📊 Score: 0 | Rounds: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        guessField = new JTextField();
        guessField.setFont(new Font("SansSerif", Font.BOLD, 18));
        guessField.setHorizontalAlignment(JTextField.CENTER);

        guessButton = new JButton("🎲 Submit Guess");
        guessButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        guessButton.setBackground(new Color(173, 255, 47));
        guessButton.addActionListener(e -> handleGuess());

        centerPanel.add(messageLabel);
        centerPanel.add(attemptLabel);
        centerPanel.add(scoreLabel);
        centerPanel.add(guessField);
        centerPanel.add(guessButton);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void initGame() {
        Random random = new Random();
        do {
            secretNumber = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
        } while (secretNumber % 5 != 0); // Ensure divisible by 5
        attempts = 0;
    }

    private void handleGuess() {
        String input = guessField.getText().trim();

        if (!input.matches("\\d+")) {
            guessField.setText("");
            attempts++;
            updateAfterAttempt("❌ Invalid input. Still counted as an attempt.");
            return;
        }

        int guess = Integer.parseInt(input);
        attempts++;

        if (guess < lowerBound || guess > upperBound) {
            updateAfterAttempt("📏 Out of range! Counted as an attempt.");
            return;
        }

        if (guess % 5 != 0) {
            updateAfterAttempt("⚠️ Not divisible by 5. Still counted.");
            return;
        }

        if (guess == secretNumber) {
            score++;
            roundsPlayed++;
            JOptionPane.showMessageDialog(this,
                    "🏆 Correct! You guessed the number in " + attempts + " attempt(s)!",
                    "Winner",
                    JOptionPane.INFORMATION_MESSAGE);
            nextRound();
        } else if (attempts >= maxAttempts) {
            roundsPlayed++;
            JOptionPane.showMessageDialog(this,
                    "❌ You're out of attempts! The correct number was " + secretNumber,
                    "Game Over",
                    JOptionPane.ERROR_MESSAGE);
            nextRound();
        } else {
            String hint = guess < secretNumber ? "🔼 Too low!" : "🔽 Too high!";
            updateAfterAttempt(hint);
        }

        guessField.setText("");
    }

    private void updateAfterAttempt(String message) {
        messageLabel.setText(message);
        attemptLabel.setText("🔁 Attempts left: " + (maxAttempts - attempts));
    }

    private void nextRound() {
        int option = JOptionPane.showConfirmDialog(this, "🔄 Play another round?", "Continue?", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            initGame();
            updateUI();
        } else {
            endGame();
        }
    }

    private void updateUI() {
        messageLabel.setText("🤔 I'm thinking of a number between " + lowerBound + " and " + upperBound);
        attemptLabel.setText("🔁 Attempts left: " + maxAttempts);
        scoreLabel.setText("📊 Score: " + score + " | Rounds: " + roundsPlayed);
        guessField.setText("");
    }

    private void endGame() {
        JOptionPane.showMessageDialog(this,
                "🏁 Game Over!\n\n🏆 Final Score: " + score + " win(s) out of " + roundsPlayed + " round(s).\nThanks for playing!",
                "Game Finished",
                JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberGuessingGameGUI game = new NumberGuessingGameGUI();
            game.setVisible(true);
        });
    }
}
