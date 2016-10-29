package com.bethere24system.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bethere24system.R;
import com.bethere24system.data.Score;
import com.bethere24system.utils.DisplayUtils;

/**
 * Created by Administrator on 3/5/2016.
 */
public class StateCircleCenterView extends FrameLayout {

    private RectF mOval;
    private RectF mOvalInner;
    private ViewHolder mHolder;
    private Paint mPaint = new Paint();
    private Score mCurrentScore;

    public StateCircleCenterView(Context context) {
        super(context);
        init();
    }

    public StateCircleCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StateCircleCenterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public StateCircleCenterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        addView(LayoutInflater.from(getContext()).inflate(R.layout.widget_state_circle_center, this, false));
        mHolder = new ViewHolder(this);
    }

    public void setScore(Score score) {
        mCurrentScore = score;

        if (score.getScore() > 0) {

            mHolder.description.setVisibility(VISIBLE);
            mHolder.description.setText(score.getTitleRes());
            mHolder.score.setText(String.valueOf(score.getScore()));
        } else {

            mHolder.description.setVisibility(GONE);
            mHolder.score.setText("-");
        }
        invalidate();
    }

    private static final class ViewHolder {
        public final TextView score;
        public final TextView description;

        public ViewHolder(View root) {
            score = (TextView) root.findViewById(R.id.score);
            description = (TextView) root.findViewById(R.id.description);
        }

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mCurrentScore == null) return;

        if (mOval == null || mOval.width() > getWidth() + 1 || mOval.width() < getWidth() - 1) {
            mOval = new RectF(0, 0, getWidth(), getHeight());

            float inset = DisplayUtils.dpToPx(11);
            mOval.inset(inset, inset);

            mOvalInner = new RectF(0, 0, getWidth(), getHeight());
            inset = DisplayUtils.dpToPx(11 + 6);
            mOvalInner.inset(inset, inset);
        }

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(mCurrentScore.getColor(getContext()));

        canvas.drawArc(mOval, 0, 360, true, mPaint);

        mPaint.setColor(Color.WHITE);
        canvas.drawArc(mOvalInner, 0, 360, true, mPaint);

    }
}
