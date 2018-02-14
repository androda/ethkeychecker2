package com.lostphoenix;

import com.google.common.collect.Lists;

import java.util.List;

public class StringUtils {
    static String hexToAsciiWithNormalization(String hex) {
        StringBuilder output = new StringBuilder();
        boolean valid = true;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int sum = 0;
        List<Integer> charVals = Lists.newArrayList();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            int charVal = Integer.parseInt(str, 16);
            if (charVal > max)
                max = charVal;
            if (charVal < min) {
                min = charVal;
            }
            sum+=charVal;
            charVals.add(charVal);
        }
        int average = sum/charVals.size();
        if(max-min>74)
            return null;
        int shift = 85-average;
        for (Integer charVal : charVals) {
            output.append((char) (charVal+shift));
        }

        return output.toString();
    }

    static String hexToAscii(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            int charVal = Integer.parseInt(str, 16);
                output.append((char) charVal);
        }
        return output.toString();
    }


    static String leftRotate(String s, int shift) {
        int i = shift % s.length();
        return s.substring(i) + s.substring(0, i);
    }

    static String rightRotate(String s, int shift) {
        int i = s.length() - (shift % s.length());
        return s.substring(i) + s.substring(0, i);

    }

    public static String convertBinaryStringToHex(String s) {
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < s.length(); i += 4) {
            String section = s.substring(i, i + 4);
            String secitionConverted = Long.toString(Long.parseLong(section, 2), 16);
            retVal.append(secitionConverted);
        }
        return retVal.toString();
    }

    private static String orBinaryStrings(String s1, String s2) {
        if (s1.length() != s2.length())
            throw new IllegalArgumentException("Sizes must match");
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) == '1' || s2.charAt(i) == '1') {
                retVal.append("1");
            } else {
                retVal.append("0");
            }
        }
        return retVal.toString();
    }

    private static String xorBinaryStrings(String s1, String s2) {
        if (s1.length() != s2.length())
            throw new IllegalArgumentException("Sizes must match");
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                retVal.append("0");
            } else {
                retVal.append("1");
            }
        }
        return retVal.toString();
    }
}
