package com.flipper.helpers;

import java.math.BigInteger;
import java.text.NumberFormat;

public class Numbers {

    public static String numberWithCommas(int number) {
        return NumberFormat.getIntegerInstance().format(number);
    }

    public static String numberWithCommas(String number) {
        if (number == "0") {
            return "0";
        }

        BigInteger numberAsBigInt = new BigInteger(number);
        return String.format("%,d", numberAsBigInt);
    }
}
