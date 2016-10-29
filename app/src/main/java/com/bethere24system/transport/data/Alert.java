package com.bethere24system.transport.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 3/5/2016.
 */
public class Alert {

    @SerializedName("alert_type")
    private String type;

    @SerializedName("normal_time")
    private int normalTime;

    @SerializedName("state_type")
    private int stateType;

    @SerializedName("score")
    private int score;

    @SerializedName("alert_time")
    private long time;

    @SerializedName("actual_time")
    private int actualTime;

    @SerializedName("message")
    private String message;


    public String getType() {
        return type;
    }

    public int getStateType() {
        return stateType;
    }

    public int getNormalTime() {
        return normalTime;
    }

    public int getScore() {
        return score;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public int getActualTime() {
        return actualTime;
    }

}
