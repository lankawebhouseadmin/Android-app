package com.bethere24system.transport;

import com.bethere24system.transport.data.UserState;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 3/5/2016.
 */
public interface StatesApiInterface {

    @GET("/api/states/{id}/{token}/")
    Observable<List<UserState>> getStates(@Path("id") int id, @Path("token") String token);

}
