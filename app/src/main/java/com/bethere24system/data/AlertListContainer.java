package com.bethere24system.data;

import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 3/5/2016.
 */
public class AlertListContainer {

    private List<Alert> mAlerts;
    private LongSparseArray<HashMap<StateType, List<Alert>>> mAlertsByDateAndType;
    private LongSparseArray<List<Alert>> mAlertsByDate;

    public AlertListContainer(List<com.bethere24system.transport.data.Alert> alerts) {
        mAlertsByDateAndType = new LongSparseArray<>();
        mAlerts = new ArrayList<>(alerts.size());
        mAlertsByDate = new LongSparseArray<>();

        HashMap<StateType, List<Alert>> alertsByState;
        List<Alert> alertList;

        Alert a;
        for (com.bethere24system.transport.data.Alert alert : alerts) {

            a = new Alert(alert);
            this.mAlerts.add(new Alert(alert));

            alertsByState = mAlertsByDateAndType.get(a.day.getTime());
            if (alertsByState == null) {
                alertsByState = new HashMap<>();
                mAlertsByDateAndType.append(a.day.getTime(), alertsByState);
            }

            alertList = alertsByState.get(a.stateType);
            if (alertList == null) {
                alertList = new ArrayList<>();
                alertsByState.put(a.stateType, alertList);
            }
            alertList.add(a);

            alertList = mAlertsByDate.get(a.day.getTime());
            if (alertList == null) {
                alertList = new ArrayList<>();
                mAlertsByDate.put(a.day.getTime(), alertList);
            }
            alertList.add(a);

        }
    }

    public List<Alert> getAlerts() {
        return mAlerts;
    }

    public List<Alert> getAlerts(Date day, StateType type) {
        Map<StateType, List<Alert>> alerts = mAlertsByDateAndType.get(day.getTime());
        if (alerts == null) return null;
        return alerts.get(type);
    }

    public List<Alert> getAlerts(Date day) {
        return mAlertsByDate.get(day.getTime());
    }

}
