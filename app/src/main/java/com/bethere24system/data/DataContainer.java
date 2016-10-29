package com.bethere24system.data;

import com.bethere24system.transport.data.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 3/5/2016.
 */
public class DataContainer {

    public final StateListContainer stateListContainer;
    public final AlertListContainer alertListContainer;
    public final GeneralData generalData;

    public DataContainer(List<UserState> userStates) {
        com.bethere24system.transport.data.State state = userStates.get(0).getLocation().getItem().getState();
        stateListContainer = new StateListContainer(
                                                mapStates(state.getAwayFromHome()),
                                                mapStates(state.getInBathroom()),
                                                mapStates(state.getInDining()),
                                                mapStates(state.getInRecliner()),
                                                mapStates(state.getMedication()),
                                                mapStates(state.getSleeping()),
                                                mapStates(state.getWithVisitors()));

        alertListContainer = new AlertListContainer(userStates.get(0).getLocation().getItem().getAlert());
        generalData = new GeneralData(userStates.get(0).getLocation().getItem().getVirtualDayStart());
    }

    private List<State> mapStates(List<StateItem> stateItems) {
        List<State> states = stateItems == null ? new ArrayList<>() : new ArrayList<>(stateItems.size());
        for (StateItem stateItem : stateItems) {
            states.add(new State(stateItem));
        }
        return states;
    }
}
