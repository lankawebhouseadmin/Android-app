package com.bethere24system.data;

import android.content.Context;

import com.bethere24system.R;

/**
 * Created by Administrator on 3/5/2016.
 */
public class Score {

    public static final Score EMPTY = new Score(0);

    static {
        EMPTY.mTitleRes = R.string.score_empty;
        EMPTY.mColorRes = R.color.white;
    }

    private int mScore;
    private int mColorRes;
    private int mTitleRes;

    public Score(int score) {
        mScore = score;
        if (score <= 2) {
            mColorRes = R.color.score_poor;
            mTitleRes = R.string.score_poor;
        } else if (score >= 8) {
            mColorRes = R.color.score_very_good;
            mTitleRes = R.string.score_very_good;
        } else {
            mColorRes = R.color.score_good;
            mTitleRes = R.string.score_good;
        }
    }

    public int getScore() {
        return mScore;
    }

    public int getColorRes() {
        return mColorRes;
    }

    public int getColor(Context context) {
        return context.getResources().getColor(mColorRes);
    }

    public int getTitleRes() {
        return mTitleRes;
    }

    public String getScoreText() {
        if (this.equals(EMPTY)) return "-";
        return String.valueOf(mScore);
    }

}
