package com.bethere24system.data;

import android.text.format.DateUtils;

import com.bethere24system.transport.data.Messages;
import com.bethere24system.transport.data.StateItem;
import com.bethere24system.utils.ConvertUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 3/5/2016.
 */
public class State {

    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);

    public final long id;
    public final StateType type;
    public final Date startTime;
    public final Date endTime;
    public final Score score;
    public final String normalTime;
    public final int actualTime;
    public final String actualTimeChanged;

    public final int times;
    public final Date time;
    public final String totalTime;
    public final String message;

    public final Date day;
    public final String duration;
    public final boolean isToday;

    public final float startAngle;
    public final float sweepAngle;
    public final float centerAngle;

    public State(StateItem stateItem) {
        id = stateItem.getId();
        type = StateType.fromTypeIndex(stateItem.getStateType());
//        type = StateType.fromTypeName(stateItem.getStateType());
        startTime = getDateFromTimeStamp(stateItem.getStartTime());
        endTime = getDateFromTimeStamp(stateItem.getEndTime());
        score = new Score(stateItem.getScore());
        message = new Messages().getMessageFromTypeAndScore(type, score);

        normalTime = type != StateType.MEDICATION ? ConvertUtils.convertFromMinutesToHours(stateItem.getNormalTime()) :
                        stateItem.getNormalTime() > 1 ? String.format("%d times", stateItem.getNormalTime()) :
                                                        String.format("%d time", stateItem.getNormalTime());

        actualTime = stateItem.getActualTime();
        actualTimeChanged = ConvertUtils.convertFromMinutesToHours(actualTime);

        times = stateItem.getTimes();
        totalTime = stateItem.getTotalTime();

        Date time;
        /*
        try {
            time = TIME_FORMAT.parse("");
        } catch (ParseException e) {
            e.printStackTrace();
            time = new Date();
        }
        */
//        this.time = time;
        time = startTime;
        this.time = time;

        Calendar c = Calendar.getInstance();
        c.setTime(time);
        Calendar rounded = Calendar.getInstance();
        rounded.setTimeInMillis(0);
        rounded.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        this.day = rounded.getTime();

        isToday = DateUtils.isToday(day.getTime());

        long millis = endTime.getTime() - startTime.getTime();
        int hours = (int) millis / (1000 * 60 * 60);
        int minutes = (int) millis % (1000*60*60)/(60*1000);

        String dur;
        if (hours > 0) {
            dur = String.format(Locale.UK, "%d hrs, %d mins", hours, minutes);
        } else {
            dur = String.format(Locale.UK, "%d mins", minutes);
        }

        duration = dur;

        startAngle = getStartAngle(startTime);
        sweepAngle = getSweepAngle(startTime, endTime);
        centerAngle = startAngle + sweepAngle / 2f;
    }

    public State(StateType type, Date day) {
        id = 0;
        this.type = type;
        startTime = new Date(0);
        endTime = new Date(0);
        score = new Score(0);
        time = new Date(0);
        this.day = day;
        normalTime = null;
        actualTime = 0;
        actualTimeChanged = null;
        times = 0;
        totalTime = null;
        message = null;
        duration  = "";
        isToday = false;

        startAngle = 0;
        sweepAngle = 0;
        centerAngle = 0;
    }

    private static float getStartAngle(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (0.25f * (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)));
    }

    private static float getSweepAngle(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int hours = calendar2.get(Calendar.HOUR_OF_DAY) - calendar1.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar2.get(Calendar.MINUTE) - calendar1.get(Calendar.MINUTE);

        float sweepAngle = (0.25f * (hours * 60 + minutes));

        return  sweepAngle < 0 ? 360f + sweepAngle : sweepAngle;
    }

    private static Date getDateFromTimeStamp(long timeStamp) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return new java.util.Date(timeStamp * 1000);

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof State) {
            State s = (State) o;
            if (type == s.type && id == s.id && day.getTime() == s.day.getTime()) return true;
        }
        return false;
    }
}
