package com.bethere24system.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bethere24system.data.State;
import com.bethere24system.data.StateType;
import com.bethere24system.utils.DisplayUtils;

import java.util.Map;

/**
 * Created by Administrator on 3/5/2016.
 */
public class StateCircleView extends FrameLayout {

    private RectF mOval;
    private RectF mOuterOval;
    private Paint mPaint = new Paint();
    private int mInset;
    private StateCircleCenterView mScoreView;
    private int mDisplayWidth;
    private Listener mListener;
    private State mCurrentState;
    private View mPointer;
    private Map<StateType, State> mStatesMap;

    public StateCircleView(Context context) {
        super(context);
        init();
    }

    public StateCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StateCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public StateCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mInset = DisplayUtils.dpToPx(6);

        mDisplayWidth = DisplayUtils.getDisplaySize(getContext()).x;

        for (int i = 0; i < StateType.values().length; i++) addToMainCircle(StateType.values()[i].getSmallWhiteIconRes(), i);

    }

    public void setStates(Map<StateType, State> states) {
        setStates(states, StateType.IN_BATHROOM);
    }

    public void setStates(Map<StateType, State> states, StateType currentType) {
        initScoreView();
        mStatesMap = states;
        setCurrentState(currentType);
        invalidate();
    }

    public void nextState() {
        if (mStatesMap == null) return;
        int statesAmount = StateType.values().length;
        int newIndex = mCurrentState.type.ordinal() + 1;
        if (newIndex >= statesAmount) newIndex = 0;
        StateType nextType = StateType.values()[newIndex];
        setCurrentState(nextType);
    }

    public void prevState() {
        if (mStatesMap == null) return;
        int statesAmount = StateType.values().length;
        int newIndex = mCurrentState.type.ordinal() - 1;
        if (newIndex < 0) newIndex = statesAmount - 1;
        StateType nextType = StateType.values()[newIndex];
        setCurrentState(nextType);
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

        if (mStatesMap == null) return;

        if (mOval == null || mOval.width() > getWidth() + 1 || mOval.width() < getWidth() - 1) {
            mOval = new RectF(0, 0, getWidth(), getHeight());
            mOval.inset(mInset, mInset);
            mOuterOval = new RectF(0, 0, getWidth(), getHeight());
        }

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        StateType type;
        State state;
        int count = StateType.values().length;
        float sweepAngle = 360f / (float) count;
        float startAngle;
        for (int i = 0; i < count; i++) {

            type = StateType.values()[i];
            state = mStatesMap.get(type);

            startAngle = sweepAngle * i - sweepAngle / 2 + 270;

            mPaint.setColor(state.score.getColor(getContext()));
            canvas.drawArc(mOuterOval, startAngle, sweepAngle, true, mPaint);

            mPaint.setColor(type.getColor(getContext()));
            canvas.drawArc(mOval, startAngle, sweepAngle, true, mPaint);

        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        initScoreView();

        if (mScoreView != null) {

            float currentX = event.getX();
            float currentY = event.getY();

            double rotationAngleRadians = Math.atan2(currentX - getWidth() / 2, getHeight() / 2 - currentY);
            float angle = (float) Math.toDegrees(rotationAngleRadians);

            normalizeAngle(angle);


        }
        return super.onTouchEvent(event);
    }

    public void addToMainCircle(int iconRes, int pos) {

        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(iconRes);

        float angleRotateCirclePart = 360 * pos / StateType.values().length;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = calculateX(angleRotateCirclePart);
        params.topMargin = calculateY(angleRotateCirclePart);
        this.addView(imageView, params);
    }

    private int PERCENT_CIRCLE_SIZE = 80;
    private int PERCENT_PART_CIRCLE_SIZE = 10;

    private int calculateX(float angleRotateCirclePart) {
        double radians = Math.toRadians(angleRotateCirclePart);
        int size = mDisplayWidth * PERCENT_CIRCLE_SIZE / 100;
        int sizePart = mDisplayWidth * PERCENT_PART_CIRCLE_SIZE / 100;
        int x = size / 2 + (int) (Math.sin(radians) * size / 3) - sizePart / 18 * 10;
        return x;
    }

    private int calculateY(float angleRotateCirclePart) {
        double radians = Math.toRadians(angleRotateCirclePart);
        int size = mDisplayWidth * PERCENT_CIRCLE_SIZE / 100;
        int sizePart = mDisplayWidth * PERCENT_PART_CIRCLE_SIZE / 100;
        int y = size / 2 - (int) (Math.cos(radians) * size / 3) - sizePart / 18 * 10;
        return y;
    }

    public void normalizeAngle(float degrees) {

        int length = StateType.values().length;

        float segment = 360f / (float) length;
        float totalSegment = -180f + (length % 2 == 0 ? segment / 2 : segment);
        for (int i = 0; i < length; i++) {
            if (degrees < totalSegment) {
                setCurrentState(StateType.values()[(i + length / 2 + length % 2) % length]);
//                return totalSegment - segment / 2;
                return;
            }
            totalSegment += segment;
        }
        setCurrentState(StateType.values()[(length + length / 2 + length % 2) % length]);
//        return totalSegment - segment / 2;
    }

    public void initScoreView() {
        if (mScoreView != null) return;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof StateCircleCenterView) {
                mScoreView = (StateCircleCenterView) v;
            } else if (v instanceof ImageView) {
                mPointer = v;
                if (mStatesMap != null) setCurrentState(mCurrentState.type);
            }
        }
    }

    public void refreshData() {
        setCurrentState(mCurrentState.type);
    }

    private void setCurrentState(StateType type) {
        if (!mStatesMap.get(type).equals(mCurrentState)) {

            mCurrentState = mStatesMap.get(type);
            mScoreView.setScore(mCurrentState.score);

            if (mListener != null) mListener.onStateSelected(mCurrentState);

            int length = StateType.values().length;
            float rotation = ((float) type.ordinal() * (360f / (float) length));
//            if (length % 2 > 0) {
//                setRotation(rotation);
//            } else {
//                rotation += (360f / (float) length);
//                if (rotation > 180) rotation -= 180;
                setRotation(rotation);
//            }
        }
    }

    @Override
    public void setRotation(float newRotation) {
        if (mPointer != null) {
            float currRotation = mPointer.getRotation();

            float diff = currRotation - newRotation;
            if (diff < -180f) {
                currRotation += 360f;
            } else if (diff > 180f) {
                currRotation -= 360f;
            }

            ObjectAnimator.ofFloat(mPointer, "rotation", currRotation, newRotation).start();
        }
    }

    public interface Listener {
        void onStateSelected(State type);
    }

}
