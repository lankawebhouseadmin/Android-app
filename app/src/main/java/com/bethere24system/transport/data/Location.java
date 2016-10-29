
package com.bethere24system.transport.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 3/5/2016.
 */
public class Location {

    @SerializedName("0")
    private LocationItem locationItem;

    /**
     * 
     * @return
     *     The LocationItem
     */
    public LocationItem getItem() {
        return locationItem;
    }

    /**
     * 
     * @param item
     *     The LocationItem
     */
    public void setItem(LocationItem item) {
        this.locationItem = item;
    }

}
