
package com.bethere24system.transport.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 3/5/2016.
 */
public class LocationItem {

    private Integer id;
    private Integer person;
    private Integer locationType;
    private String address;
    private String city;
    private State state;
    private Integer zip;
    private String country;

    @SerializedName("virtual_day_start")
    private String mVirtualDayStart;

    private List<Sensor> sensors = new ArrayList<>();
    private List<PersonProfile> personProfile = new ArrayList<>();

    @SerializedName("alert")
    private List<Alert> alerts = new ArrayList<>();

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
     *     The person
     */
    public Integer getPerson() {
        return person;
    }

    /**
     * 
     * @return
     *     The locationType
     */
    public Integer getLocationType() {
        return locationType;
    }

    /**
     * 
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @return
     *     The city
     */
    public String getCity() {
        return city;
    }

    /**
     * 
     * @return
     *     The state
     */
    public State getState() {
        return state;
    }

    /**
     * 
     * @return
     *     The zip
     */
    public Integer getZip() {
        return zip;
    }

    /**
     * 
     * @return
     *     The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * 
     * @return
     *     The virtualDayStart
     */
    public String getVirtualDayStart() {
        return mVirtualDayStart;
    }

    /**
     * 
     * @return
     *     The sensors
     */
    public List<Sensor> getSensors() {
        return sensors;
    }

    /**
     * 
     * @return
     *     The personProfile
     */
    public List<PersonProfile> getPersonProfile() {
        return personProfile;
    }

    public List<Alert> getAlert() {
        return alerts;
    }

}
