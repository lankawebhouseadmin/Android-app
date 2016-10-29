package com.bethere24system.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bethere24system.BeThereApplication;
import com.bethere24system.R;
import com.bethere24system.adapter.AlertAdapter;
import com.bethere24system.data.State;
import com.bethere24system.data.StateListContainer;
import com.bethere24system.data.StateType;
import com.bethere24system.widget.FilterView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 3/5/2016.
 */
public class AlertsFragment extends Fragment implements FilterView.Listener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM, dd", Locale.UK);
    private static final SimpleDateFormat DAYOFWEEK_FORMAT = new SimpleDateFormat("EEE", Locale.UK);

    public static final int LINK_ENABLE_NOTHING = 0;
    public static final int LINK_ENABLE_STATES = 1;
    public static final int LINK_ENABLE_SCORE = 2;
    public static final int LINK_ENABLE_GRAPH = 3;

    private ViewHolder mHolder;
    private Listener mListener;
    private AlertAdapter mAdapter;
    private Date mCurrentDate;
    private StateType mStateType;
    private State mCurrentState;
    private StateListContainer mStateListContainer;
    private int mLinkEnableMode;

    public static AlertsFragment newInstance(Date date, State state, int linkEnableMode) {
        AlertsFragment fragment = new AlertsFragment();
        fragment.mCurrentDate = date;
        fragment.mCurrentState = state;
        fragment.mLinkEnableMode = linkEnableMode;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) mListener = (Listener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mStateListContainer = BeThereApplication.getInstance().getData().stateListContainer;

        mHolder = new ViewHolder(inflater.inflate(R.layout.fragment_alerts, container, false));
        mHolder.filter.setOnFilterListener(this);
        mHolder.filter.setOnClickListener(this);
        mHolder.state.setOnClickListener(this);

        mAdapter = new AlertAdapter(BeThereApplication.getInstance().getData().alertListContainer.getAlerts());
        mHolder.list.setLayoutManager(new LinearLayoutManager(getContext()));
        mHolder.list.setAdapter(mAdapter);

        mHolder.dateLeft.setOnClickListener(this);
        mHolder.dateRight.setOnClickListener(this);
        mHolder.scoreLink.setOnClickListener(this);
        mHolder.statesLink.setOnClickListener(this);
        mHolder.graphLink.setOnClickListener(this);

        mHolder.weekCheck.setOnCheckedChangeListener(this);
        mHolder.dayCheck.setOnCheckedChangeListener(this);

        mHolder.stateContainer.setOnClickListener(this);
        mHolder.checkContainer.setOnClickListener(this);
        mHolder.title.setOnClickListener(this);

        if (mCurrentDate == null) {
            mHolder.weekCheck.setChecked(true);
        } else {
            mHolder.dayCheck.setChecked(true);
        }

        if (mCurrentState != null) {
            mStateType = mCurrentState.type;
            mHolder.checkContainer.setVisibility(View.GONE);
            mHolder.dateContainer.setVisibility(View.GONE);
            mHolder.stateContainer.setVisibility(View.GONE);
        }

        if (mLinkEnableMode == LINK_ENABLE_STATES) {
            mHolder.statesLink.setVisibility(View.VISIBLE);
            mHolder.scoreLink.setVisibility(View.GONE);
            mHolder.graphLink.setVisibility(View.GONE);
        } else if (mLinkEnableMode == LINK_ENABLE_SCORE) {
            mHolder.statesLink.setVisibility(View.GONE);
            mHolder.scoreLink.setVisibility(View.VISIBLE);
            mHolder.graphLink.setVisibility(View.GONE);
        } else if (mLinkEnableMode == LINK_ENABLE_GRAPH){
            mHolder.statesLink.setVisibility(View.GONE);
            mHolder.scoreLink.setVisibility(View.GONE);
            mHolder.graphLink.setVisibility(View.VISIBLE);
        }else {
            mHolder.statesLink.setVisibility(View.GONE);
            mHolder.scoreLink.setVisibility(View.GONE);
            mHolder.graphLink.setVisibility(View.GONE);
        }

        return mHolder.root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        onStateTypeSelected(mStateType);
    }

    @Override
    public void onStateTypeSelected(StateType type) {
        mStateType = type;
        mAdapter.filter(mCurrentDate, mStateType);
        if (type == null) {
            mHolder.state.setTextColor(getResources().getColor(R.color.text_dark));
            mHolder.state.setText(R.string.all);
            mHolder.state.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            mHolder.state.setTextColor(type.getColor(getContext()));
            mHolder.state.setText(type.getTitleRes());
            mHolder.state.setCompoundDrawablesRelativeWithIntrinsicBounds(type.getSmallIconRes(), 0, 0, 0);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mHolder.dateLeft) {
            setCurrentDate(mStateListContainer.getPrevDate(mCurrentDate), mStateType);
        } else if (v == mHolder.dateRight) {
            setCurrentDate(mStateListContainer.getNextDate(mCurrentDate), mStateType);
        } else if (v == mHolder.state) {
            mHolder.filter.toggleVisibility();
        } else if (v == mHolder.statesLink) {
            if (mListener != null) mListener.onHealthSummaryOpen(mCurrentDate, mCurrentState);
        } else if (v == mHolder.scoreLink) {
            if (mListener != null) mListener.onHealthScoreOpen(mCurrentDate, mCurrentState);
        } else if (v == mHolder.graphLink) {
            if (mListener != null) mListener.onHistoricalGraphsOpen(mCurrentDate, mCurrentState);
        } else if (v == mHolder.filter || v == mHolder.stateContainer || v == mHolder.checkContainer || v == mHolder.title) {
            mHolder.filter.setVisibility(View.GONE);
        }
    }

    private void setCurrentDate(Date date, StateType type) {
        mCurrentDate = date;
        mStateType = type;
        int index = mStateListContainer.getDays().indexOf(date);

        if (index == 0) {
            mHolder.dateLeft.setVisibility(View.INVISIBLE);
        } else {
            mHolder.dateLeft.setVisibility(View.VISIBLE);
        }

        if (index == mStateListContainer.getDays().size() - 1) {
            mHolder.dateRight.setVisibility(View.INVISIBLE);
        } else {
            mHolder.dateRight.setVisibility(View.VISIBLE);
        }

        if (mCurrentDate != null) {
            mHolder.date.setText(DateUtils.isToday(mCurrentDate.getTime()) ? getString(R.string.today) : convertDate(mCurrentDate));
        }

        if (type == null) {
            mHolder.state.setTextColor(getResources().getColor(R.color.text_dark));
            mHolder.state.setText(R.string.all);
            mHolder.state.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            mHolder.state.setTextColor(type.getColor(getContext()));
            mHolder.state.setText(type.getTitleRes());
            mHolder.state.setCompoundDrawablesRelativeWithIntrinsicBounds(type.getSmallIconRes(), 0, 0, 0);
        }

        mAdapter.filter(mCurrentDate, mStateType);

    }

    private String convertDate(Date date) {

        int month = date.getMonth() + 1;
        int day = date.getDate();

        return String.format("%d/%d - %s.", month, day, DAYOFWEEK_FORMAT.format(date));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mHolder.filter.setVisibility(View.GONE);
        if (buttonView == mHolder.dayCheck && isChecked) {
            if (mCurrentDate == null) mCurrentDate = mStateListContainer.getDays().get(mStateListContainer.getDays().size() - 1);
            mStateType = null;
            mHolder.dateContainer.setVisibility(View.VISIBLE);
            mHolder.stateContainer.setVisibility(View.GONE);
            setCurrentDate(mCurrentDate, mStateType);
        } else if (buttonView == mHolder.weekCheck && isChecked) {
            mCurrentDate = null;
            mHolder.dateContainer.setVisibility(View.GONE);
            mHolder.stateContainer.setVisibility(View.VISIBLE);
            setCurrentDate(mCurrentDate, mStateType);
        }
    }

    public interface Listener {
        void onHealthSummaryOpen(Date date, State state);
        void onHealthScoreOpen(Date date, State state);
        void onHistoricalGraphsOpen(Date date, State state);
    }

    private static final class ViewHolder {

        public final View root;
        public final TextView state;
        public final FilterView filter;
        public final RecyclerView list;
        public final View title;
        public final View dateContainer;
        public final View stateContainer;
        public final View checkContainer;
        public final RadioButton dayCheck;
        public final RadioButton weekCheck;
        public final View dateLeft;
        public final View dateRight;
        public final TextView date;
        public final View statesLink;
        public final View scoreLink;
        public final View graphLink;

        public ViewHolder(View root) {
            this.root = root;
            state = (TextView) root.findViewById(R.id.state);
            filter  = (FilterView) root.findViewById(R.id.filter);
            list = (RecyclerView) root.findViewById(R.id.list);
            title = (FrameLayout) root.findViewById(R.id.FLTitle);
            dateContainer = root.findViewById(R.id.date_container);
            stateContainer = root.findViewById(R.id.state_container);
            checkContainer = root.findViewById(R.id.check_container);
            dayCheck = (RadioButton) root.findViewById(R.id.day_check);
            weekCheck = (RadioButton) root.findViewById(R.id.week_check);
            dateLeft = root.findViewById(R.id.date_left);
            dateRight = root.findViewById(R.id.date_right);
            date = (TextView) root.findViewById(R.id.date);
            statesLink = root.findViewById(R.id.state_link);
            scoreLink = root.findViewById(R.id.score_link);
            graphLink = root.findViewById(R.id.alert_link);
        }

    }

}
