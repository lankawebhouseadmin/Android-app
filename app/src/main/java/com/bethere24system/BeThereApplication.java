package com.bethere24system;

import android.app.Application;

import com.bethere24system.data.DataContainer;
import com.bethere24system.transport.BeThereService;
import com.bethere24system.transport.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 3/5/2016.
 */
public class BeThereApplication extends Application {

    private static BeThereApplication sInstance;
    private Retrofit mRetrofit;
    private DataContainer mData;
    private String mLoginTime;

    public static BeThereApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(5, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) builder.addInterceptor(interceptor);

        Gson gson = new GsonBuilder()
//                .setDateFormat(Constants.DEFAULT_DATE_FORMAT)
                .registerTypeAdapter(Date.class, new gsonUTCdateAdapter())
                .create();

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(Constants.HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        BeThereService.initialize(mRetrofit);

    }

    public void setData(DataContainer data) {
        mData = data;
    }

    public DataContainer getData() {
        return mData;
    }

    public void setLoginTime(String loginTime) {
        this.mLoginTime = loginTime;
    }

    public String getLoginTiem() {
        return this.mLoginTime;
    }

    public static class gsonUTCdateAdapter implements JsonSerializer<Date>,JsonDeserializer<Date> {

        private final DateFormat dateFormat;

        public gsonUTCdateAdapter() {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        }

        @Override
        public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(dateFormat.format(date));
        }

        @Override
        public synchronized Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
            try {
                return dateFormat.parse(jsonElement.getAsString());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }
}
