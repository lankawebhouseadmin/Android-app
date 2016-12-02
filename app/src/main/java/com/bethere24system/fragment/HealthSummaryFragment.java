package com.bethere24system.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bethere24system.BeThereApplication;
import com.bethere24system.R;
import com.bethere24system.data.Alert;
import com.bethere24system.data.AlertListContainer;
import com.bethere24system.data.State;
import com.bethere24system.data.StateListContainer;
import com.bethere24system.data.StateType;
import com.bethere24system.transport.data.Data;
import com.bethere24system.utils.ConvertUtils;
import com.bethere24system.widget.StateCircleView;
import com.github.pwittchen.prefser.library.Prefser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 3/5/2016.
 */
public class HealthSummaryFragment extends Fragment implements View.OnClickListener, StateCircleView.Listener {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM, dd", Locale.UK);
    private static final SimpleDateFormat DAYOFWEEK_FORMAT = new SimpleDateFormat("EEE", Locale.UK);
    private static final String STATE_TIME_PATTERN = "<b>Normal:</b> %s, <b>Total:</b> %s";
    private static final String TIMES_PATTERN = "%d times";
    private static final String TIMES_PATTERN1 = "%d time";

    private ViewHolder mHolder;
    private Listener mListener;
    private StateListContainer mStateListContainer;
    private AlertListContainer mAlertListContainer;
    private Date mCurrentDate;
    private State mCurrentState;
    private Data mData;

    public static HealthSummaryFragment newInstance(Date date, State state) {

        HealthSummaryFragment fragment = new HealthSummaryFragment();
        fragment.mCurrentDate = date;
        fragment.mCurrentState = state;
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

//        mStateListContainer = BeThereApplication.getInstance().getData().stateListContainer;
//        mAlertListContainer = BeThereApplication.getInstance().getData().alertListContainer;

        mData = new Prefser(getContext()).get("data", Data.class, new Data());

        mHolder = new ViewHolder(inflater.inflate(R.layout.fragment_health_summary, container, false));

        mHolder.dateLeft.setOnClickListener(this);
        mHolder.dateRight.setOnClickListener(this);
        mHolder.stateLeft.setOnClickListener(this);
        mHolder.stateRight.setOnClickListener(this);
        mHolder.linkToGraph.setOnClickListener(this);
        mHolder.linkToScore.setOnClickListener(this);
        mHolder.linkToAlerts.setOnClickListener(this);

        mHolder.stateCircle.setOnStateListener(this);

//        if (mCurrentDate == null) mCurrentDate = mStateListContainer.getDays().get(mStateListContainer.getDays().size() - 1);
//        setCurrentDate(mCurrentDate, mCurrentState);
        refreshData();

        return mHolder.root;
    }

    @Override
    public void onClick(View v) {
        if (v == mHolder.dateLeft) {
            setCurrentDate(mStateListContainer.getPrevDate(mCurrentDate), mCurrentState);
        } else if (v == mHolder.dateRight) {
            setCurrentDate(mStateListContainer.getNextDate(mCurrentDate), mCurrentState);
        } else if (v == mHolder.stateLeft) {
            mHolder.stateCircle.prevState();
        } else if (v == mHolder.stateRight) {
            mHolder.stateCircle.nextState();
        } else if (v == mHolder.linkToGraph) {
            if (mListener != null) mListener.onHistoricalGraphsOpen(mCurrentDate, mCurrentState);
        } else if (v == mHolder.linkToScore) {
            if (mListener != null) mListener.onHealthScoreOpen(mCurrentDate, mCurrentState);
        } else if (v == mHolder.linkToAlerts) {
            if (mListener != null) mListener.onAlertSummaryOpen(mCurrentDate, mCurrentState, AlertsFragment.LINK_ENABLE_STATES);
        }
    }

    public void refreshData() {
        mStateListContainer = BeThereApplication.getInstance().getData().stateListContainer;
        mAlertListContainer = BeThereApplication.getInstance().getData().alertListContainer;

        mCurrentDate = mStateListContainer.getDays().get(mStateListContainer.getDays().size() - 1);

        setCurrentDate(mCurrentDate, mCurrentState);
        mHolder.stateCircle.refreshData();
        onStateSelected(mCurrentState);
    }

    private void setCurrentDate(Date date, State state) {
        mCurrentDate = date;
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

        if (state != null) {
            mHolder.stateCircle.setStates(mStateListContainer.getStatesByType(mCurrentDate), state.type);
        } else {
            mHolder.stateCircle.setStates(mStateListContainer.getStatesByType(mCurrentDate));
        }

        mHolder.date.setText(mCurrentState.isToday ? getString(R.string.today) : convertDate(mCurrentDate));
    }

    private String convertDate(Date date) {

        int month = date.getMonth() + 1;
        int day = date.getDate();

        return String.format("%d/%d - %s.", month, day, DAYOFWEEK_FORMAT.format(date));
    }

