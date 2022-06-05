package edu.csc413.calculator.evaluator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EvaluatorUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private TextField expressionTextField = new TextField();
    private Panel buttonPanel = new Panel();

    // total of 20 buttons on the calculator,
    // numbered from left to right, top to bottom
    // bText[] array contains the text for corresponding buttons
    private static final String[] buttonText = {
        "7", "8", "9", "+", "4", "5", "6", "- ", "1", "2", "3",
        "*", "0", "^", "=", "/", "(", ")", "C", "CE"
    };

    /**
     * C  is for clear, clears entire expression
     * CE is for clear expression, clears last entry up until the last operator.
     */
    private Button[] buttons = new Button[buttonText.length];
    private String expression="";
    public static void main(String argv[]) {
        new EvaluatorUI();
    }

    public EvaluatorUI() {
        setLayout(new BorderLayout());
        this.expressionTextField.setPreferredSize(new Dimension(600, 50));
        this.expressionTextField.setFont(new Font("Courier", Font.BOLD, 28));

        add(expressionTextField, BorderLayout.NORTH);
        expressionTextField.setEditable(false);

        add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setLayout(new GridLayout(5, 4));

        //create 20 buttons with corresponding text in bText[] array
        Button tempButtonReference;
        for (int i = 0; i < EvaluatorUI.buttonText.length; i++) {
            tempButtonReference = new Button(buttonText[i]);
            tempButtonReference.setFont(new Font("Courier", Font.BOLD, 28));
            buttons[i] = tempButtonReference;
        }

        //add buttons to button panel
        for (int i = 0; i < EvaluatorUI.buttonText.length; i++) {
            buttonPanel.add(buttons[i]);
        }

        //set up buttons to listen for mouse input
        for (int i = 0; i < EvaluatorUI.buttonText.length; i++) {
            buttons[i].addActionListener(this);
        }

        setTitle("Calculator");
        setSize(400, 400);
        setLocationByPlatform(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * This function is triggered anytime a button is pressed
     * on our Calculator GUI.
     * @param actionEventObject Event object generated when a
     *                    button is pressed.
     */
    public void actionPerformed(ActionEvent actionEventObject) {
        String command = actionEventObject.getActionCommand();
        if (command.equals("C")) {
            this.expressionTextField.setText("0");
            expression="";
        }
        else
            this.expressionTextField.setText(command);
        //System.out.println("Text value "+ this.expressionTextField.getText());
        int result =0 ;

        Evaluator e = new Evaluator();

        try {
            if(!command.equals("=")) {
                expression = expression + this.expressionTextField.getText();
                System.out.println("In if: "+expression);
            } else if(command.equals("=")){
                expression = expression.replace("=", "");
                result = e.evaluateExpression(expression);
                System.out.println("expression is "+expression);
                System.out.println("result is: "+result);
                this.expressionTextField.setText(expression+"="+result);
            } else if (command.equals("C")) {
                System.out.println("C validation ");
                this.expressionTextField.setText("0");
            } else if (command.equals("CE")){
                expression = expression.replace("CE", " ");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
