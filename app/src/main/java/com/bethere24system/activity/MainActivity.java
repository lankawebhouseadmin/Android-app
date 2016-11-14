package com.bethere24system.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bethere24system.BeThereApplication;
import com.bethere24system.BuildConfig;
import com.bethere24system.R;
import com.bethere24system.data.DataContainer;
import com.bethere24system.data.State;
import com.bethere24system.fragment.AlertsFragment;
import com.bethere24system.fragment.ContactInfoFragment;
import com.bethere24system.fragment.GraphsFragment;
import com.bethere24system.fragment.HealthScoreFragment;
import com.bethere24system.fragment.HealthSummaryFragment;
import com.bethere24system.fragment.LeftMenuFragment;
import com.bethere24system.transport.BeThereService;
import com.bethere24system.transport.data.LoginData;
import com.github.pwittchen.prefser.library.Prefser;

import java.util.Date;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 3/5/2016.
 */
public class MainActivity extends AppCompatActivity implements LeftMenuFragment.Listener, HealthSummaryFragment.Listener,
        HealthScoreFragment.Listener, AlertsFragment.Listener, GraphsFragment.Listener, ContactInfoFragment.Listener, View.OnClickListener {

    private ViewHolder mHolder;
    private View mDecorView;
    private HealthSummaryFragment mCurrentSummery;

    LoginData loginData; // Dennis

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String username = getIntent().getStringExtra("username"); // Dennis
        String password = getIntent().getStringExtra("password"); // Dennis
        loginData = new LoginData(username, password); // Dennis

        mDecorView = getWindow().getDecorView();

        mHolder = new ViewHolder(this);
        mHolder.drawer.closeDrawers();
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mHolder.drawer, mHolder.toolbar, R.string.drawer_open, R.string.drawer_close);
        mHolder.drawer.setDrawerListener(mDrawerToggle);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Resources resources = getResources();
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dm.widthPixels / dm.density, resources.getDisplayMetrics());
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mHolder.menu.getLayoutParams();
        params.width = (int)width;
        mHolder.menu.setLayoutParams(params);

        mHolder.alert.setOnClickListener(this);
        mHolder.refresh.setOnClickListener(this);

        // 2016 05 11 Dennis
        mHolder.progressLayout.setVisibility(View.GONE);

        String title = getResources().getString(R.string.app_name_tm);
//        if (BuildConfig.BUILD_TYPE.equals("debug")) mHolder.toolbar_title.setText(title + " " + "Dev");
        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            mHolder.toolbar_title.setText(title + " " + "Dev");
        } else if (BuildConfig.BUILD_TYPE.equals("qa")) {
            mHolder.toolbar_title.setText(title + " " + "Uat");
        }
        // end Dennis

        // 2016 09 23 Arik
        mCurrentSummery = new HealthSummaryFragment();
        // end Arik

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.left_menu_container, new LeftMenuFragment())
                .add(R.id.content_container, mCurrentSummery)
                .commit();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mDecorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                             View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    private void hideSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION   // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN        // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onHealthSummaryOpen(Date date, State state) {
        mCurrentSummery = HealthSummaryFragment.newInstance(date, state);
        openFragment(mCurrentSummery);
    }

    @Override
    public void onHealthScoreOpen(Date date, State state) {
        openFragment(HealthScoreFragment.newInstance(date, state));
    }

    @Override
    public void onAlertSummaryOpen(Date date, State state, int linkEnableMode) {
        openFragment(AlertsFragment.newInstance(date, state, linkEnableMode));
    }

    @Override
    public void onHistoricalGraphsOpen(Date date, State state) {
        openFragment(GraphsFragment.newInstance(date, state));
    }

    @Override
    public void onAlertCountChanged(Date date, State state, int linkEnableMode, int count) {
        Log.e("######", " count: " + count);
        if (count > 0) {
            mHolder.badge.setVisibility(View.VISIBLE);
            mHolder.badge.setText(String.valueOf(count));
        } else {
            mHolder.badge.setVisibility(View.GONE);
        }
    }

    @Override
    public void onContactInfoOpen() {
        openFragment(new ContactInfoFragment());
    }

    private void openFragment(Fragment fragment) {
        onAlertCountChanged(null, null, 0, 0);
        if (getSupportFragmentManager().findFragmentById(R.id.content_container).getClass() != fragment.getClass()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.content_container, fragment)
                    .commit();
        }
        mHolder.drawer.closeDrawers();
    }

    @Override
    public void onClick(View v) {
        if (v == mHolder.alert) {
            onAlertSummaryOpen(null, null, 0);
        } else if (v == mHolder.refresh) {

//            Toast.makeText(MainActivity.this, "Refresh", Toast.LENGTH_SHORT).show();

            // Dennis
//            onHealthSummaryOpen(null, null);
            mHolder.progressLayout.setVisibility(View.VISIBLE);

            BeThereService.getAuthApi().login(loginData)
                    .flatMap(userInfo ->{
                        new Prefser(MainActivity.this).put("data", userInfo.getData());
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
//                            this::openMainActivity,
                            this::refreshData,
                            this::onLoginError
                    );
        }
    }

    // Dennis
    private void openMainActivity(DataContainer dataContainer) {

        BeThereApplication.getInstance().setData(dataContainer);
        mHolder.progressLayout.setVisibility(View.GONE);
    }

    private void onLoginError(Throwable e) {
        e.printStackTrace();

        // 2016 05 11 by Dennis
        showAlertMessage(R.string.error_title_common, e.getMessage());
    }

    private void refreshData(DataContainer dataContainer) {

        BeThereApplication.getInstance().setData(dataContainer);
        mHolder.progressLayout.setVisibility(View.GONE);

        if (mCurrentSummery != null) {
            mCurrentSummery.refreshData();
        }
    }
    private void showAlertMessage(int titleRes, String messageRes) {
        new AlertDialog.Builder(this)
                .setTitle(titleRes)
                .setMessage(messageRes)
                .setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .create()
                .show();
    }


    private static final class ViewHolder {

        public final DrawerLayout drawer;
        public final View menu;
        public final Toolbar toolbar;
        public final View alert;
        public final View refresh;
        public final TextView badge;

        public final TextView toolbar_title;// Dennis
        public final ProgressBar progress; // Dennis
        public final RelativeLayout progressLayout;

        public ViewHolder(Activity activity) {
            drawer = (DrawerLayout) activity.findViewById(R.id.drawer);
            menu = activity.findViewById(R.id.left_menu_container);
            toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
            alert = activity.findViewById(R.id.alert);
            refresh = activity.findViewById(R.id.refresh);
            badge = (TextView) activity.findViewById(R.id.badge);

            toolbar_title = (TextView) activity.findViewById(R.id.toolbar_title);// Dennis
            progress = (ProgressBar) activity.findViewById(R.id.progress); // Dennis
            progressLayout = (RelativeLayout)activity.findViewById(R.id.progressLayout); // Dennis
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //����ǰ������ҳ�����ȷ�����ҳ
                return false;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