    private String getTotalTime(StateType type, Date date) {

        int total = 0;

        List<State> states = mStateListContainer.getStates(type, date);

        if (states != null) {
            for (State state : states)
                total += state.actualTime;
        }

        return ConvertUtils.convertFromMinutesToHours(total);
    }

    private int getTimesByStateAndDate(StateType type, Date date) {

        return mStateListContainer.getStates(type, date).size();
    }

    @Override
    public void onStateSelected(State state) {
        mCurrentState = state;
        if (mCurrentState == null || TextUtils.isEmpty(state.message)) {
            mHolder.stateMessage.setText("No data available");
            mHolder.stateTime.setVisibility(View.INVISIBLE);
            mHolder.linkToScore.setVisibility(View.INVISIBLE);
            mHolder.linkToGraph.setVisibility(View.INVISIBLE);
            mHolder.linkToAlerts.setVisibility(View.GONE);
            mHolder.times.setVisibility(View.GONE);
            mHolder.alerts.setVisibility(View.GONE);
        } else {
            mHolder.stateTime.setVisibility(View.VISIBLE);
            mHolder.linkToScore.setVisibility(View.VISIBLE);
            mHolder.linkToGraph.setVisibility(View.VISIBLE);
            mHolder.times.setVisibility(View.VISIBLE);
            mHolder.stateMessage.setText(mData.getFirstname() + " " + state.message);

            mHolder.stateTime.setText(Html.fromHtml(String.format(Locale.UK, STATE_TIME_PATTERN, state.normalTime,
                                        state.type != StateType.MEDICATION ?
                                            getTotalTime(state.type, mCurrentDate) :
                                                getTimesByStateAndDate(state.type, mCurrentDate) > 1 ?
                                                        String.format("%d times", getTimesByStateAndDate(state.type, mCurrentDate)) :
                                                        String.format("%d time", getTimesByStateAndDate(state.type, mCurrentDate)))));

            mHolder.times.setText(Html.fromHtml(String.format(Locale.UK, getTimesByStateAndDate(state.type, mCurrentDate) > 1 ? TIMES_PATTERN : TIMES_PATTERN1,
                                                                            getTimesByStateAndDate(state.type, mCurrentDate))));

            List<Alert> alerts = mAlertListContainer.getAlerts(mCurrentDate, state.type);
            if (alerts == null || alerts.size() == 0) {
                mHolder.alerts.setVisibility(View.GONE);
                mHolder.linkToAlerts.setVisibility(View.GONE);
            } else {
                mHolder.alerts.setVisibility(View.VISIBLE);
                mHolder.alerts.setText(alerts.size() + " Alert" + (alerts.size() > 1 ? "s" : ""));
                mHolder.linkToAlerts.setVisibility(View.VISIBLE);
            }

            alerts = mAlertListContainer.getAlerts(mCurrentDate);
            if (mListener != null) mListener.onAlertCountChanged(mCurrentDate, mCurrentState,
                    AlertsFragment.LINK_ENABLE_STATES, alerts != null ? alerts.size() : 0);

        }

        if (state != null) {
            mHolder.stateType.setText(state.type.getTitleRes());
            mHolder.stateType.setTextColor(state.type.getColor(getContext()));
            mHolder.stateType.setCompoundDrawablesRelativeWithIntrinsicBounds(state.type.getSmallIconRes(), 0, 0, 0);
        }
    }

    public interface Listener {
        void onHealthScoreOpen(Date date, State state);
        void onAlertSummaryOpen(Date date, State state, int linkEnableMode);
        void onHistoricalGraphsOpen(Date date, State state);
        void onAlertCountChanged(Date date, State state, int linkEnableMode, int count);
    }

    private static final class ViewHolder {

        public final View root;
        public final StateCircleView stateCircle;
        public final View dateLeft;
        public final View dateRight;
        public final TextView date;
        public final TextView stateTime;
        public final TextView stateMessage;
        public final View stateLeft;
        public final View stateRight;
        public final TextView stateType;
        public final View linkToScore;
        public final View linkToGraph;
        public final View linkToAlerts;
        public final TextView times;
        public final TextView alerts;

        public ViewHolder(View root) {
            this.root = root;
            this.stateCircle = (StateCircleView) root.findViewById(R.id.circle);
            dateLeft = root.findViewById(R.id.date_left);
            dateRight = root.findViewById(R.id.date_right);
            date = (TextView) root.findViewById(R.id.date);
            stateTime = (TextView) root.findViewById(R.id.state_time);
            stateMessage = (TextView) root.findViewById(R.id.state_message);
            stateLeft = root.findViewById(R.id.state_left);
            stateRight = root.findViewById(R.id.state_right);
            stateType = (TextView) root.findViewById(R.id.state_type);
            linkToScore = root.findViewById(R.id.score_link);
            linkToAlerts = root.findViewById(R.id.alert_link);
            linkToGraph = root.findViewById(R.id.graph_link);
            times = (TextView) root.findViewById(R.id.times);
            alerts = (TextView) root.findViewById(R.id.alerts);
        }

    }

}
