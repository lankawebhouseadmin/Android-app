package com.bethere24system.transport;

import com.bethere24system.transport.data.LoginData;
import com.bethere24system.transport.data.UserInfo;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 3/5/2016.
 */
public interface AuthApiInterface {

    @POST("/api/login/")
    Observable<UserInfo> login(@Body LoginData loginData);

}
