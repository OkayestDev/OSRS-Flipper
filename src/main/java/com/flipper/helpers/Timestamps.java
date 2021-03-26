package com.flipper.helpers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Timestamps {
    public static String format(Timestamp timestamp) {
        LocalDateTime localTimestamp = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localTimestamp.format(DateTimeFormatter.ofPattern("d/M h:mm a"));
    }
}
