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
                        if (buttonValue == "=") {
                            if (buttonValue == "=") {
                                B = displayJLabel.getText();
                                double numA = Double.parseDouble(A);
                                double numB = Double.parseDouble(B);

                                if (operator == "+") {
                                    displayJLabel.setText(removeZeroDecimal(numA+numB));
                                }
                                else if (operator == "-") {
                                    displayJLabel.setText(removeZeroDecimal(numA-numB)); 
                                }
                                else if (operator == "×") {
                                    displayJLabel.setText(removeZeroDecimal(numA*numB)); 
                                }
                                else if (operator == "÷") {
                                    displayJLabel.setText(removeZeroDecimal(numA/numB)); 
                                }
                                clearAll();
                            }
                        }
                        else if ("+-×÷".contains(buttonValue)) {
                            if (operator == null) {
                                A = displayJLabel.getText();
                                displayJLabel.setText("0");
                                B = "0";
                            }
                            operator = buttonValue;
                        }
                    }
                    else if (Arrays.asList(topSymbols).contains(buttonValue)) {
                        if (buttonValue.equals("AC")) {
                            clearAll();
                            displayJLabel.setText("0");
                        }
                        else if (buttonValue.equals("+/-")) {
                            double numDisplay = Double.parseDouble(displayJLabel.getText());
                            numDisplay *= -1;
                            displayJLabel.setText(removeZeroDecimal(numDisplay));
                        }
                        else if (buttonValue.equals("%")) {
                            double numDisplay = Double.parseDouble(displayJLabel.getText());
                            numDisplay /= 100;
                            displayJLabel.setText(removeZeroDecimal(numDisplay));
                        }
                        else if (buttonValue.equals(".")) {
                            if (!displayJLabel.getText().contains(".")) {
                                displayJLabel.setText(displayJLabel.getText() + ".");
                            }
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
                        }
                    }
                    else{
                        if (buttonValue == ".") {
                            if (!displayJLabel.getText().contains(buttonValue)){
                                displayJLabel.setText(displayJLabel.getText()+buttonValue);
                            }

                        }
                        else if ("0123456789".contains (buttonValue)) {
                            if (displayJLabel.getText() == "0"){
                                displayJLabel.setText(buttonValue);
                            }
                            else {
                                displayJLabel.setText(displayJLabel.getText()+buttonValue);
                            }
                        }
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
    }

    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int)numDisplay);
        }
        return Double.toString(numDisplay);
    }
}