
package com.bethere24system.transport.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 3/5/2016.
 */
public class Data {

    @SerializedName("username")
    private String mUsername;

    @SerializedName("gender")
    private String mGender;

    @SerializedName("firstname")
    private String mFirstname;

    @SerializedName("lastname")
    private String mLastname;

    @SerializedName("dateOfBirth")
    private String mDateOfBirth;

    @SerializedName("recentState")
    private String mRecentState;

    @SerializedName("active")
    private Boolean mActive;

    @SerializedName("id")
    private Integer mId;

    @SerializedName("login_time")
    private String mLoginTime;
    /**
     * 
     * @return
     *     The mUsername
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * 
     * @param username
     *     The mUsername
     */
    public void setUsername(String username) {
        this.mUsername = username;
    }

    /**
     * 
     * @return
     *     The mGender
     */
    public String getGender() {
        return mGender;
    }

    /**
     * 
     * @param gender
     *     The mGender
     */
    public void setGender(String gender) {
        this.mGender = gender;
    }

    /**
     * 
     * @return
     *     The mFirstname
     */
    public String getFirstname() {
        return mFirstname;
    }

    /**
     * 
     * @param firstname
     *     The mFirstname
     */
    public void setFirstname(String firstname) {
        this.mFirstname = firstname;
    }

    /**
     * 
     * @return
     *     The mLastname
     */
    public String getLastname() {
        return mLastname;
    }

    /**
     * 
     * @param lastname
     *     The mLastname
     */
    public void setLastname(String lastname) {
        this.mLastname = lastname;
    }

    /**
     * 
     * @return
     *     The mDateOfBirth
     */
    public String getDateOfBirth() {
        return mDateOfBirth;
    }

    /**
     * 
     * @param dateOfBirth
     *     The date_of_birth
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.mDateOfBirth = dateOfBirth;
    }

    /**
     * 
     * @return
     *     The mRecentState
     */
    public String getRecentState() {
        return mRecentState;
    }

    /**
     * 
     * @param recentState
     *     The recent_state
     */
    public void setRecentState(String recentState) {
        this.mRecentState = recentState;
    }

    /**
     * 
     * @return
     *     The mActive
     */
    public Boolean getActive() {
        return mActive;
    }

    /**
     * 
     * @param active
     *     The mActive
     */
    public void setActive(Boolean active) {
        this.mActive = active;
    }

    /**
     * 
     * @return
     *     The mId
     */
    public Integer getId() {
        return mId;
    }

    /**
     * 
     * @param id
     *     The mId
     */
    public void setId(Integer id) {
        this.mId = id;
    }

    public String getLoginTime() {
        return mLoginTime;
    }

    public void setLoginTime(String loginTime) {
        this.mLoginTime = loginTime;
    }
}
