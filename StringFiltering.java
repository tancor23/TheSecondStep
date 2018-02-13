package GPSolutions;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class StringFiltering {
    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayResult = new ArrayList<>();
        Pattern pattern;
        String argument;

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the argument: ");
            argument = sc.nextLine();
            if (argument.isEmpty()) {
                System.out.println("The argument is empty. Please repeat to enter! ");
            } else {
                break;
            }
        }

        argument = argument.replaceAll("\\s+", "|");

        System.out.println("Enter the string(s) for compare: ");
        while (true) {
            String arrayString = sc.nextLine();
            if (arrayString.isEmpty()) {
                break;
            }
            arrayList.add(arrayString);
        }
        sc.close();


        try {
            for (String str : arrayList) {
                String[] result = str.split(" ");
                pattern = Pattern.compile(argument);
                result[result.length - 1] = result[result.length - 1].replace(";", "");
                for (int i = 0; i < result.length; i++) {
                    if (pattern.matcher(result[i]).matches()) {
                        arrayResult.add(str);
                        break;
                    }
                }
            }
        } catch (PatternSyntaxException pt) {
            for (String str : arrayList) {
                for (int i = 0; i < str.split("\\s+").length; i++) {
                    String result = str.split(" ")[i].replace(";", "");

                    if (argument.equals(result)) {
                        arrayResult.add(str);
                        break;
                    }
                }
            }
        }
        System.out.print("The result is: ");
        for (String line : arrayResult) {
            System.out.println(line);
        }
        if (arrayResult.size() == 0) {
            System.out.print("No matches");
        }
    }
}