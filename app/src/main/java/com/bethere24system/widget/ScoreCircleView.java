package com.bethere24system.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bethere24system.BeThereApplication;
import com.bethere24system.data.ClockDate;
import com.bethere24system.data.State;
import com.bethere24system.data.StateType;
import com.bethere24system.utils.DisplayUtils;
import com.bethere24system.utils.RotationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 3/5/2016.
 */
public class ScoreCircleView extends FrameLayout {

    private RectF mOval;
    private Paint mPaint = new Paint();
    private int mInset;
    private ScoreCircleCenterView mCenterView;
    private int mDisplayWidth;
    private Listener mListener;
    @Nullable
    private State mCurrentState;
    private View mPointer;
    private List<State> mStates;
    private StateType mStateType;
    private ClockDate mStartOfTheDay;
    private ClockDate mTodaysLoginDate;
    private Date mCurrentDate = new Date();

    public ScoreCircleView(Context context) {
        super(context);
        init();
    }

    public ScoreCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScoreCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScoreCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mInset = DisplayUtils.dpToPx(6);
        mDisplayWidth = DisplayUtils.getDisplaySize(getContext()).x;
        if (isInEditMode()) {
            mStartOfTheDay = new ClockDate();
            mTodaysLoginDate = new ClockDate();
        } else {
            mStartOfTheDay = BeThereApplication.getInstance().getData().generalData.startOfTheDay;
            mTodaysLoginDate = BeThereApplication.getInstance().getData().generalData.loginDate;
        }
    }

    public void setStates(Date currentDate, List<State> states, StateType currentType) {
        initScoreView();
        mStates = states == null ? new ArrayList<>() : states;
        mStateType = currentType;
        if (mCurrentState == null) mCurrentState = mStates.size() > 0 ? mStates.get(0) : null;
        setCurrentState(mCurrentState, mStateType);
        invalidate();
        mCurrentDate = currentDate;
    }

    public void setStates(Date currentDate, List<State> states, State state, StateType currentType) {
        if (states == null || states.indexOf(state) < 0) {
            mCurrentState = null;
        } else {
            mCurrentState = state;
        }
        setStates(currentDate, states, mCurrentState == null ? currentType : state.type);
    }

    public void nextState() {
        if (mStates == null || mStates.size() <= 1 || mCurrentState == null) return;
        int index = mStates.indexOf(mCurrentState) - 1;
        if (index < 0) index = mStates.size() - 1;
        RotationUtils.setClockwise(true);
        setCurrentState(mStates.get(index), mStateType);
    }

    public void prevState() {
        if (mStates == null || mStates.size() <= 1 || mCurrentState == null) return;
        int index = mStates.indexOf(mCurrentState) + 1;
        if (index >= mStates.size()) index = 0;
        RotationUtils.setClockwise(false);
        setCurrentState(mStates.get(index), mStateType);
    }

    public void setOnStateListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DisplayUtils.dpToPx(2));
        mPaint.setColor(Color.GREEN);

        double rads = Math.toRadians(DateUtils.isToday(mCurrentDate.getTime()) ? mTodaysLoginDate.angle : mStartOfTheDay.angle);
        float dx = (float) (getWidth() / 2f * (Math.sin(rads)));
        float dy = (float) (getWidth() / 2f * (Math.cos(rads)));

        Path path = new Path();
        path.moveTo(getWidth()/2, getWidth()/2);
        path.rLineTo(dx, -dy);
        path.close();
        canvas.drawPath(path, mPaint);

        if (mStates == null || mStates.size() == 0) return;

        if (mOval == null || mOval.width() > getWidth() + 1 || mOval.width() < getWidth() - 1) {
            mOval = new RectF(0, 0, getWidth(), getHeight());
        }

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        for (State state : mStates) {

            mPaint.setColor(Color.WHITE);
            mPaint.setAlpha(100);
            canvas.drawArc(mOval, state.startAngle - 90f, state.sweepAngle, true, mPaint);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        RotationUtils.setScoreTouch(true);
        initScoreView();

        if (mCenterView != null) {

            float currentX = event.getX();
            float currentY = event.getY();

            double rotationAngleRadians = Math.atan2(currentX - getWidth() / 2, getHeight() / 2 - currentY);
            float angle = (float) Math.toDegrees(rotationAngleRadians);

            if (angle < 0) angle += 360f;

            normalizeAngle(angle);

        }
        return super.onTouchEvent(event);
    }

    public void normalizeAngle(float degrees) {

        if (mStates == null || mStates.size() < 2) return;

        State nearest = null;
        for (State state : mStates) {
            if (nearest == null) {
                nearest = state;
            } else {
                nearest = getNearest(degrees, nearest, state);
            }
        }

        setCurrentState(nearest, mStateType);

    }

    private State getNearest(float target, State state1, State state2) {
//        return Math.abs(target - state1.centerAngle % 180f) > Math.abs(target - state2.centerAngle % 180f) ? state2 : state1;
        return Math.abs(target - state1.centerAngle) > Math.abs(target - state2.centerAngle) ? state2 : state1;
    }

    public void initScoreView() {
        if (mCenterView != null) return;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof ScoreCircleCenterView) {
                mCenterView = (ScoreCircleCenterView) v;
            } else if (v instanceof ImageView) {
                mPointer = v;
                if (mStateType != null) setCurrentState(mCurrentState, mStateType);
            }
        }
    }

    private void setCurrentState(State state, StateType type) {

        mCenterView.setScore(state == null ? null : state.score, type);
        mCurrentState = state;
        if (mListener != null) mListener.onStateSelected(state);

        if (state == null) {
            mPointer.setVisibility(View.INVISIBLE);
        } else {
            mPointer.setVisibility(View.VISIBLE);
            setRotation(state.startAngle);
        }

    }

    @Override
    public void setRotation(float newRotation) {
        if (mPointer != null) {
            float currRotation = mPointer.getRotation();

            if (RotationUtils.getScoreTouch()) {

                if ((360f - currRotation) + newRotation < 90f) currRotation -= 360f;
                else if ((360f - newRotation) + currRotation < 90f) newRotation -= 360f;
            } else {
                if (currRotation < 0) currRotation += 360f;

                if (RotationUtils.getClockwise()) {
                    if (currRotation >= newRotation) currRotation -= 360f;
                } else {
                    if (currRotation < newRotation) newRotation -= 360f;
                }
            }

            ObjectAnimator.ofFloat(mPointer, "rotation", currRotation, newRotation).start();
        }

        RotationUtils.setScoreTouch(false);
    }

    public interface Listener {
        void onStateSelected(State type);
    }

}
