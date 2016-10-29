package com.bethere24system.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 3/5/2016.
 */
public class ClockDate {

    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm:ss", Locale.UK);

    public final Date date;
    public final int hours;
    public final int minutes;
    public final int seconds;
    public final float angle;

    public ClockDate(String string) throws ParseException {
        this(TIME_FORMAT.parse(string));
    }

    public ClockDate() {
        this(new Date());
    }

    public ClockDate(Date date) {
        this.date = date;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        seconds = calendar.get(Calendar.SECOND);

        angle = 0.25f * (hours * 60f + minutes);

    }

}
