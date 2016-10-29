package com.bethere24system.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bethere24system.BeThereApplication;
import com.bethere24system.R;
import com.bethere24system.data.Alert;
import com.bethere24system.data.AlertListContainer;
import com.bethere24system.data.Graph;
import com.bethere24system.data.State;
import com.bethere24system.data.StateListContainer;
import com.bethere24system.data.StateType;
import com.bethere24system.widget.FilterView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 3/5/2016.
 */
public class GraphsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, FilterView.Listener, OnChartValueSelectedListener {

    private ViewHolder mHolder;
    private Listener mListener;
    private StateListContainer mStateListContainer;
    private AlertListContainer mAlertListContainer;
    private StateType mStateType;
    private State mCurrentState;
    private Date mCurrentDate;


    public static GraphsFragment newInstance(Date date, State state) {
        GraphsFragment fragment = new GraphsFragment();
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

        mHolder = new ViewHolder(inflater.inflate(R.layout.fragment_graphs, container, false));

        mHolder.filter.enableAllItem(false);
        mHolder.filter.setOnFilterListener(this);
        mHolder.filter.setOnClickListener(this);

        mHolder.week.setOnCheckedChangeListener(this);
        mHolder.month.setOnCheckedChangeListener(this);
        mHolder.year.setOnCheckedChangeListener(this);

        mHolder.linkToHealth.setOnClickListener(this);
        mHolder.linkToScore.setOnClickListener(this);
        mHolder.linkToAlerts.setOnClickListener(this);
        mHolder.stateType.setOnClickListener(this);

        mHolder.stateGraph.setOnChartValueSelectedListener(this);
        mHolder.stateGraph.setDrawGridBackground(false);
        mHolder.stateGraph.setDragEnabled(false);
        mHolder.stateGraph.setScaleEnabled(false);

        if (mCurrentDate == null) mCurrentDate = mStateListContainer.getDays().get(mStateListContainer.getDays().size() - 1);
        if (mStateType == null) mStateType = StateType.IN_BATHROOM;

        mHolder.monthMark.setText(getMonthFromVirtualDay());
        onStateTypeSelected(mStateType);

        return mHolder.root;
    }

    private String getMonthFromVirtualDay() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        SimpleDateFormat format = new SimpleDateFormat("MMMM-dd-yyyy");
        String formattedDate = format.format(date);

