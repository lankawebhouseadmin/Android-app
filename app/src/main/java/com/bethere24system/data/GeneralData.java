package com.bethere24system.data;

import java.text.ParseException;

/**
 * Created by Administrator on 3/5/2016.
 */
public class GeneralData {

    public final ClockDate startOfTheDay;
    public final ClockDate loginDate;

    public GeneralData(String clockDate) {

        ClockDate startOfTheDay;

        try {
            startOfTheDay = new ClockDate(clockDate);
        } catch (ParseException e) {
            e.printStackTrace();
            startOfTheDay = new ClockDate();
        }

        this.startOfTheDay = startOfTheDay;
        loginDate = new ClockDate();
    }

}
