package com.bethere24system.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.bethere24system.R;
import com.bethere24system.data.StateType;

/**
 * Created by Administrator on 3/5/2016.
 */
public class FilterView extends FrameLayout implements View.OnClickListener {

    private ViewHolder mHolder;
    private Listener mListener;

    public FilterView(Context context) {
        super(context);
        initView();
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.widget_filter_popup, this, false));
        mHolder = new ViewHolder(this);
        mHolder.filterAll.setOnClickListener(this);
        mHolder.filterBathroom.setOnClickListener(this);
        mHolder.filterVisitors.setOnClickListener(this);
        mHolder.filterDining.setOnClickListener(this);
        mHolder.filterBed.setOnClickListener(this);
        mHolder.filterAwayHome.setOnClickListener(this);
        mHolder.filterRecliner.setOnClickListener(this);
        mHolder.filterMedication.setOnClickListener(this);
        mHolder.filterMotion.setOnClickListener(this);
    }

    public void enableAllItem(boolean enable) {
        if (enable) {
            mHolder.filterAll.setVisibility(View.VISIBLE);
            mHolder.filterAllDevider.setVisibility(View.VISIBLE);
        } else {
            mHolder.filterAll.setVisibility(View.GONE);
            mHolder.filterAllDevider.setVisibility(View.GONE);
        }
    }

    public void setOnFilterListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v == mHolder.filterAll) {
            onStateStyleSelected(null);
        } else if (v == mHolder.filterBathroom) {
            onStateStyleSelected(StateType.IN_BATHROOM);
        } else if (v == mHolder.filterVisitors) {
            onStateStyleSelected(StateType.WITH_VISITORS);
        } else if (v == mHolder.filterDining) {
            onStateStyleSelected(StateType.IN_DINING);
        } else if (v == mHolder.filterBed) {
            onStateStyleSelected(StateType.SLEEPING);
        } else if (v == mHolder.filterAwayHome) {
            onStateStyleSelected(StateType.AWAY_FROM_HOME);
        } else if (v == mHolder.filterRecliner) {
            onStateStyleSelected(StateType.IN_RECLINER);
        } else if (v == mHolder.filterMedication) {
            onStateStyleSelected(StateType.MEDICATION);
        } else if (v == mHolder.filterMotion) {
            onStateStyleSelected(StateType.IN_MOTION);
        }
    }

    private void onStateStyleSelected(StateType type) {
        new Handler().postDelayed(FilterView.this::hide, 200);
        if (mListener != null) mListener.onStateTypeSelected(type);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void toggleVisibility() {
        if (getVisibility() == VISIBLE) {
            hide();
        } else {
            show();
        }
    }

    public interface Listener {
        void onStateTypeSelected(StateType type);
    }

    private static final class ViewHolder {

        public final View filterContainer;
        public final View filterAll;
        public final View filterAllDevider;
        public final View filterBathroom;
        public final View filterVisitors;
        public final View filterDining;
        public final View filterBed;
        public final View filterAwayHome;
        public final View filterRecliner;
        public final View filterMedication;
        public final View filterMotion;

        public ViewHolder(View root) {
            filterContainer = root.findViewById(R.id.filter_container);
            filterAll = root.findViewById(R.id.filter_item_all);
            filterAllDevider = root.findViewById(R.id.filter_item_all_divider);
            filterBathroom = root.findViewById(R.id.filter_item_bathroom);
            filterVisitors = root.findViewById(R.id.filter_item_visitors);
            filterDining = root.findViewById(R.id.filter_item_dining);
            filterBed = root.findViewById(R.id.filter_item_bed);
            filterAwayHome = root.findViewById(R.id.filter_item_awayhome);
            filterRecliner = root.findViewById(R.id.filter_item_recliner);
            filterMedication = root.findViewById(R.id.filter_item_medication);
            filterMotion = root.findViewById(R.id.filter_item_motion);
        }

    }

}
