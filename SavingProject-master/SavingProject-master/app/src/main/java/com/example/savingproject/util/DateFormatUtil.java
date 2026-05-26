package com.example.savingproject.util;

import com.example.savingproject.DATA.SettingsManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateFormatUtil {

    private DateFormatUtil() {}

    public static String formatDisplay(String isoOrTimestamp) {
        if (isoOrTimestamp == null || isoOrTimestamp.isEmpty()) {
            return "";
        }
        Date date = parseIncoming(isoOrTimestamp);
        if (date == null) {
            return isoOrTimestamp;
        }
        SettingsManager settings = SettingsManager.getInstance();
        String pattern = SettingsManager.DATE_YYYY_MM_DD.equals(settings.getDateFormat())
                ? "yyyy-MM-dd" : "dd/MM/yyyy";
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
    }

    private static Date parseIncoming(String value) {
        String[] patterns = {
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd"
        };
        for (String pattern : patterns) {
            try {
                return new SimpleDateFormat(pattern, Locale.US).parse(value);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }
}
