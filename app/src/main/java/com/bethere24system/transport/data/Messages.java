package com.bethere24system.transport.data;

import com.bethere24system.data.Score;
import com.bethere24system.data.StateType;
import com.bethere24system.transport.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 3/7/2016.
 */
public class Messages {

    private static JSONObject messages;

    public Messages() {

        try {
            JSONArray array = new JSONArray(Constants.messages);
            this.messages = array.getJSONObject(0);
        }catch(JSONException e) {

        }
    }

    public String getMessageFromTypeAndScore (StateType type, Score score) {

        String message = "";

        int index = 1;

        if (score.getScore() <= 2) index = 2;
        else if (score.getScore() >= 8) index = 0;

        try {
            JSONArray jsonArray = messages.getJSONArray(type.getTypeName());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            message = jsonObject.getString(String.format("%d", index));
        } catch (JSONException e) {

        }

        return message;
    }

}
