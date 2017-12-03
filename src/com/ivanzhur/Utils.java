package com.ivanzhur;

public class Utils {

    public static int stringToInt(String s) {
        int i = 0;

        try {
            i = Integer.parseInt(s.trim());
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException: " + nfe.getMessage());
        }
        return i;
    }
}

