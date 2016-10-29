package com.bethere24system.data;

import com.bethere24system.transport.data.Messages;
import com.bethere24system.utils.ConvertUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 3/5/2016.
 */
public class Alert {

    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);

    public final StateType stateType;
    public final String alertType;
    public final String normalTime;
    public final Score score;
    public final Date time;
    public final Date day;
    public final String message;
    public final String actualTimeChanged;

//    public final int actualTime;
    public final String actualTime; // Dennis

    public Alert(com.bethere24system.transport.data.Alert alert) {
        alertType = alert.getType();
//        stateType = StateType.fromTypeName(alert.getStateType());
        stateType = StateType.fromTypeIndex(alert.getStateType());
//        normalTime = ConvertUtils.convertFromMinutesToHours(alert.getNormalTime());
//        actualTimeChanged = ConvertUtils.convertFromMinutesToHours(actualTime);

        normalTime = stateType != StateType.MEDICATION ? ConvertUtils.convertFromMinutesToHours(alert.getNormalTime()) :
                            alert.getNormalTime() > 1 ? String.format("%d times", alert.getNormalTime()) :
                                                        String.format("%d time", alert.getNormalTime());

        // actualTime = alert.getActualTime();
        actualTime = stateType != StateType.MEDICATION ? ConvertUtils.convertFromMinutesToHours(alert.getActualTime()) :
                alert.getActualTime() > 1 ? String.format("%d times", alert.getActualTime()) :
                        String.format("%d time", alert.getActualTime());


        // actualTimeChanged = ConvertUtils.convertFromMinutesToHours(actualTime);
        actualTimeChanged = ConvertUtils.convertFromMinutesToHours(alert.getActualTime());

        score = new Score(alert.getScore());
        message = new Messages().getMessageFromTypeAndScore(stateType, score);

        Date time;
        try {
            time = TIME_FORMAT.parse(getDateFromTimeStamp(alert.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            time = new Date();
        }
        this.time = time;

        Calendar c = Calendar.getInstance();
        c.setTime(time);
        Calendar rounded = Calendar.getInstance();
        rounded.setTimeInMillis(0);
        rounded.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        this.day = rounded.getTime();
    }

    private static String getDateFromTimeStamp(long timeStamp) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return TIME_FORMAT.format(new java.util.Date(timeStamp * 1000));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Alert) {
            Alert alert = (Alert) o;
            return alert.stateType == stateType && alert.day.getTime() == day.getTime();
        }
        return super.equals(o);
    }
}
