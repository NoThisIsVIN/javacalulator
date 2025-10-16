import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {
    private final JTextField display = new JTextField("0");
    private String currentOperator = "";
    private double result = 0;
    private boolean startNewNumber = true;

    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(320, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(6, 6));

        display.setFont(new Font("SansSerif", Font.BOLD, 28));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        add(display, BorderLayout.NORTH);

        String[] buttons = {
            "C", "←", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "±", "0", ".", "="
        };

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 6, 6));
        for (String b : buttons) {
            JButton btn = new JButton(b);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 20));
            btn.addActionListener(this);
            panel.add(btn);
        }

        add(panel, BorderLayout.CENTER);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("0123456789".contains(cmd)) {
            appendDigit(cmd);
            return;
        }

        switch (cmd) {
            case ".":
                appendDot();
                break;
            case "C":
                clearAll();
                break;
            case "←":
                backspace();
                break;
            case "+": case "-": case "*": case "/":
                applyOperator(cmd);
                break;
            case "=":
                calculateResult();
                currentOperator = "";
                startNewNumber = true;
                break;
            case "%":
                percent();
                break;
            case "±":
                toggleSign();
                break;
        }
    }

    private void appendDigit(String d) {
        if (startNewNumber || display.getText().equals("0")) {
            display.setText(d);
            startNewNumber = false;
        } else {
            display.setText(display.getText() + d);
        }
    }

    private void appendDot() {
        if (startNewNumber) {
            display.setText("0.");
            startNewNumber = false;
            return;
        }
        if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }

    private void clearAll() {
        display.setText("0");
        result = 0;
        currentOperator = "";
        startNewNumber = true;
    }

    private void backspace() {
        String t = display.getText();
        if (startNewNumber || t.length() <= 1) {
            display.setText("0");
            startNewNumber = true;
        } else {
            display.setText(t.substring(0, t.length() - 1));
        }
    }

    private void applyOperator(String op) {
        try {
            double x = Double.parseDouble(display.getText());
            if (!currentOperator.isEmpty()) {
                result = compute(result, x, currentOperator);
            } else {
                result = x;
            }
            currentOperator = op;
            display.setText(formatNumber(result));
            startNewNumber = true;
        } catch (NumberFormatException ex) {
            display.setText("0");
            startNewNumber = true;
            currentOperator = "";
            result = 0;
        }
    }

    private void calculateResult() {
        if (currentOperator.isEmpty()) return;
        try {
            double x = Double.parseDouble(display.getText());
            result = compute(result, x, currentOperator);
            display.setText(formatNumber(result));
            currentOperator = "";
        } catch (NumberFormatException ex) {
            display.setText("0");
        }
    }

    private double compute(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) {
                    JOptionPane.showMessageDialog(this, "Cannot divide by zero", "Error", JOptionPane.ERROR_MESSAGE);
                    return 0;
                }
                return a / b;
            default: return b;
        }
    }

    private void percent() {
        try {
            double x = Double.parseDouble(display.getText());
            x = x / 100.0;
            display.setText(formatNumber(x));
            startNewNumber = true;
        } catch (NumberFormatException ex) {
            display.setText("0");
        }
    }

    private void toggleSign() {
        try {
            double x = Double.parseDouble(display.getText());
            x = -x;
            display.setText(formatNumber(x));
        } catch (NumberFormatException ex) {
            display.setText("0");
        }
    }

    private String formatNumber(double v) {
        if (v == (long) v) {
            return String.format("%d", (long) v);
        } else {
            return String.valueOf(v);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}
