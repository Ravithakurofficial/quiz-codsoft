import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quiz extends JFrame {
    private JButton startQuizButton;
    private List<Question> defaultQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean quizInProgress = false;
    private Map<Question, String> userResponses = new HashMap<>(); // Initialize userResponses here

    private JButton makeQuizButton;
    private Timer timer;
    private JLabel timerLabel;
    private int timeRemaining;
    private JLabel answerLabel;
    public Quiz() {
        setTitle("Quiz Application");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1, 10, 10));

        startQuizButton = new JButton("Start Quiz");

        addDefaultQuestions();

        makeQuizButton = new JButton("Make Quiz");
        makeQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!quizInProgress) {
                    String numQuestionsStr = JOptionPane.showInputDialog(Quiz.this, "Enter the number of questions to create:");
                    try {
                        int numQuestions = Integer.parseInt(numQuestionsStr);
                        createCustomQuiz(numQuestions);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(Quiz.this, "Invalid input. Please enter a valid number.");
                    }
                } else {
                    JOptionPane.showMessageDialog(Quiz.this, "A quiz is already in progress. Finish the current quiz or click Start Quiz.");
                }
            }
        });

        startQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!quizInProgress) {
                    if (defaultQuestions.size() >= 1) {
                        currentQuestionIndex = 0;
                        score = 0;
                        userResponses = new HashMap<>();
                        startQuiz();
                        quizInProgress = true;
                        startTimer();
                    } else {
                        JOptionPane.showMessageDialog(Quiz.this, "No questions available to start the quiz.");
                    }
                } else {
                    JOptionPane.showMessageDialog(Quiz.this, "A quiz is already in progress. Finish the current quiz or click Start Quiz.");
                }
            }
        });
        topPanel.add(makeQuizButton);
        topPanel.add(startQuizButton);

        timerLabel = new JLabel("Time Remaining: 0 seconds");
        topPanel.add(timerLabel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        answerLabel = new JLabel(""); // Initialize the answerLabel
        answerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(answerLabel, BorderLayout.CENTER);

        add(mainPanel);

        setVisible(true);
    }

    private void createCustomQuiz(int numQuestions) {
        List<Question> customQuestions = new ArrayList<>();

        for (int i = 0; i < numQuestions; i++) {
            String questionStr = JOptionPane.showInputDialog(Quiz.this, "Enter question #" + (i + 1) + ":");
            String[] options = new String[4];
            for (int j = 0; j < 4; j++) {
                options[j] = JOptionPane.showInputDialog(Quiz.this, "Enter option " + (char) ('a' + j) + ":");
            }
            String correctOption = JOptionPane.showInputDialog(Quiz.this, "Enter the correct option (e.g., 'a', 'b', 'c', or 'd'):");

            customQuestions.add(new Question(questionStr, options, correctOption));
        }

        defaultQuestions.clear();
        defaultQuestions.addAll(customQuestions);
        JOptionPane.showMessageDialog(Quiz.this, "Custom quiz created with " + numQuestions + " questions.");
    }

    private void startQuiz() {
        userResponses = new HashMap<>(); // Initialize the userResponses map
        if (currentQuestionIndex < defaultQuestions.size()) {
            displayQuestion(defaultQuestions.get(currentQuestionIndex));
        } else {
            JOptionPane.showMessageDialog(Quiz.this, "No questions available to start the quiz.");
        }
    }

    private void displayQuestion(Question question) {
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(6, 1));

        JLabel questionLabel = new JLabel(question.getQuestion());
        questionPanel.add(questionLabel);

        ButtonGroup optionGroup = new ButtonGroup();
        JRadioButton[] optionButtons = new JRadioButton[4];

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton(question.getOptions()[i]);
            optionGroup.add(optionButtons[i]);
            questionPanel.add(optionButtons[i]);
        }

        JButton submitButton = new JButton("Submit");
        questionPanel.add(submitButton);

        getContentPane().removeAll();
        getContentPane().add(questionPanel);
        revalidate();
        repaint();

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = "";
                for (int i = 0; i < 4; i++) {
                    if (optionButtons[i].isSelected()) {
                        selectedOption = optionButtons[i].getText();
                        break;
                    }
                }

                userResponses.put(question, selectedOption);

                if (currentQuestionIndex < defaultQuestions.size() - 1) {
                    // If there are more questions, proceed to the next question
                    currentQuestionIndex++;
                    displayQuestion(defaultQuestions.get(currentQuestionIndex));
                    resetTimer();
                } else {
                    // If this was the last question, show the quiz results
                    showQuizResults();
                }
            }
        });
    }

    private void showQuizResults() {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout());
    
        StringBuilder resultMessage = new StringBuilder("<html>Quiz Results:<br>");
    
        for (Question question : defaultQuestions) {
            String userResponse = userResponses.get(question);
            String correctAnswer = question.getCorrectOption();
            boolean isCorrect = userResponse != null && userResponse.equals(correctAnswer);
    
            resultMessage.append("<br>").append(question.getQuestion()).append("<br>");
            resultMessage.append("Your Answer: ").append(userResponse).append("<br>");
            resultMessage.append("Correct Answer: ").append(correctAnswer).append("<br>");
            resultMessage.append("Result: ").append(isCorrect ? "Correct" : "Incorrect").append("<br>");
            resultMessage.append("-------------------------");
    
            if (isCorrect) {
                score++;
            }
        }
    
        resultMessage.append("<br>Overall Score: ").append(score).append(" out of ").append(defaultQuestions.size()).append("</html>");
    
        JLabel resultsLabel = new JLabel(resultMessage.toString());
        resultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultsPanel.add(resultsLabel, BorderLayout.CENTER);
    
        getContentPane().removeAll();
        getContentPane().add(resultsPanel);
        revalidate();
        repaint();
    
        quizInProgress = false;
        stopTimer();
    }
    

    private void addDefaultQuestions() {
        defaultQuestions.add(new Question("The term 'Computer' is derived from..........", new String[]{"a. Latin", "b. German", "c. French", "d. Arabic"}, "a. Latin"));
        defaultQuestions.add(new Question("Who is the inventor of 'Difference Engine'?", new String[]{"a. Allen Turing", "b. Charles Babbage", "c. Simur Cray", "d. Augusta Adaming"}, "b. Charles Babbage"));
        defaultQuestions.add(new Question("Who is the father of Computer?", new String[]{"a. Allen Turing", "b. Charles Babbage", "c. Simur Cray", "d. Augusta Adaming"}, "b. Charles Babbage"));
        defaultQuestions.add(new Question("Who is the father of Computer science?", new String[]{"a. Allen Turing", "b. Charles Babbage", "c. Simur Cray", "d. Augusta Adaming"}, "a. Allen Turing"));
        defaultQuestions.add(new Question("Who is the father of the personal computer?", new String[]{"a. Edward Robert", "b. Allen Turing", "c. Charles Babbage", "d. None of these"}, "a. Edward Robert"));
    }

    private void startTimer() {
        timeRemaining = 30; // Set the initial time limit for each question (adjust as needed)
        timerLabel.setText("Time Remaining: " + timeRemaining + " seconds");

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;

                // Update the timer label with the remaining time
                timerLabel.setText("Time Remaining: " + timeRemaining + " seconds");

                if (timeRemaining <= 0) {
                    // Time's up! Proceed to the next question or show results
                    timer.stop();
                    JOptionPane.showMessageDialog(Quiz.this, "Time's up!");
                    currentQuestionIndex++;
                    if (currentQuestionIndex < defaultQuestions.size()) {
                        displayQuestion(defaultQuestions.get(currentQuestionIndex));
                        resetTimer();
                    } else {
                        showQuizResults();
                    }
                }
            }
        });
        timer.setInitialDelay(0); // Start the timer immediately when displaying a new question
        timer.start();
    }

    private void resetTimer() {
        timeRemaining = 30; // Reset the timer for each question
        timerLabel.setText("Time Remaining: " + timeRemaining + " seconds");
        timer.restart();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Quiz();
            }
        });
    }
}

class Question {
    private String question;
    private String[] options;
    private String correctOption;

    public Question(String question, String[] options, String correctOption) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectOption() {
        return correctOption;
    }
}
