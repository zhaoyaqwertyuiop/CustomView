package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class RingView extends View{

    private int innerRadius; // 圆环内径
    private int ringWidth; // 圆环宽度
    private int backgroundColor; // 圆环背景色
    private int strokeColor; // 圆环的颜色
    private int startAngle; // 开始角度,默认为270

    private Paint onePaint, twoPaint;

    public RingView(Context context) {
        this(context, null);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingView);
        innerRadius = typedArray.getDimensionPixelOffset(R.styleable.RingView_innerRadius, 15);
        ringWidth = typedArray.getDimensionPixelOffset(R.styleable.RingView_ringWidth, 5);
        backgroundColor = typedArray.getColor(R.styleable.RingView_backgroundColor, Color.RED);
        strokeColor = typedArray.getColor(R.styleable.RingView_strokeColor, Color.GREEN);
        startAngle = typedArray.getInteger(R.styleable.RingView_startAngle, 270);

        startAngle = 270;
        typedArray.recycle();

        initPaint();
    }

    private void initPaint() {
        onePaint = new Paint();
        onePaint.setAntiAlias(true); // 抗锯齿
        onePaint.setColor(backgroundColor);
        onePaint.setStyle(Paint.Style.STROKE);
        onePaint.setStrokeWidth(ringWidth);

        twoPaint = new Paint();
        twoPaint.setAntiAlias(true);
        twoPaint.setColor(strokeColor);
        twoPaint.setStyle(Paint.Style.STROKE);
        twoPaint.setStrokeWidth(ringWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        canvas.drawCircle(cx, cy, innerRadius + 1 +ringWidth/2, this.onePaint); // 画底层圆环

        float radius = innerRadius + 1 +ringWidth/2;
        RectF oval = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
        canvas.drawArc(oval, startAngle, change, false, twoPaint);

//        if (change > 90) {
//            twoPaint.setColor(Color.BLUE);
//        }
//        canvas.drawArc(oval, startAngle, change, false, twoPaint);
//        if (change <= 90) {
//            twoPaint.setColor(Color.GREEN);
//            canvas.drawArc(oval, startAngle, change, false, twoPaint);
//        } else {
//            twoPaint.setColor(Color.GREEN);
//            canvas.drawArc(oval, startAngle, 90, false, twoPaint);
//            twoPaint.setColor(Color.BLUE);
//            canvas.drawArc(oval, (float) (startAngle + 89.5), (float) (change - 89.5), false, twoPaint);
//        }
    }

    float change; // 角度差
    float finallyChange;
    private Handler handler = new Handler();

    /** 设置百分比 */
    public void setValue(double score) {
        change = (float) 0;
        finallyChange = (float) (360 * score / 100.0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (change <= finallyChange) {
                        Thread.sleep(1);
                        change += 0.3;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RingView.this.invalidate();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