        return formattedDate.split("-")[0];
    }

    private void drawGraph(StateType type) {

        ArrayList<Graph> graphs = new ArrayList<>();

        for (Date dateOfWeek : mStateListContainer.getDays()) {
            float totalTime = 0f;

            List<State> states = mStateListContainer.getStates(type, dateOfWeek);

            if (states != null) {
                for (State state : states) {
//
//                    String[] actualTime = state.actualTime.indexOf(", ") > 0 ? state.actualTime.split(", ") :
//                                            state.actualTime.indexOf(String.valueOf(Character.toChars(160))) > 0 ? state.actualTime.split(String.valueOf(Character.toChars(160))) :
//                                            state.actualTime.split(" ");
//
//                    float time = Float.parseFloat(actualTime[0].indexOf(" ") > 0 ? actualTime[0].split(" ")[0] :
//                                                    actualTime[0].indexOf(String.valueOf(Character.toChars(160))) > 0 ? actualTime[0].split(String.valueOf(Character.toChars(160)))[0] :
//                                                    String.format("%s", Float.parseFloat(actualTime[0]) / 60f));
//                    time *= 60f;
//                    time += Float.parseFloat(actualTime[1].indexOf(" ") > 0 ? actualTime[1].split(" ")[0] :
//                                             actualTime[1].indexOf(String.valueOf(Character.toChars(160))) > 0 ? actualTime[1].split(String.valueOf(Character.toChars(160)))[0] :
//                                                     "0");

//                    totalTime += Float.parseFloat(actualTime[0]);
                    totalTime += (float)state.actualTime;
                }
            }

            graphs.add(new Graph(dateOfWeek, totalTime));
        }

        setData(graphs);
    }

    private void setData(ArrayList<Graph> graphs) {

        float maxYAxis = 0f;
        ArrayList<String> xValues = new ArrayList<String>();

        for (int i = 0; i < graphs.size() + 2; i++) {

            if (i == 0 || i == graphs.size() + 1) {
                xValues.add("");
                continue;
            }

            xValues.add(String.format("%d/%d", graphs.get(i - 1).day.getMonth() + 1, graphs.get(i - 1).day.getDate()));
        }

        ArrayList<Entry> yValues = new ArrayList<Entry>();

        for (int i = 1; i < graphs.size() + 1; i++) {

            if (graphs.get(i - 1).totalTime > maxYAxis)
                maxYAxis = graphs.get(i - 1).totalTime;

            yValues.add(new Entry(graphs.get(i - 1).totalTime / 60.0f + 0.01f, i));
        }

        mHolder.stateGraph.clear();
        mHolder.stateGraph.setDescription("");
        mHolder.stateGraph.animateX(1500, Easing.EasingOption.EaseInOutQuart);

        Legend legend = mHolder.stateGraph.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        YAxis leftAxis = mHolder.stateGraph.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaxValue(24f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setAxisLineWidth(2f);
        leftAxis.setAxisLineColor(Color.BLACK);
        leftAxis.setZeroLineWidth(2f);
        leftAxis.setZeroLineColor(Color.BLACK);
        leftAxis.setYOffset(1f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setDrawZeroLine(true);

        mHolder.stateGraph.getAxisRight().setEnabled(false);

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yValues, "");

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setValueFormatter(new LargeValueFormatter());

//        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
//        set1.setFillDrawable(drawable);
//        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);

        // create a data object with the datasets
        LineData data = new LineData(xValues, dataSets);

        // set data
        mHolder.stateGraph.setData(data);
    }
    
    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

     }
    
    @Override
    public void onClick(View v) {

        if (v == mHolder.linkToHealth) {
            if (mListener != null) mListener.onHealthSummaryOpen(mCurrentDate, mCurrentState);
        } else if (v == mHolder.linkToScore) {
            if (mListener != null) mListener.onHealthScoreOpen(mCurrentDate, mCurrentState);
        } else if (v == mHolder.linkToAlerts) {
            if (mListener != null) mListener.onAlertSummaryOpen(mCurrentDate, mCurrentState, AlertsFragment.LINK_ENABLE_GRAPH);
        } else if (v == mHolder.stateType) {
            mHolder.filter.toggleVisibility();
            if (mHolder.filter.getVisibility() == View.VISIBLE) {
                mHolder.scroll.scrollTo(0, 0);
            }
        }else if (v == mHolder.filter) {
            mHolder.filter.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onStateTypeSelected(StateType type) {
        mStateType = type;
        mCurrentState = mStateListContainer.getStates(type) != null ? mStateListContainer.getStates(type).get(0) : mCurrentState;
        mCurrentDate = mCurrentState.day;

        mHolder.stateType.setText(type.getTitleRes());
        mHolder.stateType.setTextColor(type.getColor(getContext()));
        mHolder.stateType.setCompoundDrawablesRelativeWithIntrinsicBounds(type.getSmallIconRes(), 0, 0, 0);

        List<Alert> alerts = mAlertListContainer.getAlerts();
        int alertCount = 0;

        for (Alert alert : alerts) {
            if (alert.stateType != null) {
                if (alert.stateType.name().equals(type.name())) {
                    alertCount++;

                    mCurrentState = mStateListContainer.getStates(alert.stateType).get(0);
                    mCurrentDate = alert.day;
                }
            }
        }

        if (alertCount > 0)
            mHolder.linkToAlerts.setVisibility(View.VISIBLE);
        else
            mHolder.linkToAlerts.setVisibility(View.GONE);

        if (mListener != null) mListener.onAlertCountChanged(mCurrentDate, mCurrentState,
                                                            AlertsFragment.LINK_ENABLE_GRAPH, alertCount);

        drawGraph(mStateType);
    }

    public interface Listener {
        void onHealthSummaryOpen(Date date, State state);
        void onHealthScoreOpen(Date date, State state);
        void onAlertSummaryOpen(Date date, State state, int linkEnableMode);
        void onAlertCountChanged(Date date, State state, int linkEnableMode, int count);
    }

    private static final class ViewHolder {

        public final View root;
        public final CheckBox week;
        public final CheckBox month;
        public final CheckBox year;
        public final LineChart stateGraph;
        public final FilterView filter;
        public final View stateLeft;
        public final View stateRight;
        public final View linkToHealth;
        public final View linkToScore;
        public final View linkToAlerts;
        public final TextView stateType;
        public final ScrollView scroll;
        public final TextView monthMark;

        public ViewHolder(View root) {
            this.root = root;
            week = (CheckBox) root.findViewById(R.id.week);
            month = (CheckBox) root.findViewById(R.id.month);
            year = (CheckBox) root.findViewById(R.id.year);
            stateGraph = (LineChart) root.findViewById(R.id.chartStateGraph);
            scroll = (ScrollView) root.findViewById(R.id.scroll);
            filter = (FilterView) root.findViewById(R.id.filter);

            stateLeft = root.findViewById(R.id.state_left);
            stateRight = root.findViewById(R.id.state_right);
            linkToHealth = root.findViewById(R.id.health_link);
            linkToScore = root.findViewById(R.id.score_link);
            linkToAlerts = root.findViewById(R.id.alert_link);
            stateType = (TextView) root.findViewById(R.id.state);
            monthMark = (TextView) root.findViewById(R.id.mark_month);
        }

    }

}
