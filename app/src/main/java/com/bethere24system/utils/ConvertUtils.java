package com.bethere24system.utils;

import android.text.Html;

import java.util.Locale;

/**
 * Created by Administrator on 3/7/2016.
 */
public class ConvertUtils {

    private static final String HOURS_PATTERN = "%d Hr, %02d Min";
    private static final String MINUTES_PATTERN = "%2d Min";

    public static String convertFromMinutesToHours(int minutes) {

        return minutes / 60 > 0 ? Html.fromHtml(String.format(Locale.UK, HOURS_PATTERN, minutes / 60, minutes % 60)).toString() :
                                    Html.fromHtml(String.format(Locale.UK, MINUTES_PATTERN, minutes % 60)).toString();
    }
}
