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
public class StateListContainer {
    private static final int NORMAL_TIME = 18;

    private ArrayList<State> mStates;
    private HashMap<StateType, List<State>> mStatesByType;
    private LongSparseArray<List<State>> mStatesByDay;
    private HashMap<StateType, LongSparseArray<List<State>>> mStatesByTypeAndDay;
    private LongSparseArray<HashMap<StateType, State>> mStateByDayAndTime;

    private List<Date> mDays;

    public StateListContainer(List<State> ... stateLists) {
        mStates = new ArrayList<>();
        mStatesByType = new HashMap<>();
        mStatesByDay = new LongSparseArray<>();
        mStatesByTypeAndDay = new HashMap<>();
        mStateByDayAndTime = new LongSparseArray<>();

        List<State> states;
        LongSparseArray<List<State>> statesByDate;
        HashMap<StateType, State> stateByType;

        LongSparseArray<Date> days = new LongSparseArray<>();

        for (List<State> stateList : stateLists) {
            for (State state : stateList) {
                mStates.add(state);

                states = mStatesByType.get(state.type);
                if (states == null) {
                    states = new ArrayList<>();
                    mStatesByType.put(state.type, states);
                }
                states.add(state);

                states = mStatesByDay.get(state.day.getTime());
                if (states == null) {
                    states = new ArrayList<>();
                    mStatesByDay.put(state.day.getTime(), states);
                }
                states.add(state);

                statesByDate = mStatesByTypeAndDay.get(state.type);
                if (statesByDate == null) {
                    statesByDate = new LongSparseArray<>();
                    mStatesByTypeAndDay.put(state.type, statesByDate);
                }

                states = statesByDate.get(state.day.getTime());
                if (states == null) {
                    states = new ArrayList<>();
                    statesByDate.put(state.day.getTime(), states);
                }
                states.add(state);

                stateByType = mStateByDayAndTime.get(state.day.getTime());
                if (stateByType == null) {
                    stateByType = new HashMap<>();
                    for (StateType stateType : StateType.values()) {
                        stateByType.put(stateType, new State(stateType, state.day));
                    }
                    mStateByDayAndTime.append(state.day.getTime(), stateByType);
                }

                if (stateByType.get(state.type).id == 0 || stateByType.get(state.type).score.getScore() > state.score.getScore())
                    stateByType.put(state.type, state);

                days.append(state.day.getTime(), state.day);

            }

            mDays = new ArrayList<>(days.size());
            for (int i = 0; i < days.size(); i++) {
                mDays.add(days.valueAt(i));
            }

        }

    }

    public List<State> getStates() {
        return mStates;
    }

    public List<State> getStates(StateType type) {
        return mStatesByType.get(type);
    }

    public List<State> getStates(Date time) {
        return mStatesByDay.get(time.getTime());
    }

    public List<State> getStates(StateType type, Date date) {
        LongSparseArray<List<State>> arr = mStatesByTypeAndDay.get(type);
        if (arr == null) return null;

        List<State> correctStates = new ArrayList<>();
        if (type == StateType.SLEEPING) {

            List<State> stateList = arr.get(date.getTime());
            for (int i = 0; i < stateList.size(); i++) {
                Object state = stateList.get(i);
                if ((state != null) && (state instanceof State)) {
                    if (((State)state).startTime.getHours() < NORMAL_TIME) {
                        correctStates.add(((State)state));
                    }
                }
            }

            if (mDays.indexOf(date) > 0) {
                List<State> arrState = arr.get(getPrevDate(date).getTime());
                if (arrState != null) {
                    for (State state : arrState) {
                        if (state.startTime.getHours() >= NORMAL_TIME)
                            correctStates.add(state);
                    }
                }

            }

        } else {

            correctStates = arr.get(date.getTime());
        }

        return correctStates;
//        return arr.get(date.getTime());
    }

    public Map<StateType, State> getStatesByType(Date day) {
        return mStateByDayAndTime.get(day.getTime());
    }

    public List<Date> getDays() {
        return mDays;
    }

    public Date getNextDate(Date day) {
        int index = mDays.indexOf(day);
        if (index == mDays.size() - 1) {
            return mDays.get(0);
        } else {
            return mDays.get(index + 1);
        }
    }

    public Date getPrevDate(Date day) {
        int index = mDays.indexOf(day);
        if (index <= 0) {
            return mDays.get(mDays.size() - 1);
        } else {
            return mDays.get(index - 1);
        }

    }

}
