package com.kuoruan.slideswitch.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kuoruan.slideswitch.R;

/**
 * 滑动开关
 */

public class SlideSwitch extends View {

    private Bitmap mSwitchBackground;
    private Bitmap mSlideButton;
    private boolean mSwitchState;
    private Paint mPaint;
    private OnSwitchStateChangeListener mOnSwitchStateChangeListener;
    private boolean isTouchMode = false;
    private float mCurrentX;

    public SlideSwitch(Context context) {
        this(context, null, 0);
    }

    public SlideSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取控件参数
        TypedArray typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.SlideSwitch,
                defStyleAttr, 0);

        int switchBackgroundRes = typedArray.getResourceId(R.styleable.SlideSwitch_switch_background, -1);
        int slideButtonRes = typedArray.getResourceId(R.styleable.SlideSwitch_slide_button, -1);
        mSwitchState = typedArray.getBoolean(R.styleable.SlideSwitch_switch_state, false);

        typedArray.recycle();

        setSwitchBackground(switchBackgroundRes);
        setSlideButton(slideButtonRes);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置控件大小为控件背景大小
        setMeasuredDimension(mSwitchBackground.getWidth(), mSwitchBackground.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mSwitchBackground, 0f, 0f, mPaint);

        if (isTouchMode) {
            float newLeft = mCurrentX - mSlideButton.getWidth() / 2.0f;
            int maxLeft = mSwitchBackground.getWidth() - mSlideButton.getWidth();

            if (newLeft <= 0) {
                newLeft = 0;
            }

            if (newLeft >= maxLeft) {
                newLeft = maxLeft;
            }

            canvas.drawBitmap(mSlideButton, newLeft, 0f, mPaint);
        } else {
            if (mSwitchState) {
                canvas.drawBitmap(mSlideButton, mSwitchBackground.getWidth() - mSlideButton.getWidth(), 0f, mPaint);
            } else {
                canvas.drawBitmap(mSlideButton, 0f, 0f, mPaint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouchMode = true;
                mCurrentX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isTouchMode = false;
                mCurrentX = event.getX();

                float center = mSwitchBackground.getWidth() / 2.0f;

                // 根据当前按下的位置, 和控件中心的位置进行比较
                boolean state = mCurrentX > center;

                // 如果开关状态变化了, 通知界面. 里边开关状态更新了
                if (state != mSwitchState && mOnSwitchStateChangeListener != null) {
                    mOnSwitchStateChangeListener.onSwitchStateChange(this, state);
                    mSwitchState = state;
                }

                break;
            default:
                break;
        }

        invalidate(); // 重绘界面

        return true; // 消费事件
    }

    public void setSwitchBackground(int resId) {
        mSwitchBackground = BitmapFactory.decodeResource(getResources(), resId);
    }

    public void setSlideButton(int resId) {
        mSlideButton = BitmapFactory.decodeResource(getResources(), resId);
    }

    public void setSwitchState(boolean switchState) {
        mSwitchState = switchState;
    }

    public void setOnSwitchStateChangeListener(OnSwitchStateChangeListener onSwitchStateChangeListener) {
        mOnSwitchStateChangeListener = onSwitchStateChangeListener;
    }

    public interface OnSwitchStateChangeListener {
        void onSwitchStateChange(View view, boolean state);
    }
}
