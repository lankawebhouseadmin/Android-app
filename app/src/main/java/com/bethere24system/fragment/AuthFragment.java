package com.bethere24system.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.bethere24system.BeThereApplication;
import com.bethere24system.BuildConfig;
import com.bethere24system.R;
import com.bethere24system.transport.Constants;

import static com.bethere24system.activity.AuthActivity.MYPREFS;

/**
 * Created by Administrator on 3/5/2016.
 */
public class AuthFragment extends Fragment implements View.OnClickListener {

    private ViewHolder mHolder;
    private Listener mListener;
    private String savedUsername = "";
    private String savedPassword = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) mListener = (Listener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHolder = new ViewHolder(inflater.inflate(R.layout.fragment_login, container, false));

        mHolder.forgotPassword.setOnClickListener(this);
        mHolder.signIn.setOnClickListener(this);

        if (BuildConfig.DEBUG) {
            mHolder.username.setText(""); // ben_goldberg
            mHolder.password.setText(""); // ben1234
        }

        if (loadPreferences()){
            mHolder.username.setText(savedUsername);
            mHolder.password.setText(savedPassword);
        }

        mHolder.imgviewLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Select Server")
                        .setPositiveButton("Stage", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Constants.HOST = "http://staging.noostore.com:80";
                                Constants.APP_STATE = "Dev";
                                BeThereApplication.getInstance().changedHost();
                            }
                        })
                        .setNegativeButton("UAT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Constants.HOST = "http://uat.noostore.com:80";
                                Constants.APP_STATE = "Uat";
                                BeThereApplication.getInstance().changedHost();
                            }
                        })
                        .setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();

                return false;
            }
        });

        return mHolder.root;
    }

    @Override
    public void onClick(View v) {
        if (v == mHolder.forgotPassword) {
            if (mListener != null) mListener.onForgotPassword();
        } else if (v == mHolder.signIn) {
            if (mListener != null) {

                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.translate);
                mHolder.username.startAnimation(animation);
                mHolder.password.startAnimation(animation);
                mHolder.forgotPassword.startAnimation(animation);
                mHolder.signIn.startAnimation(animation);

//                mHolder.username.setVisibility(View.INVISIBLE);
//                mHolder.password.setVisibility(View.INVISIBLE);
//                mHolder.forgotPassword.setVisibility(View.INVISIBLE);
//                mHolder.signIn.setVisibility(View.INVISIBLE);

                mListener.onLogin(mHolder.username.getText().toString(), mHolder.password.getText().toString());
            }
        }
    }

    public void showProgress() {
        mHolder.username.setVisibility(View.INVISIBLE);
        mHolder.password.setVisibility(View.INVISIBLE);
        mHolder.forgotPassword.setVisibility(View.INVISIBLE);
        mHolder.signIn.setVisibility(View.INVISIBLE);
        mHolder.progress.setVisibility(View.VISIBLE);
    }

    public void showContent() {
        mHolder.username.setVisibility(View.VISIBLE);
        mHolder.password.setVisibility(View.VISIBLE);
        mHolder.forgotPassword.setVisibility(View.VISIBLE);
        mHolder.signIn.setVisibility(View.VISIBLE);
        mHolder.progress.setVisibility(View.INVISIBLE);
    }


    public interface Listener {
        void onLogin(String username, String password);
        void onForgotPassword();
    }

    private static final class ViewHolder {
        public final View root;
        public final ImageView imgviewLogo;
        public final EditText username;
        public final EditText password;
        public final View forgotPassword;
        public final View signIn;
        public final View progress;

        public ViewHolder(View root) {
            this.root = root;
            imgviewLogo = (ImageView) root.findViewById(R.id.imgview_logo);
            username = (EditText) root.findViewById(R.id.username);
            password = (EditText) root.findViewById(R.id.password);
            forgotPassword = root.findViewById(R.id.forgotPassword);
            signIn = root.findViewById(R.id.signIn);
            progress = root.findViewById(R.id.progress);
        }
    }

    public boolean loadPreferences() {

        int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(MYPREFS,
                mode);
        // Retrieve the saved values.
        boolean flag = mySharedPreferences.getBoolean("isSetting", false);
        if (flag == true){
            savedUsername = mySharedPreferences.getString("username", "");
            savedPassword = mySharedPreferences.getString("userpass", "");
        }
        return flag;

    }

}
