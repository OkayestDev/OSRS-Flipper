package com.flipper.helpers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Timestamps {
    public static String format(Timestamp timestamp) {
        return new SimpleDateFormat("M/d H:mm:ss").format(timestamp);
    }
}
