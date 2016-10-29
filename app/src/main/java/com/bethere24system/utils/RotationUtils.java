package com.bethere24system.utils;

/**
 * Created by Administrator on 3/7/2016.
 */
public class RotationUtils {

    public static boolean isClockwise;
    public static boolean isScoreTouched;

    public static void setClockwise(boolean YesNo) {

        isClockwise = YesNo;
    }

    public static boolean getClockwise() {

        return isClockwise;
    }

    public static void setScoreTouch(boolean YesNo) {

        isScoreTouched = YesNo;
    }

    public static boolean getScoreTouch() {

        return isScoreTouched;
    }
}
