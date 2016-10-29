package com.bethere24system.utils;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 3/5/2016.
 */
public class ValidationUtils {

    public static boolean isUsernameShort(@NonNull String username) {
        return username.length() < 6;
    }

    public static boolean isPasswordShort(@NonNull String password) {
        return password.length() < 6;
    }

}
