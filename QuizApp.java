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
                String question = JOptionPane.showInputDialog(QuizApp.this, "Enter a question:");
                
                if (question != null && !question.isEmpty()) {
                    String[] options = new String[4]; // Assuming 4 options
                    
                    for (int i = 0; i < options.length; i++) {
                        options[i] = JOptionPane.showInputDialog(QuizApp.this, "Enter option " + (i + 1) + ":");
                        
                        if (options[i] == null || options[i].isEmpty()) {
                            JOptionPane.showMessageDialog(QuizApp.this, "Option " + (i + 1) + " not entered or input canceled.");
                            return; // Exit the quiz creation
                        }
                    }
                    
                    String correctOption = (String) JOptionPane.showInputDialog(
                        QuizApp.this,
                        "Select the correct option:",
                        "Choose Correct Option",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0] // Default option
                    );
                    
                    if (correctOption != null) {
                        // You can save the question, options, and correctOption and perform further actions
                        // For now, let's just display the entered information as confirmation
                        StringBuilder result = new StringBuilder();
                        result.append("Question: ").append(question).append("\nOptions:\n");
                        for (String option : options) {
                            result.append(option).append("\n");
                        }
                        result.append("Correct Option: ").append(correctOption);
                        
                        JOptionPane.showMessageDialog(QuizApp.this, result.toString());
                    } else {
                        JOptionPane.showMessageDialog(QuizApp.this, "No correct option selected or selection canceled.");
                    }
                } else {
                    JOptionPane.showMessageDialog(QuizApp.this, "No question entered or input canceled.");
                }
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
