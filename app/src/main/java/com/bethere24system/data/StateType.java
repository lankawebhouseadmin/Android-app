package com.bethere24system.data;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bethere24system.BeThereApplication;
import com.bethere24system.R;

/**
 * Created by Administrator on 3/5/2016.
 */
public enum StateType {

    IN_BATHROOM("in_bathroom", R.color.bathroom, R.drawable.ic_bathroom_small, R.drawable.ic_bathroom_small_white, R.string.bathroom, 7),
    WITH_VISITORS("with_visitors", R.color.visitors, R.drawable.ic_visitors_small, R.drawable.ic_visitors_small_white, R.string.visitors, 12),
    IN_DINING("in_dining_area", R.color.dining, R.drawable.ic_dining_small, R.drawable.ic_dining_small_white, R.string.dining, 5),
    IN_MOTION("in_motion", R.color.motion, R.drawable.ic_motion_small, R.drawable.ic_motion_small_white, R.string.motion, 9),
    SLEEPING("in_bed", R.color.bed, R.drawable.ic_bed_small, R.drawable.ic_bed_small_white, R.string.bed, 1),
    AWAY_FROM_HOME("away_from_home", R.color.awayhome, R.drawable.ic_awayhome_small, R.drawable.ic_awayhome_small_white, R.string.awayhome, 11),
    IN_RECLINER("in_recliner", R.color.recliner, R.drawable.ic_recliner_small, R.drawable.ic_recliner_small_white, R.string.recliner, 2),
    MEDICATION("taking_medication", R.color.medication, R.drawable.ic_medication_small, R.drawable.ic_medication_small_white, R.string.medication, 10);


    private String mTypeName;
    private int mColorRes;
    private int mSmallIconRes;
    private int mSmallWhiteIconRes;
    private int mTitleRes;
    private int mTypeIndex;

    @Nullable
    public static StateType fromTypeName(String stateType) {
        for (StateType type : values()) if (type.mTypeName.equals(stateType)) return type;
        return null;
    }

    @Nullable
    public static StateType fromTypeIndex(int stateType) {
        for (StateType type : values()) if (type.mTypeIndex == stateType) return type;
        return null;
    }

    public static StateType fromTypeTitle(String title) {
        for (StateType type : values()) if (BeThereApplication.getInstance().getString(type.getTitleRes()).equals(title)) return type;
        return null;
    }

    StateType(String typeName, int colorRes, int smallIconRes, int smallIconWhiteRes, int titleRes, int typeIndex) {
        mTypeName = typeName;
        mColorRes = colorRes;
        mSmallIconRes = smallIconRes;
        mSmallWhiteIconRes = smallIconWhiteRes;
        mTitleRes = titleRes;
        mTypeIndex = typeIndex;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public int getColorRes() {
        return mColorRes;
    }

    public int getColor(Context context) {
        return context.getResources().getColor(mColorRes);
    }

    public int getSmallIconRes() {
        return mSmallIconRes;
    }

    public int getSmallWhiteIconRes() {
        return mSmallWhiteIconRes;
    }

    public int getTitleRes() {
        return mTitleRes;
    }

}
