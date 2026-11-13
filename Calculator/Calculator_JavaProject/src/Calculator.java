import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    JLabel displayJLabel = new JLabel();
    JPanel displayJPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    String A = "0";
    String operator = null;
    String B = null;

    String expression = "";
    String current = "0";
    boolean numberEntered = false;

    Calculator() {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        displayJLabel.setBackground(customBlack);
        displayJLabel.setForeground(Color.white);
        displayJLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayJLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayJLabel.setPreferredSize(new Dimension(boardWidth, 100));
        displayJLabel.setText("0");
        displayJLabel.setOpaque(true);

        displayJPanel.setLayout(new BorderLayout());
        displayJPanel.add(displayJLabel);
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
                                B = numberEntered ? current : "0";
                                String result = calculate(A, operator, B);
                                clearAll();
                                current = result;
                                refreshDisplay();
                            }
                        }
                        else {
                            if (operator == null) {
                                A = current;
                                expression = formatNumber(A) + buttonValue;
                            }
                            else if (numberEntered) {
                                B = current;
                                A = calculate(A, operator, B);
                                expression = expression + formatNumber(B) + buttonValue;
                            }
                            else {
                                expression = expression.substring(0, expression.length() - 1) + buttonValue;
                            }
                            operator = buttonValue;
                            current = "";
                            numberEntered = false;
                            refreshDisplay();
                        }
                    }
                    else if (Arrays.asList(topSymbols).contains(buttonValue)) {
                        if (buttonValue.equals("AC")) {
                            clearAll();
                            refreshDisplay();
                        }
                        else if (buttonValue.equals("+/-")) {
                            double numDisplay = Double.parseDouble(currentOrZero());
                            numDisplay *= -1;
                            current = removeZeroDecimal(numDisplay);
                            numberEntered = true;
                            refreshDisplay();
                        }
                        else if (buttonValue.equals("%")) {
                            double numDisplay = Double.parseDouble(currentOrZero());
                            numDisplay /= 100;
                            current = removeZeroDecimal(numDisplay);
                            numberEntered = true;
                            refreshDisplay();
                        }
                    }
                    else if (buttonValue.equals("√")) {
                        double num = Double.parseDouble(currentOrZero());
                        if (num < 0) {
                            clearAll();
                            setDisplay("Error");
                        }
                        else {
                            num = Math.sqrt(num);
                            current = removeZeroDecimal(num);
                            numberEntered = true;
                            refreshDisplay();
                        }
                    }
                    else {
                        if (buttonValue.equals(".")) {
                            if (!numberEntered) {
                                current = "0.";
                            }
                            else if (!current.contains(".")) {
                                current = current + ".";
                            }
                        }
                        else {
                            if (!numberEntered || current.equals("0")) {
                                current = buttonValue;
                            }
                            else {
                                current = current + buttonValue;
                            }
                        }
                        numberEntered = true;
                        refreshDisplay();
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
        expression = "";
        current = "0";
        numberEntered = false;
    }

    String currentOrZero() {
        if (current.isEmpty()) {
            return "0";
        }
        return current;
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

    void refreshDisplay() {
        if (expression.isEmpty()) {
            setDisplay(current);
        }
        else if (numberEntered) {
            setDisplay(expression + formatNumber(current));
        }
        else {
            setDisplay(expression);
        }
    }

    void setDisplay(String text) {
        int maxWidth = boardWidth - 40;
        int size = 80;
        Font font = new Font("Arial", Font.PLAIN, size);
        FontMetrics fm = displayJLabel.getFontMetrics(font);

        while (fm.stringWidth(text) > maxWidth && size > 24) {
            size -= 2;
            font = new Font("Arial", Font.PLAIN, size);
            fm = displayJLabel.getFontMetrics(font);
        }

        if (fm.stringWidth(text) > maxWidth) {
            String tail = text;
            while (fm.stringWidth("…" + tail) > maxWidth && tail.length() > 1) {
                tail = tail.substring(1);
            }
            text = "…" + tail;
        }

        displayJLabel.setFont(font);
        displayJLabel.setText(text);
    }

    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int)numDisplay);
        }
        return Double.toString(numDisplay);
    }
}