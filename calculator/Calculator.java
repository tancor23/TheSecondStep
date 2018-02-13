package GPSolutions.calculator;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Calculator {

    private String str;

    public Calculator(String str) {
        this.str = str;
    }

    public void checkExpression() throws CalculatorException {
        if (str.matches(".*[^\\d\\^\\(\\)\\s\\*\\/\\+\\-\\.,].*")) {
            throw new CalculatorException("Invalid data. The expression contains unsupported symbol. Please enter correct expression");
        }
        if (str.matches(".*(\\d+)\\s+(\\d+).*")) {
            throw new CalculatorException("Invalid data. Number can't contain spaces. Please enter correct expression");
        }
        if (str.matches(".*[^\\)\\d]") || str.matches(".*[\\^\\*\\/\\+]{2,}.*")) {
            throw new CalculatorException("Invalid data. Please enter correct expression");
        }
        if (str.matches(".*(\\d+)[\\.,]+(\\d*)[\\.,]+.*")) {
            throw new CalculatorException("Invalid data. In one number several separators of the whole and fractional parts. Please enter correct expression");
        }
        if (str.matches(".*\\b" + "\\)\\(" + "\\b.*")) {
            throw new CalculatorException("Invalid data. There is no mathematical sign between ) and (");
        }
        char[] ch = str.toCharArray();
        int chLenght = ch.length, x = 0, y = 0;
        for (int i = 0; i < chLenght; i++) {
            if (str.charAt(i) == '(') {
                x++;
            }
            if (str.charAt(i) == ')') {
                y++;
            }
        }
        if (x != y) {
            throw new CalculatorException("Invalid data. The number of open parentheses isn't equal to the number of closed parentheses. Please enter correct expression");
        }

        str = str.replaceAll(" ", "");
        str = str.replaceAll(",", ".");
        doCalculate();
    }

    public void doCalculate() {

        StringBuilder result;
        try {
            result = reverseString(str);
            System.out.println(result);
            if (doCalculate(result) == 1.0f / 0 || doCalculate(result) == -1.0f / 0) {
                System.out.println("Invalid data. Division by zero");
                System.exit(0);
            }
            System.out.println(doCalculate(result));
        } catch (EmptyStackException e) {
            System.out.println("Invalid data. Incorrect expression!");
            System.exit(0);
        } catch (NumberFormatException n) {
            System.out.println("Invalid data. Incorrect expression");
            System.exit(0);
        }

    }

    public static StringBuilder reverseString(String string) {

        Stack<Character> stack = new Stack<>();
        StringBuilder resultNumb = new StringBuilder();
        StringBuilder resultString = new StringBuilder();
        Float fl;

        char[] ch = string.toCharArray();
        for (int i = 0; i < ch.length; i++) {

            if (ch[i] == '-' & i == 0) {
                resultNumb.append(ch[i]);
                i++;
            }
            Pattern firstPattern = Pattern.compile("[0-9]|\\.");
            Matcher firstMatcher = firstPattern.matcher(ch[i] + "");
            if (firstMatcher.matches()) {
                resultNumb.append(ch[i] + "");
            }

            Pattern secondPattern = Pattern.compile("\\*|\\/|\\+|\\-|\\^");
            Matcher secondMatcher = secondPattern.matcher(ch[i] + "");
            if (secondMatcher.matches()) {

                if (resultNumb == null || resultNumb.toString().equals("")) {
                } else {
                    fl = Float.parseFloat(String.valueOf(resultNumb));
                    resultString.append(fl + " ");
                    resultNumb.setLength(0);
                }
                boolean b = true;
                while (b) {
                    if (!stack.isEmpty()) {
                        if (getPriority(ch[i]) <= getPriority(stack.peek())) {
                            resultString.append(stack.pop() + " ");
                        } else {
                            stack.push(ch[i]);
                            break;
                        }
                    } else {
                        stack.push(ch[i]);
                        break;
                    }
                    if (getPriority(ch[i]) > getPriority(stack.peek())) {
                        stack.push(ch[i]);
                        break;
                    }
                }
                if (ch[i + 1] == '-') {
                    resultNumb.append(ch[i + 1]);
                    i++;
                }
            }

            Pattern thirdPattern = Pattern.compile("\\(");
            Matcher thirdMatcher = thirdPattern.matcher(ch[i] + "");
            if (thirdMatcher.matches()) {
                stack.push(ch[i]);
            }

            Pattern fourthPattern = Pattern.compile("\\)");
            Matcher fourthMatcher = fourthPattern.matcher(ch[i] + "");
            if (fourthMatcher.matches()) {
                fl = Float.parseFloat(String.valueOf(resultNumb));
                resultString.append(fl + " ");
                resultNumb.setLength(0);
                boolean d = true;
                while (d) {
                    if (getPriority(stack.peek()) > 1) {
                        resultString.append(stack.pop() + " ");
                    }
                    if (getPriority(stack.peek()) == 1) {
                        stack.pop();
                        break;
                    }
                }
            }
        }
        while (!stack.empty()) {
            if (resultNumb == null || resultNumb.toString().equals("")) {

            } else {
                fl = Float.parseFloat(String.valueOf(resultNumb));
                resultString.append(fl + " ");
                resultNumb.setLength(0);
            }
            resultString.append(stack.pop() + " ");
        }
        return resultString;
    }

    public static int getPriority(char ch) {
        int priority = 0;
        switch (ch) {
            case '(':
                priority = 1;
                break;
            case '+':
                priority = 2;
                break;
            case '-':
                priority = 2;
                break;
            case '*':
                priority = 3;
                break;
            case '/':
                priority = 3;
                break;
            case '^':
                priority = 4;
                break;
        }
        return priority;
    }

    public static float doCalculate(StringBuilder stringBuilder) {
        float result;

        Stack<Float> floatStack = new Stack<>();


        String str = String.valueOf(stringBuilder);
        String[] mas = str.split(" ");

        for (int i = 0; i < mas.length; i++) {

            Pattern firstParrent = Pattern.compile("^[-+]?[0-9]*[.,]?[0-9]+(?:[eE][-+]?[0-9]+)?$");
            Matcher firstMatcher = firstParrent.matcher(mas[i]);
            if (firstMatcher.matches()) {
                floatStack.push(Float.valueOf(mas[i]));
            }

            Pattern secondPattern = Pattern.compile("\\*|\\/|\\+|\\-|\\^");
            Matcher secondMatcher = secondPattern.matcher(mas[i]);
            if (secondMatcher.matches()) {
                float a = floatStack.pop();
                float b = floatStack.pop();
                floatStack.push(symbol(mas[i], a, b));
            }
        }
        result = floatStack.pop();
        return result;
    }

    public static float symbol(String str, float a, float b) {
        if (str.equals("+")) {
            return a + b;
        } else if (str.equals("-")) {
            return b - a;
        } else if (str.equals("*")) {
            return b * a;
        } else if (str.equals("/")) {
            return b / a;
        } else if (str.equals("^")) {
            return (float) Math.pow(b, a);
        } else {
            System.out.println("Invalid data");
            System.exit(0);
            return 0;
        }
    }
}