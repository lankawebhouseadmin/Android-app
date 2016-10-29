package com.bethere24system.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bethere24system.R;
import com.bethere24system.data.State;

import java.util.Date;

/**
 * Created by Administrator on 3/5/2016.
 */
public class LeftMenuFragment extends Fragment implements View.OnClickListener {

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
        mHolder = new ViewHolder(inflater.inflate(R.layout.fragment_left_menu, container, false));
        mHolder.healthSummary.setOnClickListener(this);
        mHolder.healthScore.setOnClickListener(this);
        mHolder.alert.setOnClickListener(this);
        mHolder.graph.setOnClickListener(this);
        mHolder.contact.setOnClickListener(this);
        return mHolder.root;
    }

    @Override
    public void onClick(View v) {
        if (v == mHolder.healthSummary) {
            if (mListener != null) mListener.onHealthSummaryOpen(null, null);
        } else if (v == mHolder.healthScore) {
            if (mListener != null) mListener.onHealthScoreOpen(null, null);
        } else if (v == mHolder.alert) {
            if (mListener != null) mListener.onAlertSummaryOpen(null, null, 0);
        } else if (v == mHolder.graph) {
            if (mListener != null) mListener.onHistoricalGraphsOpen(null, null);
        } else if (v == mHolder.contact) {
            if (mListener != null) mListener.onContactInfoOpen();
        }
    }


    public interface Listener {
        void onHealthSummaryOpen(Date date, State state);
        void onHealthScoreOpen(Date date, State state);
        void onAlertSummaryOpen(Date date, State state, int linkEnableMode);
        void onHistoricalGraphsOpen(Date date, State state);
        void onContactInfoOpen();
    }

    private static final class ViewHolder {

        public final View root;
        public final View healthSummary;
        public final View healthScore;
        public final View alert;
        public final View graph;
        public final View contact;

        public ViewHolder(View root) {
            this.root = root;
            healthSummary = root.findViewById(R.id.health_summary);
            healthScore = root.findViewById(R.id.health_score);
            alert = root.findViewById(R.id.alert_summary);
            graph = root.findViewById(R.id.graph);
            contact = root.findViewById(R.id.contact);
        }

    }

}
