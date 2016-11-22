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
import android.widget.ScrollView;
import android.widget.TextView;

import com.bethere24system.BeThereApplication;
import com.bethere24system.R;
import com.bethere24system.data.Alert;
import com.bethere24system.data.AlertListContainer;
import com.bethere24system.data.State;
import com.bethere24system.data.StateListContainer;
import com.bethere24system.data.StateType;
import com.bethere24system.utils.ConvertUtils;
import com.bethere24system.widget.FilterView;
import com.bethere24system.widget.ScoreCircleView;
import com.bethere24system.transport.data.Data;
import com.github.pwittchen.prefser.library.Prefser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 3/5/2016.
 */
public class HealthScoreFragment extends Fragment implements View.OnClickListener, ScoreCircleView.Listener, FilterView.Listener {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a", Locale.UK);
    private static final SimpleDateFormat DAYOFWEEK_FORMAT = new SimpleDateFormat("EEE", Locale.UK);
    private static final String TIME_PATTERN = "<b>Normal:</b> %s, <b>Total:</b> %s";
    private static final String DURATION_PATTERN = "<b>From:</b> %s, <b>To:</b> %s, <b>Actual:</b> %s";
    private static final String TIMES_PATTERN = "%d times";
    private static final String TIMES_PATTERN1 = "%d time";

    private ViewHolder mHolder;
    private Listener mListener;
    private StateListContainer mStateListContainer;
    private AlertListContainer mAlertListContainer;
    private Date mCurrentDate;
    private StateType mStateType;
    private State mCurrentState;
    private Data mData;

    public static HealthScoreFragment newInstance(Date date, State state) {
        HealthScoreFragment fragment = new HealthScoreFragment();
        fragment.mCurrentDate = date;
        fragment.mCurrentState = state;
        fragment.mStateType = state != null ? state.type : null;
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
        mAlertListContainer = BeThereApplication.getInstance().getData().alertListContainer;

        mData = new Prefser(getContext()).get("data", Data.class, new Data());

        mHolder = new ViewHolder(inflater.inflate(R.layout.fragment_health_score, container, false));

        mHolder.filter.enableAllItem(false);
        mHolder.filter.setOnFilterListener(this);
        mHolder.filter.setOnClickListener(this);

        mHolder.dateLeft.setOnClickListener(this);
        mHolder.dateRight.setOnClickListener(this);
        mHolder.stateLeft.setOnClickListener(this);
        mHolder.stateRight.setOnClickListener(this);
        mHolder.linkToGraph.setOnClickListener(this);
        mHolder.linkToHealth.setOnClickListener(this);
        mHolder.linkToAlerts.setOnClickListener(this);
        mHolder.stateType.setOnClickListener(this);
        mHolder.stateTypeTop.setOnClickListener(this);

        mHolder.stateCircle.setOnStateListener(this);

        if (mCurrentDate == null) mCurrentDate = mStateListContainer.getDays().get(mStateListContainer.getDays().size() - 1);
        if (mStateType == null) mStateType = StateType.IN_BATHROOM;
        onStateTypeSelected(mStateType);

        return mHolder.root;
    }

    @Override
    public void onClick(View v) {
        if (v == mHolder.dateLeft) {
            setCurrentDate(mStateListContainer.getPrevDate(mCurrentDate));
        } else if (v == mHolder.dateRight) {
            setCurrentDate(mStateListContainer.getNextDate(mCurrentDate));
        } else if (v == mHolder.stateLeft) {
            mHolder.stateCircle.prevState();
        } else if (v == mHolder.stateRight) {
            mHolder.stateCircle.nextState();
        }  else if (v == mHolder.linkToHealth) {
            if (mListener != null) mListener.onHealthSummaryOpen(mCurrentDate, mCurrentState);
        }else if (v == mHolder.linkToGraph) {
            if (mListener != null) mListener.onHistoricalGraphsOpen(mCurrentDate, mCurrentState);
        } else if (v == mHolder.linkToAlerts) {
            if (mListener != null) mListener.onAlertSummaryOpen(mCurrentDate, mCurrentState, AlertsFragment.LINK_ENABLE_SCORE);
        }else if (v == mHolder.stateType) {
            mHolder.filter.toggleVisibility();
            if (mHolder.filter.getVisibility() == View.VISIBLE) {
                mHolder.scroll.scrollTo(0,0);
            }
        } else if (v == mHolder.stateTypeTop) {
            mHolder.filter.toggleVisibility();
            if (mHolder.filter.getVisibility() == View.VISIBLE) {
                mHolder.scroll.scrollTo(0,0);
            }
        } else if (v == mHolder.filter) {
            mHolder.filter.setVisibility(View.GONE);
        }
    }

