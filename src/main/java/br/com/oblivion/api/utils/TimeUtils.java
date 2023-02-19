package br.com.oblivion.api.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {

    public static String formatRemainingTime(long time) {
        if (time == 0L) {
            return "nunca";
        }
        long day = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) - day * 24L;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60L;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toMinutes(time) * 60L;
        StringBuilder sb = new StringBuilder();
        if (day > 0L) {
            sb.append(day).append(" ").append(day == 1L ? "dia" : "dias").append(" ");
        }
        if (hours > 0L) {
            sb.append(hours).append(" ").append(hours == 1L ? "hora" : "horas").append(" ");
        }
        if (minutes > 0L) {
            sb.append(minutes).append(" ").append(minutes == 1L ? "minuto" : "minutos").append(" ");
        }
        if (seconds > 0L) {
            sb.append(seconds).append(" ").append(seconds == 1L ? "segundo" : "segundos");
        }
        String diff = sb.toString();
        return diff.isEmpty() ? "0" : diff;
    }

    private static final Pattern periodPattern = Pattern.compile("([0-9]+)([mhd])");

    public static Long parsePeriod(String period) {

        if (period == null) return 0L;
        period = period.toLowerCase();
        Matcher matcher = periodPattern.matcher(period);
        Instant instant = Instant.EPOCH;

        while (matcher.find()) {
            int num = Integer.parseInt(matcher.group(1));
            String typ = matcher.group(2);
            switch (typ) {
                case "h":
                    instant = instant.plus(Duration.ofHours(num));
                    break;
                case "d":
                    instant = instant.plus(Duration.ofDays(num));
                    break;
                case "m":
                    instant = instant.plus(Duration.ofDays(30L * num));
            }
        }
        return instant.toEpochMilli();
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return localDateTime.format(format);
    }

}
