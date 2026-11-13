import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Calculator {
    int boardWidth = 360;
    int boardHeight = 540;
    
    Color customLightGray = new Color(212, 212, 210);
    Color customDarkGray = new Color(80, 80, 80);
    Color customBlack = new Color(28, 28, 28);
    Color customSunglow = new Color(255, 209, 55);

    String[] buttonValues = {
        "AC", "+/-", "%", "÷", 
        "7", "8", "9", "×", 
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "√", "="
    };
    String[] rightSymbols = {"÷", "×", "-", "+", "="};
    String[] topSymbols = {"AC", "+/-", "%"};

    JFrame frame = new JFrame("Calculator");
    JLabel historyJLabel = new JLabel();
    JLabel displayJLabel = new JLabel();
    JPanel displayJPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    String A = "0";
    String operator = null;
    String B = null;

    String history = "";          
    boolean numberEntered = false; 

    Calculator() {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        historyJLabel.setBackground(customBlack);
        historyJLabel.setForeground(customLightGray);
        historyJLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        historyJLabel.setHorizontalAlignment(JLabel.RIGHT);
        historyJLabel.setBorder(new EmptyBorder(0, 0, 0, 12));
        historyJLabel.setPreferredSize(new Dimension(boardWidth, 36));
        historyJLabel.setOpaque(true);

        displayJLabel.setBackground(customBlack);
        displayJLabel.setForeground(Color.white);
        displayJLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayJLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayJLabel.setText("0");
        displayJLabel.setOpaque(true);

        displayJPanel.setLayout(new BorderLayout());
        displayJPanel.add(historyJLabel, BorderLayout.NORTH);
        displayJPanel.add(displayJLabel, BorderLayout.CENTER);
        frame.add(displayJPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(5,4));
        buttonsPanel.setBackground(customBlack);
        frame.add(buttonsPanel);

        for (int i = 0; i < buttonValues.length; i++) {
            JButton button = new JButton();
            String buttonValue = buttonValues[i];
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setText(buttonValue);
            button.setFocusable(false);
            button.setBorder(new LineBorder(customBlack));
            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                button.setBackground(customLightGray);
                button.setForeground(customBlack);
            }
            else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                button.setBackground(customSunglow);
                button.setForeground(Color.white);
            }
            else {
                button.setBackground(customDarkGray);
                button.setForeground(Color.white);
            }

            buttonsPanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    String buttonValue = button.getText();

                    if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                        if (buttonValue.equals("=")) {
                            if (operator != null) {
                                B = displayJLabel.getText();
                                String result = calculate(A, operator, B);
                                showHistory(history + formatNumber(B) + "=");
                                displayJLabel.setText(result);
                                clearAll();
                            }
                        }
                        else {
                            if (operator == null) {
                                // first operator: start the history
                                A = displayJLabel.getText();
                                history = formatNumber(A) + buttonValue;
                            }
                            else if (numberEntered) {
                                // chaining: work out 7-2 first, then keep going
                                B = displayJLabel.getText();
                                A = calculate(A, operator, B);
                                history = history + formatNumber(B) + buttonValue;
                            }
                            else {
                                // no new number typed, so just swap the operator
                                history = history.substring(0, history.length() - 1) + buttonValue;
                            }
                            operator = buttonValue;
                            displayJLabel.setText("0");
                            numberEntered = false;
                            showHistory(history);
                        }
                    }
                    else if (Arrays.asList(topSymbols).contains(buttonValue)) {
                        if (buttonValue.equals("AC")) {
                            clearAll();
                            displayJLabel.setText("0");
                            showHistory("");
                        }
                        else if (buttonValue.equals("+/-")) {
                            double numDisplay = Double.parseDouble(displayJLabel.getText());
                            numDisplay *= -1;
                            displayJLabel.setText(removeZeroDecimal(numDisplay));
                            numberEntered = true;
                        }
                        else if (buttonValue.equals("%")) {
                            double numDisplay = Double.parseDouble(displayJLabel.getText());
                            numDisplay /= 100;
                            displayJLabel.setText(removeZeroDecimal(numDisplay));
                            numberEntered = true;
                        }
                    }
                    else if (buttonValue.equals("√")) {
                        double num = Double.parseDouble(displayJLabel.getText());
                        if (num < 0) {
                            displayJLabel.setText("Error");
                        }
                        else {
                            num = Math.sqrt(num);
                            displayJLabel.setText(removeZeroDecimal(num));
                            numberEntered = true;
                        }
                    }
                    else {
                        // digits and the decimal point
                        if (operator == null && !numberEntered) {
                            showHistory(""); // new calculation, clear the old expression
                        }
                        if (buttonValue.equals(".")) {
                            if (!numberEntered) {
                                displayJLabel.setText("0.");
                            }
                            else if (!displayJLabel.getText().contains(".")) {
                                displayJLabel.setText(displayJLabel.getText() + ".");
                            }
                        }
                        else {
                            if (!numberEntered || displayJLabel.getText().equals("0")) {
                                displayJLabel.setText(buttonValue);
                            }
                            else {
                                displayJLabel.setText(displayJLabel.getText() + buttonValue);
                            }
                        }
                        numberEntered = true;
                    }
                }
            });
            frame.setVisible(true);
        }
    }

    void clearAll() {
        A = "0";
        operator = null;
        B = null;
        history = "";
        numberEntered = false;
    }

    String calculate(String x, String op, String y) {
        double numA = Double.parseDouble(x);
        double numB = Double.parseDouble(y);
        double result = 0;

        if (op.equals("+")) {
            result = numA + numB;
        }
        else if (op.equals("-")) {
            result = numA - numB;
        }
        else if (op.equals("×")) {
            result = numA * numB;
        }
        else if (op.equals("÷")) {
            result = numA / numB;
        }
        return removeZeroDecimal(result);
    }

    String formatNumber(String num) {
        if (num.startsWith("-")) {
            return "(" + num + ")";
        }
        return num;
    }

    void showHistory(String text) {
        int maxLength = 28;
        if (text.length() > maxLength) {
            text = "…" + text.substring(text.length() - (maxLength - 1));
        }
        historyJLabel.setText(text);
    }

    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int)numDisplay);
        }
        return Double.toString(numDisplay);
    }
}