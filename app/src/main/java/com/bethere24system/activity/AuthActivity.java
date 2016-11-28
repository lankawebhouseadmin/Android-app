package com.bethere24system.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.bethere24system.BeThereApplication;
import com.bethere24system.R;
import com.bethere24system.data.DataContainer;
import com.bethere24system.fragment.AuthFragment;
import com.bethere24system.transport.BeThereService;
import com.bethere24system.transport.data.LoginData;
import com.bethere24system.utils.ValidationUtils;
import com.github.pwittchen.prefser.library.Prefser;

import java.io.Serializable;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 3/5/2016.
 */
public class AuthActivity extends AppCompatActivity implements AuthFragment.Listener {

    private Subscription mSubscription;
    private AuthFragment mAuthFragment;
    public static final String MYPREFS="mySharedPreferences";

    LoginData loginData; // Dennis

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuthFragment = new AuthFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mAuthFragment)
                .commit();

    }

    @Override
    public void onLogin(String username, String password) {

        if (!isUsernameValid(username) || !isPasswordValid(password)) return;

        mAuthFragment.showProgress();

//        mSubscription = BeThereService.getAuthApi().login(new LoginData(username, password))
//                .flatMap(userInfo -> BeThereService.getStatesApi().getStates(userInfo.getData().getId(), userInfo.getToken())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread()))
//                .map(DataContainer::new)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .cache()
//                .subscribe(
//                        this::openMainActivity,
//                        this::onLoginError
//                );


        loginData = new LoginData(username, password); // Dennis

        mSubscription = BeThereService.getAuthApi().login(loginData)
                .flatMap(userInfo ->{
                    new Prefser(AuthActivity.this).put("data", userInfo.getData());
                    BeThereApplication.getInstance().setLoginTime(userInfo.getData().getLoginTime());
                    return BeThereService.getStatesApi().getStates(userInfo.getData().getId(), userInfo.getToken())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                })
                .map(DataContainer::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(
                        this::openMainActivity,
                        this::onLoginError
                );

    }

    private boolean isUsernameValid(String username) {
        if (TextUtils.isEmpty(username)) {
            showAlertMessage(R.string.title_signin, R.string.error_message_empty_username);
            return false;
        } else if (ValidationUtils.isUsernameShort(username)) {
            showAlertMessage(R.string.title_signin, R.string.error_message_short_username);
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        if (TextUtils.isEmpty(password)) {
            showAlertMessage(R.string.title_signin, R.string.error_message_empty_password);
            return false;
        } else if (ValidationUtils.isPasswordShort(password)) {
            showAlertMessage(R.string.title_signin, R.string.error_message_short_password);
            return false;
        }
        return true;
    }

    private void onLoginError(Throwable e) {
        e.printStackTrace();
        mAuthFragment.showContent();

        // 2016 05 11 by Dennis
        //showAlertMessage(R.string.error_title_common, R.string.error_message_common);
        showAlertMessage(R.string.error_title_common, e.getMessage());
    }
    private void showAlertMessage(int titleRes, int messageRes) {
        new AlertDialog.Builder(this)
                .setTitle(titleRes)
                .setMessage(messageRes)
                .setPositiveButton(R.string.dialog_positive_button, null)
                .create()
                .show();
    }
    private void showAlertMessage(int titleRes, String messageRes) {
        new AlertDialog.Builder(this)
                .setTitle(titleRes)
                .setMessage(messageRes)
                .setPositiveButton(R.string.dialog_positive_button, null)
                .create()
                .show();
    }


    private void openMainActivity(DataContainer dataContainer) {
        BeThereApplication.getInstance().setData(dataContainer);

        Intent intent = new Intent(AuthActivity.this, MainActivity.class);

        intent.putExtra("username", loginData.username); // Dennis
        intent.putExtra("password", loginData.password); // Dennis
        savePreferences();

        startActivity(intent);
        finish();
    }


    @Override
    public void onForgotPassword() {
        showAlertMessage(R.string.title_forgot_password, R.string.message_forgot_password);
    }


    @Override
    protected void onDestroy() {
        if (mSubscription != null) mSubscription.unsubscribe();
        super.onDestroy();
    }




    public void savePreferences(){
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreferences = getSharedPreferences(MYPREFS, mode);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("username", loginData.username);
        editor.putString("userpass", loginData.password);
        editor.putBoolean("isSetting", true);
        editor.commit();
    }

}
