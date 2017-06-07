package com.andreabardella.aifaservicesconsumer.util;

import android.support.annotation.NonNull;

public class Code39Conversion {

    private static final String BASE_32 = "0123456789BCDFGHJKLMNPQRSTUVWXYZ";

    public static String base32ToBase10(@NonNull String input) throws Exception {
        int res = 0;
        int exponent = 5;
        for (char c : input.toCharArray()) {
            int index = binarySearch(BASE_32.toCharArray(), c);
            if (index == -1) {
                throw new Exception(input + " cannot be converted");
            }
            res += Math.pow(32, exponent) * (index);
            exponent--;
        }
        String result = Integer.toString(res);
        for (int i=result.length(); i<9; i++) {
            result = '0' + result;
        }
        return result;
    }

    public static String base10ToBase32(@NonNull String input) throws Exception {
        String in = input;
        if (in.charAt(0) == 'A') {
            in = in.substring(1);
        }
        int code;
        try {
            code = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("unable to convert " + input + " into a number");
        }
        String result = "";
        for (int i=5; i>-1; i--) {
            int val = (int) (code/Math.pow(32, i));
            code -= val*Math.pow(32, i);
            if (val <= 31) {
                result += BASE_32.toCharArray()[val];
            } else {
                throw new Exception("unable to convert " + input + " into an AIC code");
            }
        }
        return result;
    }

    private static int binarySearch(char[] arr, char c) {
        int start = 0;
        int end = arr.length - 1;
        while (start <= end) {
            int pivot = start + (end - start)/2;
            if (c == arr[pivot]) {
                return pivot;
            } else if (c < arr[pivot]) {
                end = pivot - 1;
            } else {
                start = pivot + 1;
            }
        }
        return -1;
    }
}
