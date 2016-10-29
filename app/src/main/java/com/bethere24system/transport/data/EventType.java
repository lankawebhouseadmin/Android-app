
package com.bethere24system.transport.data;

/**
 * Created by Administrator on 3/5/2016.
 */
public class EventType {

    private Integer id;
    private String eventName;
    private String eventDescription;

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
     *     The eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * 
     * @param eventName
     *     The event_name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * 
     * @return
     *     The eventDescription
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * 
     * @param eventDescription
     *     The event_description
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

}
