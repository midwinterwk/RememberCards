package com.example.cardapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ControlBall extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mColor = getResources().getColor(R.color.colorBall);
    private int mColorCenter = getResources().getColor(R.color.colorBallCenter);
    private int mRadius;
    private enum TouchType {
        NONE, TOP, LEFT, BOTTOM, RIGHT, CENTER;
    }

    private static final String TAG = "ControlBall";

    public ControlBall(Context context) {
        super(context);
        init();
    }

    public ControlBall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setColor(mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        mRadius = Math.min(width, height) / 2;
        canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2,
                mRadius, mPaint);
        Paint mPaintCenter = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCenter.setColor(mColorCenter);
        canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2,
                mRadius / 3, mPaintCenter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "eventX = " + event.getX() + "eventY = " + event.getY());
        TouchType touchType = getTouchType(event.getX(), event.getY());
        Log.d(TAG, "touchType = " + touchType);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (touchType) {
                case TOP:
                    CardUtil.onKeyEvent(KeyEvent.KEYCODE_DPAD_UP);
                    break;
                case LEFT:
                    CardUtil.onKeyEvent(KeyEvent.KEYCODE_DPAD_LEFT);
                    break;
                case BOTTOM:
                    CardUtil.onKeyEvent(KeyEvent.KEYCODE_DPAD_DOWN);
                    break;
                case RIGHT:
                    CardUtil.onKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
                    break;
                case CENTER:
//                CardUtil.onKeyEvent(KeyEvent.KEYCODE_ENTER);
                    CardUtil.onKeyEvent(KeyEvent.KEYCODE_DPAD_CENTER);
                    break;
            }
        }
        return true;
    }

    private TouchType getTouchType(float x, float y) {
        float centerX = mRadius;
        float centerY = mRadius;
        float relativeX = x - centerX;
        float relativeY = y - centerY;
        float distance = (float) Math.sqrt(Math.pow(relativeX,2) + Math.pow(relativeY,2));
        if (distance < mRadius / 3) {
            return TouchType.CENTER;
        } else {
            if (relativeY > relativeX) {
                if (relativeY + relativeX > 0) {
                    return TouchType.BOTTOM;
                } else {
                    return TouchType.LEFT;
                }
            } else {
                if (relativeY + relativeX > 0) {
                    return TouchType.RIGHT;
                } else {
                    return TouchType.TOP;
                }
            }
        }
    }


}
