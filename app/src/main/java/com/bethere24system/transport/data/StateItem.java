
package com.bethere24system.transport.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 3/5/2016.
 */
public class StateItem {

    @SerializedName("id")
    private Integer id;

    @SerializedName("person_location")
    private int personLocation;

    @SerializedName("sensor")
    private int sensor;

    @SerializedName("start_time")
    private long startTime;

    @SerializedName("end_time")
    private long endTime;

    @SerializedName("score")
    private Integer score;

    @SerializedName("normal_time")
    private int normalTime;

    @SerializedName("actual_time")
    private int actualTime;

    /////////////////////////////////////

    @SerializedName("state_type")
    private int stateType;

    @SerializedName("state_name")
    private String stateName;

    @SerializedName("time")
    private String time;

    @SerializedName("times")
    private int times;

    @SerializedName("total_time")
    private String totalTime;

    @SerializedName("message")
    private String message;

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @return
     *     The stateType
     */
    public int getStateType() {
        return stateType;
    }


    /**
     * 
     * @return
     *     The personLocation
     */
    public int getPersonLocation() {
        return personLocation;
    }

    /**
     * 
     * @return
     *     The sensor
     */
    public int getSensor() {
        return sensor;
    }

    /**
     * 
     * @return
     *     The startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * 
     * @return
     *     The endTime
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * 
     * @return
     *     The score
     */
    public int getScore() {
        return score;
    }

    /**
     * 
     * @return
     *     The stateName
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * 
     * @return
     *     The time
     */
    public String getTime() {
        return time;
    }

    /**
     * 
     * @return
     *     The normalTime
     */
    public int getNormalTime() {
        return normalTime;
    }

    /**
     *
     * @return
     *     The times
     */
    public int getTimes() {
        return times;
    }

    /**
     * 
     * @return
     *     The actualTime
     */
    public int getActualTime() {
        return actualTime;
    }

    /**
     *
     * @return
     *     The totalTime
     */
    public String getTotalTime() {
        return totalTime;
    }

    /**
     *
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

}