    private void setCurrentDate(Date date) {
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

        List<State> states = mStateListContainer.getStates(mStateType, mCurrentDate);

        if (mCurrentState != null) {
            mHolder.stateCircle.setStates(mCurrentDate, states, mCurrentState, mStateType);
        } else {
            mHolder.stateCircle.setStates(mCurrentDate, states, mStateType);
        }

        if (states == null || states.size() < 2) {
            mHolder.stateLeft.setVisibility(View.INVISIBLE);
            mHolder.stateRight.setVisibility(View.INVISIBLE);
        } else {
            mHolder.stateLeft.setVisibility(View.VISIBLE);
            mHolder.stateRight.setVisibility(View.VISIBLE);
        }

        if (mCurrentState != null)
            mHolder.date.setText(convertDate(mCurrentDate));
    }

    private String convertDate(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        Date prevDate = cal.getTime();

        int month = date.getMonth() + 1;
        int day = date.getDate();

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.UK);
        Date loginTime;
        try {
            loginTime =  timeFormat.parse(BeThereApplication.getInstance().getLoginTime());
        } catch (Exception e) {
            loginTime = new Date();
        }
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.UK);
        String timeString = timeFormat.format(loginTime);
        if (mCurrentState.isToday == false) {
            timeString = "6:00 am";
        }

        int prevMonth = prevDate.getMonth() + 1;
        int prevDay = prevDate.getDate();
