package com.bethere24system.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bethere24system.R;
import com.bethere24system.data.Alert;
import com.bethere24system.data.StateType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 3/5/2016.
 */
public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder> {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("MM/dd/yyyy\nhh:mm a", Locale.UK);

    private List<Alert> mAllAlerts;
    private List<Alert> mAlerts;

    public AlertAdapter(List<Alert> alerts) {
        mAllAlerts = new ArrayList<>(alerts);
        mAlerts = new ArrayList<>(alerts);
    }

    public void filter(Date date, StateType type) {
        List<Alert> newAlerts;
        if (type == null && date == null) {
            newAlerts = new ArrayList<>(mAllAlerts);
        } else {
            newAlerts = new ArrayList<>();
            for (Alert alert : mAllAlerts) {
                if (type == null || alert.stateType == type) {
                    if (date == null || date.getTime() == alert.day.getTime()) {
                        newAlerts.add(alert);
                    }
                }
            }
        }
        animateTo(newAlerts);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_alert, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Alert alert = mAlerts.get(position);

        StateType stateType = alert.stateType;
        if (stateType != null) {
            holder.icon.setImageResource(stateType.getSmallWhiteIconRes());
            holder.icon.setBackgroundColor(stateType.getColor(holder.icon.getContext()));
            holder.title.setTextColor(stateType.getColor(holder.title.getContext()));

            holder.title.setText(stateType.getTitleRes());
        }

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        String strTime = String.format("%d/%d/%d\n%d:%d %s", alert.time.getMonth() + 1, alert.time.getDate(), alert.time.getYear() + 1900,
                                                                alert.time.getHours() > 12 ? alert.time.getHours() - 12 : alert.time.getHours(), alert.time.getMinutes(),
                                                                TIME_FORMAT.format(alert.time).split(" ")[1]);

        holder.time.setText(strTime);
        holder.normalTime.setText("Normal: " + alert.normalTime);
//        holder.actualTime.setText("Actual: " + alert.actualTimeChanged);
        holder.actualTime.setText("Actual: " + alert.actualTime); // Dennis

        holder.actualTime.setTextColor(alert.score.getColor(holder.actualTime.getContext()));

        holder.message.setText(alert.message);

    }

    @Override
    public int getItemCount() {
        return mAlerts.size();
    }

    public void animateTo(List<Alert> alerts) {
        applyAndAnimateRemovals(alerts);
        applyAndAnimateAdditions(alerts);
        applyAndAnimateMovedItems(alerts);
    }

    private void applyAndAnimateRemovals(List<Alert> newAlerts) {
        for (int i = mAlerts.size() - 1; i >= 0; i--) {
            final Alert model = mAlerts.get(i);
            if (!newAlerts.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Alert> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Alert model = newModels.get(i);
            if (!mAlerts.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Alert> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Alert model = newModels.get(toPosition);
            final int fromPosition = mAlerts.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Alert removeItem(int position) {
        final Alert model = mAlerts.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Alert model) {
        if (position > mAlerts.size()) {
            mAlerts.add(model);
        } else {
            mAlerts.add(position, model);
        }
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        if (toPosition >= mAlerts.size()) return;
        final Alert model = mAlerts.remove(fromPosition);
        mAlerts.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    protected static final class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView icon;
        public final TextView title;
        public final TextView time;
        public final TextView normalTime;
        public final TextView actualTime;
        public final TextView message;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            normalTime = (TextView) itemView.findViewById(R.id.normal_time);
            actualTime = (TextView) itemView.findViewById(R.id.actual_time);
            message = (TextView) itemView.findViewById(R.id.message);
        }
    }

}
