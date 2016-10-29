package com.bethere24system.transport.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 3/5/2016.
 */
public class LoginData {

    @SerializedName("username")
    public final String username;
    @SerializedName("password")
    public final String password;

    public LoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
