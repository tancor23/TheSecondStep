package GPSolutions.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.print("Enter the expression: ");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String str = br.readLine();

            Calculator calculator = new Calculator(str);
            calculator.checkExpression();

        } catch (CalculatorException ex) {
            System.out.println(ex.getMessage());
        }
    }

}