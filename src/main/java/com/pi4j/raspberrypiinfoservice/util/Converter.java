package com.pi4j.raspberrypiinfoservice.util;

public class Converter {

    private Converter() {
        // NOP
    }

    public static String intToHexColor(int value) {
        return "#" + padLeftZeros(Integer.toHexString(value), 6);
    }

    /**
     * https://www.baeldung.com/java-pad-string
     */
    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
