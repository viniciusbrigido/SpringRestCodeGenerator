package com.brigido.springrestcodegenerator.util;

import static java.lang.Character.*;

public class StringUtil {

    public static String capitalizeFirstLetter(String value) {
        char firstChar = toUpperCase(value.charAt(0));
        return firstChar + value.substring(1);
    }

    public static String lowerCaseFirstLetter(String value) {
        char firstChar = toLowerCase(value.charAt(0));
        return firstChar + value.substring(1);
    }

    public static String parseCamelCaseToSnakeCase(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (isUpperCase(currentChar)) {
                if (i > 0) {
                    output.append('_');
                }
                output.append(toLowerCase(currentChar));
            } else {
                output.append(currentChar);
            }
        }
        return output.toString();
    }
}
