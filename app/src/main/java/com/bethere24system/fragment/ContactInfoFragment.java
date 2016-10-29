package com.bethere24system.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bethere24system.R;

/**
 * Created by Administrator on 3/5/2016.
 */
public class ContactInfoFragment extends Fragment {

    private ViewHolder mHolder;
    private Listener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) mListener = (Listener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHolder = new ViewHolder(inflater.inflate(R.layout.fragment_contact, container, false));

        return mHolder.root;
    }

    public interface Listener {

    }

    private static final class ViewHolder {

        public final View root;

        public ViewHolder(View root) {
            this.root = root;
        }

    }

}
