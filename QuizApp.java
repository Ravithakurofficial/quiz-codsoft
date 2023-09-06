import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizApp extends JFrame {
    private JButton makeQuizButton;
    private JButton startQuizButton;

    public QuizApp() {
        setTitle("Quiz Application");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel to hold the buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10)); // 2 rows, 1 column, with spacing

        makeQuizButton = new JButton("Make Quiz");
        startQuizButton = new JButton("Start Quiz");

        // Add action listeners to the buttons
        makeQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add logic to create a quiz
                JOptionPane.showMessageDialog(QuizApp.this, "You selected 'Make Quiz'. Create your quiz here.");
            }
        });

        startQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add logic to start the quiz
                JOptionPane.showMessageDialog(QuizApp.this, "You selected 'Start Quiz'. Take the quiz here.");
            }
        });

        // Add the buttons to the panel
        panel.add(makeQuizButton);
        panel.add(startQuizButton);

        // Add the panel to the frame
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizApp();
            }
        });
    }
}
