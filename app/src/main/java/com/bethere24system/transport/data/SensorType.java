
package com.bethere24system.transport.data;

/**
 * Created by Administrator on 3/5/2016.
 */
public class SensorType {

    private Integer id;
    private String sensorTypeName;
    private String sensorDescription;
    private String manufacturer;
    private Object active;

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
     *     The sensorTypeName
     */
    public String getSensorTypeName() {
        return sensorTypeName;
    }

    /**
     * 
     * @param sensorTypeName
     *     The sensor_type_name
     */
    public void setSensorTypeName(String sensorTypeName) {
        this.sensorTypeName = sensorTypeName;
    }

    /**
     * 
     * @return
     *     The sensorDescription
     */
    public String getSensorDescription() {
        return sensorDescription;
    }

    /**
     * 
     * @param sensorDescription
     *     The sensor_description
     */
    public void setSensorDescription(String sensorDescription) {
        this.sensorDescription = sensorDescription;
    }

    /**
     * 
     * @return
     *     The manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * 
     * @param manufacturer
     *     The manufacturer
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * 
     * @return
     *     The active
     */
    public Object getActive() {
        return active;
    }

    /**
     * 
     * @param active
     *     The active
     */
    public void setActive(Object active) {
        this.active = active;
    }

}
