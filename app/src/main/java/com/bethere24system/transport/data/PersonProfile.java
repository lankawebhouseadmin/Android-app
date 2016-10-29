
package com.bethere24system.transport.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 3/5/2016.
 */
public class PersonProfile {

    private Integer id;
    private String profileName;
    private Integer personLocation;
    private List<Schedule> schedules = new ArrayList<Schedule>();
    private List<EventProfile> eventProfile = new ArrayList<EventProfile>();

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
     *     The profileName
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * 
     * @param profileName
     *     The profile_name
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
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
     *     The schedules
     */
    public List<Schedule> getSchedules() {
        return schedules;
    }

    /**
     * 
     * @param schedules
     *     The schedules
     */
    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    /**
     * 
     * @return
     *     The eventProfile
     */
    public List<EventProfile> getEventProfile() {
        return eventProfile;
    }

    /**
     * 
     * @param eventProfile
     *     The event_profile
     */
    public void setEventProfile(List<EventProfile> eventProfile) {
        this.eventProfile = eventProfile;
    }

}
