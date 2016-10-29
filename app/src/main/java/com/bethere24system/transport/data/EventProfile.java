
package com.bethere24system.transport.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 3/5/2016.
 */
public class EventProfile {

    private Integer id;
    private Integer personProfile;
    private EventType eventType;
    private StateConstraint stateConstraint;
    private Integer actionType;
    private Integer schedule;
    private String validFrom;
    private String validTo;
    private List<Object> event = new ArrayList<>();

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
     *     The personProfile
     */
    public Integer getPersonProfile() {
        return personProfile;
    }

    /**
     * 
     * @param personProfile
     *     The person_profile
     */
    public void setPersonProfile(Integer personProfile) {
        this.personProfile = personProfile;
    }

    /**
     * 
     * @return
     *     The eventType
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * 
     * @param eventType
     *     The event_type
     */
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    /**
     * 
     * @return
     *     The stateConstraint
     */
    public StateConstraint getStateConstraint() {
        return stateConstraint;
    }

    /**
     * 
     * @param stateConstraint
     *     The state_constraint
     */
    public void setStateConstraint(StateConstraint stateConstraint) {
        this.stateConstraint = stateConstraint;
    }

    /**
     * 
     * @return
     *     The actionType
     */
    public Integer getActionType() {
        return actionType;
    }

    /**
     * 
     * @param actionType
     *     The action_type
     */
    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    /**
     * 
     * @return
     *     The schedule
     */
    public Integer getSchedule() {
        return schedule;
    }

    /**
     * 
     * @param schedule
     *     The schedule
     */
    public void setSchedule(Integer schedule) {
        this.schedule = schedule;
    }

    /**
     * 
     * @return
     *     The validFrom
     */
    public String getValidFrom() {
        return validFrom;
    }

    /**
     * 
     * @param validFrom
     *     The valid_from
     */
    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    /**
     * 
     * @return
     *     The validTo
     */
    public String getValidTo() {
        return validTo;
    }

    /**
     * 
     * @param validTo
     *     The valid_to
     */
    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    /**
     * 
     * @return
     *     The event
     */
    public List<Object> getEvent() {
        return event;
    }

    /**
     * 
     * @param event
     *     The event
     */
    public void setEvent(List<Object> event) {
        this.event = event;
    }

}
