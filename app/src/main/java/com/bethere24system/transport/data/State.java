
package com.bethere24system.transport.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 3/5/2016.
 */
public class State {

    /**
     * IMPORTANT! This model is the copy of Json object. Can't be changed to other structure until server model is not changed
     * So it is impossible to replace few List<> with HashMam<>
     */

    @SerializedName("in_bathroom")
    private List<StateItem> mInBathroom = new ArrayList<>();

    @SerializedName("medication")
    private List<StateItem> mMedication = new ArrayList<>();

    @SerializedName("in_recliner")
    private List<StateItem> mInRecliner = new ArrayList<>();

    @SerializedName("in_dining")
    private List<StateItem> mInDining = new ArrayList<>();

    @SerializedName("sleeping")
    private List<StateItem> mSleeping = new ArrayList<>();

    @SerializedName("away_from_home")
    private List<StateItem> mAwayFromHome = new ArrayList<>();

    @SerializedName("with_visitors")
    private List<StateItem> mWithVisitors = new ArrayList<>();



    /**
     * @return The inBathroom
     */
    public List<StateItem> getInBathroom() {
        return mInBathroom;
    }

    /**
     * @return The medication
     */
    public List<StateItem> getMedication() {
        return mMedication;
    }

    /**
     * @return The inRecliner
     */
    public List<StateItem> getInRecliner() {
        return mInRecliner;
    }

    /**
     * @return The inDining
     */
    public List<StateItem> getInDining() {
        return mInDining;
    }

    /**
     * @return The sleeping
     */
    public List<StateItem> getSleeping() {
        return mSleeping;
    }

    /**
     * @return The AwayFromHome
     */
    public List<StateItem> getAwayFromHome() {
        return mAwayFromHome;
    }

    /**
     * @return The WithVisitors
     */
    public List<StateItem> getWithVisitors() {
        return mWithVisitors;
    }

}
