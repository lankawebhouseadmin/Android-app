package com.bethere24system.transport;

import retrofit2.Retrofit;

/**
 * Created by Administrator on 3/5/2016.
 */
public class BeThereService {

    private static AuthApiInterface sAuthApi;
    private static StatesApiInterface sStatesApi;

    private static boolean sIsInitialized = false;

    public static void initialize(Retrofit retrofit) {

        sIsInitialized = true;

        sAuthApi = retrofit.create(AuthApiInterface.class);
        sStatesApi = retrofit.create(StatesApiInterface.class);
    }

    public static AuthApiInterface getAuthApi() {
        checkInitialization();
        return sAuthApi;
    }

    public static StatesApiInterface getStatesApi() {
        return sStatesApi;
    }

    private static void checkInitialization() {
        if (!sIsInitialized) throw new RuntimeException("Need to call 'initialize' before");
    }

}
