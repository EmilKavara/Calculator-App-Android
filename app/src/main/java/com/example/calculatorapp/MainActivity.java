package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // Variable to hold the current text in the calculator input field
    public String text;
    // Boolean flag to indicate whether the calculator can currently accept an operand (number)
    boolean canTakeOperand=false;
    boolean canWriteDot=false;
    // Variable to hold the result of the most recently evaluated math expression
    double res=0;
    // List to hold the order of math operations entered by the user
    ArrayList<String> numbersAndOperators =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setClickable(false);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        TextView textView=(TextView) findViewById(R.id.resultTextView);
        textView.setTextColor(Color.BLACK);
        // Creating buttons for numbers from 0-9
        for (int i = 0; i <= 9; i++) {
            // Get the ID of the button corresponding to the current number
            int buttonId = getResources().getIdentifier("appCompatButton" + i, "id", getPackageName());
            // Get a reference to the button view
            Button button = findViewById(buttonId);
            // Set an OnClickListener on the button
            button.setOnClickListener(this);
        }
        // Get references to all the other calculator buttons and set OnClickListeners on them
        Button deleteButton= (Button) findViewById(R.id.appCompatButtonDelete);
        Button AC_Button= (Button) findViewById(R.id.appCompatButtonAC);
        Button addition= (Button) findViewById(R.id.appCompatButtonAddition);
        Button subtraction= (Button) findViewById(R.id.appCompatButtonSubtraction);
        Button division= (Button) findViewById(R.id.appCompatButtonDivide);
        Button multiply= (Button) findViewById(R.id.appCompatButtonMultiply);
        Button equals= (Button) findViewById(R.id.appCompatButtonEquals);
        Button modulus= (Button) findViewById(R.id.appCompatButtonModulus);
        Button dot= (Button) findViewById(R.id.appCompatButtonDot);
        addition.setOnClickListener(this);
        subtraction.setOnClickListener(this);
        division.setOnClickListener(this);
        multiply.setOnClickListener(this);
        equals.setOnClickListener(this);
        modulus.setOnClickListener(this);
        AC_Button.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        dot.setOnClickListener(this);
    }

    // Called when a calculator button is clicked
    public void onClick (View v){
        switch (v.getId()) {
            // If the button clicked is a number button, add the number to the input field
            // Handle number buttons
            case R.id.appCompatButton0:
            case R.id.appCompatButton1:
            case R.id.appCompatButton2:
            case R.id.appCompatButton3:
            case R.id.appCompatButton4:
            case R.id.appCompatButton5:
            case R.id.appCompatButton6:
            case R.id.appCompatButton7:
            case R.id.appCompatButton8:
            case R.id.appCompatButton9:
                String number = ((Button) v).getText().toString();
                addText(number);
                canTakeOperand = true;
                canWriteDot = true;
                break;
            case R.id.appCompatButtonDelete:
                removeText(1);
                break;
            case R.id.appCompatButtonAC:
                removeText(0);
                break;
            case R.id.appCompatButtonAddition:
                if(canTakeOperand){
                    addText(" + ");
                    numbersAndOperators.add("+");
                    canTakeOperand=false;
                    canWriteDot=false;
                }
                break;
            case R.id.appCompatButtonSubtraction:
                if(canTakeOperand){
                    addText(" - ");
                    numbersAndOperators.add("-");
                    canTakeOperand=false;
                    canWriteDot=false;
                }
                break;
            case R.id.appCompatButtonDivide:
                if(canTakeOperand){
                    addText(" / ");
                    numbersAndOperators.add("/");
                    canTakeOperand=false;
                    canWriteDot=false;
                }
                break;
            case R.id.appCompatButtonMultiply:
                if(canTakeOperand){
                    addText(" * ");
                    numbersAndOperators.add("*");
                    canTakeOperand=false;
                    canWriteDot=false;
                }
                break;
            case R.id.appCompatButtonModulus:
                if(canTakeOperand){
                    addText(" % ");
                    percent();
                    canTakeOperand=false;
                    canWriteDot=false;
                }
                break;
            case R.id.appCompatButtonEquals:
                mathOperations();
                numbersAndOperators.clear();
                break;
            case R.id.appCompatButtonDot:
                if(canWriteDot){
                    addText(".");
                    canWriteDot=false;
                    canTakeOperand=false;
                }
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }
    // Displaying numbers and operators that user entered by adding them to the Text View
    void addText(String textToAdd){
        EditText editText = (EditText) findViewById(R.id.editText);
        TextView textView=(TextView) findViewById(R.id.resultTextView);
        if (textToAdd.equals(".")) {
            if (text.contains(".")) {
                // Dot already exists, ignore the input
                return;
            } else {
                canWriteDot = false;
            }
        }
        if(textView.getText().toString().isEmpty()) {
            text = editText.getText().toString();
            text = text + textToAdd;
            editText.setText(text);
        }else{
            textView.setText("");
            String stringRes=res+"";
            if (stringRes.endsWith(".0")) {
                stringRes = stringRes.substring(0, stringRes.length() - 2);
                editText.setText(stringRes+""+textToAdd);
            }else{
                editText.setText(res+""+textToAdd);
            }
        }
    }
    // This function calculates the percentage of the first number entered by the user and displays the result in the "result" TextView
    void percent(){
        EditText editText = (EditText) findViewById(R.id.editText);
        String [] numbers=editText.getText().toString().split(" ");
        double result=Double.parseDouble(numbers[0])/100;
        TextView textView=(TextView) findViewById(R.id.resultTextView);
        textView.setText(result+"");
    }

    // Number 1 is for removing one character and 0 is for deleting all data
    void removeText(int num){
        if(num==1){
            EditText editText = (EditText) findViewById(R.id.editText);
            text=editText.getText().toString();
            if(text.length()>=2){
                text=text.substring(0,text.length()-1);
                // Removes Element and Whitespace(only from left side of string)
                text = text.replaceAll("\\s+$", "");
            }else{
                text="";
            }
            editText.setText(text);
        } else if(num==0){
            EditText editText = (EditText) findViewById(R.id.editText);
            editText.setText("");
            TextView textView=(TextView) findViewById(R.id.resultTextView);
            textView.setText("");
            numbersAndOperators.clear();
        }else{
            throw new RuntimeException("Unknown Operation");
        }
    }
    void mathOperations() {
        TextView resultTextView = findViewById(R.id.resultTextView);
        EditText numbersEditText = findViewById(R.id.editText);
        if(!numbersEditText.equals("")) {
            String inputExpression = numbersEditText.getText().toString();
            String finalResult = evaluateExpression(inputExpression);
            resultTextView.setText(finalResult);
        }
    }
    // Evaluating expession by order (PEMDAS)
    String evaluateExpression(String expression) {
        String[] expressionArray = expression.split(" ");
        double result = 0;
        Stack<Double> operandStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        for (String value : expressionArray) {
            // Checking if value is operator or operand
            if (value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/")) {
                while (!operatorStack.isEmpty() && hasPrecedence(value, operatorStack.peek())) {
                    double operand2 = operandStack.pop();
                    double operand1 = operandStack.pop();
                    String operator = operatorStack.pop();
                    double partialResult = performOperation(operand1, operand2, operator);
                    operandStack.push(partialResult);
                }
                operatorStack.push(value);
            } else {
                double operand = Double.parseDouble(value);
                operandStack.push(operand);
            }
        }

        while (!operatorStack.isEmpty()) {
            double operand2 = operandStack.pop();
            double operand1 = operandStack.pop();
            String operator = operatorStack.pop();
            double partialResult = performOperation(operand1, operand2, operator);
            operandStack.push(partialResult);
        }

        if (!operandStack.isEmpty()) {
            result = operandStack.pop();
        }
        // Formatting decimal number
        DecimalFormat df = new DecimalFormat("#.###");
        res=Double.parseDouble(df.format(result));
        return df.format(result);
    }
    // Checking if operator have precedence
    boolean hasPrecedence(String operator1, String operator2) {
        if ((operator2.equals("*") || operator2.equals("/")) && (operator1.equals("+") || operator1.equals("-"))) {
            return true;
        }
        return false;
    }
    // Function where operations are performed
    // Returns result with type double
    double performOperation(double operand1, double operand2, String operator) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                if (operand2 == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }



}