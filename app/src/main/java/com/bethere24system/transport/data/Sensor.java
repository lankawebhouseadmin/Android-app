
package com.bethere24system.transport.data;

/**
 * Created by Administrator on 3/5/2016.
 */
public class Sensor {

    private Integer id;
    private SensorType sensorType;
    private Object stateType;
    private Integer personLocation;
    private String sensorName;
    private Boolean active;
    private String sensorKey;

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
     *     The sensorType
     */
    public SensorType getSensorType() {
        return sensorType;
    }

    /**
     * 
     * @param sensorType
     *     The sensor_type
     */
    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    /**
     * 
     * @return
     *     The stateType
     */
    public Object getStateType() {
        return stateType;
    }

    /**
     * 
     * @param stateType
     *     The state_type
     */
    public void setStateType(Object stateType) {
        this.stateType = stateType;
    }

    /**
     * 
     * @return
     *     The personLocation
     */
    public Integer getPersonLocation() {
        return personLocation;
    }

    /**
     * 
     * @param personLocation
     *     The person_location
     */
    public void setPersonLocation(Integer personLocation) {
        this.personLocation = personLocation;
    }

    /**
     * 
     * @return
     *     The sensorName
     */
    public String getSensorName() {
        return sensorName;
    }

    /**
     * 
     * @param sensorName
     *     The sensor_name
     */
    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    /**
     * 
     * @return
     *     The active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * 
     * @param active
     *     The active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * 
     * @return
     *     The sensorKey
     */
    public String getSensorKey() {
        return sensorKey;
    }

    /**
     * 
     * @param sensorKey
     *     The sensor_key
     */
    public void setSensorKey(String sensorKey) {
        this.sensorKey = sensorKey;
    }

}
