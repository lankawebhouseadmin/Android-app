
package com.bethere24system.transport.data;

/**
 * Created by Administrator on 3/5/2016.
 */
public class StateConstraint {

    private Integer id;
    private Integer constraintType;
    private Integer stateType;
    private Integer aggregated;
    private Integer minDuration;
    private Integer maxDuration;
    private Integer minFrequency;
    private Integer maxFrequency;
    private String rangeStartTime;
    private String rangeEndTime;

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
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The constraintType
     */
    public Integer getConstraintType() {
        return constraintType;
    }

    /**
     * 
     * @param constraintType
     *     The constraint_type
     */
    public void setConstraintType(Integer constraintType) {
        this.constraintType = constraintType;
    }

    /**
     * 
     * @return
     *     The stateType
     */
    public Integer getStateType() {
        return stateType;
    }

    /**
     * 
     * @param stateType
     *     The state_type
     */
    public void setStateType(Integer stateType) {
        this.stateType = stateType;
    }

    /**
     * 
     * @return
     *     The aggregated
     */
    public Integer getAggregated() {
        return aggregated;
    }

    /**
     * 
     * @param aggregated
     *     The aggregated
     */
    public void setAggregated(Integer aggregated) {
        this.aggregated = aggregated;
    }

    /**
     * 
     * @return
     *     The minDuration
     */
    public Integer getMinDuration() {
        return minDuration;
    }

    /**
     * 
     * @param minDuration
     *     The min_duration
     */
    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    /**
     * 
     * @return
     *     The maxDuration
     */
    public Integer getMaxDuration() {
        return maxDuration;
    }

    /**
     * 
     * @param maxDuration
     *     The max_duration
     */
    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    /**
     * 
     * @return
     *     The minFrequency
     */
    public Integer getMinFrequency() {
        return minFrequency;
    }

    /**
     * 
     * @param minFrequency
     *     The min_frequency
     */
    public void setMinFrequency(Integer minFrequency) {
        this.minFrequency = minFrequency;
    }

    /**
     * 
     * @return
     *     The maxFrequency
     */
    public Integer getMaxFrequency() {
        return maxFrequency;
    }

    /**
     * 
     * @param maxFrequency
     *     The max_frequency
     */
    public void setMaxFrequency(Integer maxFrequency) {
        this.maxFrequency = maxFrequency;
    }

    /**
     * 
     * @return
     *     The rangeStartTime
     */
    public String getRangeStartTime() {
        return rangeStartTime;
    }

    /**
     * 
     * @param rangeStartTime
     *     The range_start_time
     */
    public void setRangeStartTime(String rangeStartTime) {
        this.rangeStartTime = rangeStartTime;
    }

    /**
     * 
     * @return
     *     The rangeEndTime
     */
    public String getRangeEndTime() {
        return rangeEndTime;
    }

    /**
     * 
     * @param rangeEndTime
     *     The range_end_time
     */
    public void setRangeEndTime(String rangeEndTime) {
        this.rangeEndTime = rangeEndTime;
    }

}
