package com.bethere24system.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bethere24system.R;

/**
 * Created by Administrator on 3/5/2016.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = (ImageView) findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        logo.startAnimation(animation);

        new Handler().postDelayed(this::startLoginActivity, 3000);
    }

    private void startLoginActivity() {
        if (!isFinishing()) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }
    }

}
