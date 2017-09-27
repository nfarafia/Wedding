package com.vergiliy.wedding.helpers;

// Class with base helper methods
public class BaseHelper {

    // Parse String to Integer with defaut value
    public static Integer parseInteger(String string, int defaultValue ) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e ) {
            return defaultValue;
        }
    }
}