//        String loginTime = BeThereApplication.getInstance().getLoginTime();
//        String time = loginTime.substring(11, 16);

        String headerTitle = String.format("%d/%d %s to %d/%d %s", prevMonth, prevDay, timeString, month, day, timeString);

        return headerTitle; //String.format("%d/%d - %s.", month, day, DAYOFWEEK_FORMAT.format(date));
    }

    private String getTotalTime(StateType type, Date date) {

        int total = 0;

        List<State> states = mStateListContainer.getStates(type, date);

        if (states != null) {
            for (State state : states) {
                total += state.actualTime;
            }
        }

        return ConvertUtils.convertFromMinutesToHours(total);
    }

    private int getTimesByStateAndDate(StateType type, Date date) {

        return mStateListContainer.getStates(type, date) != null ? mStateListContainer.getStates(type, date).size() : 0;
    }

    @Override
    public void onStateSelected(State state) {
        mCurrentState = state;

        if (state == null || TextUtils.isEmpty(state.message)) {
            mHolder.stateMessage.setText("No data available");
            mHolder.stateTime.setVisibility(View.INVISIBLE);
            mHolder.linkToHealth.setVisibility(View.INVISIBLE);
            mHolder.linkToGraph.setVisibility(View.INVISIBLE);
            mHolder.linkToAlerts.setVisibility(View.GONE);
            mHolder.stateDuration.setVisibility(View.INVISIBLE);
            mHolder.times.setVisibility(View.GONE);
            mHolder.alerts.setVisibility(View.GONE);
        } else {
            mHolder.stateTime.setVisibility(View.VISIBLE);
            mHolder.linkToHealth.setVisibility(View.VISIBLE);
            mHolder.linkToGraph.setVisibility(View.VISIBLE);
            mHolder.stateDuration.setVisibility(View.VISIBLE);
            mHolder.stateMessage.setText(mData.getFirstname() + " " + state.message);
            mHolder.times.setVisibility(View.VISIBLE);

//            TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            String startTime = TIME_FORMAT.format(state.startTime);
            String endTime = TIME_FORMAT.format(state.endTime);

            mHolder.stateTime.setText(Html.fromHtml(String.format(Locale.UK, TIME_PATTERN, state.normalTime,
                                        mStateType != StateType.MEDICATION ?
                                        getTotalTime(mStateType, mCurrentDate) :
                                                getTimesByStateAndDate(mStateType, mCurrentDate) > 1 ?
                                                        String.format("%d times", getTimesByStateAndDate(mStateType, mCurrentDate)) :
                                                        String.format("%d time", getTimesByStateAndDate(mStateType, mCurrentDate)))));


            mHolder.stateDuration.setText(Html.fromHtml(String.format(Locale.UK, DURATION_PATTERN,
                    startTime.indexOf("0") == 0 ? startTime.substring(0, 0) + "" + startTime.substring(1) : startTime,
                    endTime.indexOf("0") == 0 ? endTime.substring(0, 0) + "" + endTime.substring(1) : endTime,
                    mStateType != StateType.MEDICATION ? state.actualTimeChanged :
                            getTimesByStateAndDate(mStateType, mCurrentDate) > 1 ?
                            String.format("%d times", getTimesByStateAndDate(mStateType, mCurrentDate)) :
                            String.format("%d time", getTimesByStateAndDate(mStateType, mCurrentDate)))));

            mHolder.times.setText(Html.fromHtml(String.format(Locale.UK, getTimesByStateAndDate(mStateType, mCurrentDate) > 1 ? TIMES_PATTERN : TIMES_PATTERN1,
                                                                            getTimesByStateAndDate(mStateType, mCurrentDate))));

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
                    AlertsFragment.LINK_ENABLE_SCORE, alerts != null ? alerts.size() : 0);

        }

    }

    @Override
    public void onStateTypeSelected(StateType type) {
        mStateType = type;

        mHolder.stateType.setText(type.getTitleRes());
        mHolder.stateType.setTextColor(type.getColor(getContext()));
        mHolder.stateType.setCompoundDrawablesRelativeWithIntrinsicBounds(type.getSmallIconRes(), 0, 0, 0);

        mHolder.stateTypeTop.setText(type.getTitleRes());
        mHolder.stateTypeTop.setTextColor(type.getColor(getContext()));
        mHolder.stateTypeTop.setCompoundDrawablesRelativeWithIntrinsicBounds(type.getSmallIconRes(), 0, 0, 0);

        setCurrentDate(mCurrentDate);
    }

    public interface Listener {
        void onHealthSummaryOpen(Date date, State state);
        void onAlertSummaryOpen(Date date, State state, int linkEnableMode);
        void onHistoricalGraphsOpen(Date date, State state);
        void onAlertCountChanged(Date date, State state, int linkEnableMode, int count);
    }

    private static final class ViewHolder {

        public final View root;
        public final ScoreCircleView stateCircle;
        public final View dateLeft;
        public final View dateRight;
        public final TextView date;
        public final TextView stateTime;
        public final TextView stateDuration;
        public final TextView stateMessage;
        public final View stateLeft;
        public final View stateRight;
        public final TextView stateType;
        public final TextView stateTypeTop;
        public final View linkToHealth;
        public final View linkToGraph;
        public final View linkToAlerts;
        public final FilterView filter;
        public final TextView times;
        public final TextView alerts;
        public final ScrollView scroll;

        public ViewHolder(View root) {
            this.root = root;
            this.stateCircle = (ScoreCircleView) root.findViewById(R.id.circle);
            dateLeft = root.findViewById(R.id.date_left);
            dateRight = root.findViewById(R.id.date_right);
            date = (TextView) root.findViewById(R.id.date);
            stateTime = (TextView) root.findViewById(R.id.state_time);
            stateDuration = (TextView) root.findViewById(R.id.state_duration);
            stateMessage = (TextView) root.findViewById(R.id.state_message);
            stateLeft = root.findViewById(R.id.state_left);
            stateRight = root.findViewById(R.id.state_right);
            stateType = (TextView) root.findViewById(R.id.state_type);
            stateTypeTop = (TextView) root.findViewById(R.id.state_type_top);
            linkToHealth = root.findViewById(R.id.health_link);
            linkToAlerts = root.findViewById(R.id.alert_link);
            linkToGraph = root.findViewById(R.id.graph_link);
            filter = (FilterView) root.findViewById(R.id.filter);
            times = (TextView) root.findViewById(R.id.times);
            alerts = (TextView) root.findViewById(R.id.alerts);
            scroll = (ScrollView) root.findViewById(R.id.scroll);
        }

    }

}
