package com.glad.randomlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 *@date 2019/10/10
 *@author ningwei
 *@description  二阶贝塞尔曲线
 */
public class Bezier extends View {
    private Paint mPaint;
    private PointF start,end, control;
    private int centerX, centerY;

    public Bezier(Context context) {
        super(context);
        initView();
    }

    public Bezier(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public Bezier(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    //初始化画笔，还有贝塞尔曲线的数据点和控制点
    void initView(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        start = new PointF(0,0);
        end = new PointF(0, 0);
        control = new PointF(0, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取view的中心
        centerX = w / 2;
        centerY = h / 2;
        //更新两个数据点的坐标。
        start.x = centerX - 100;
        start.y = centerY;

        end.x = centerX + 100;
        end.y = centerY;

        //跟新控制点的坐标
        control.x = centerX;
        control.y = centerY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        control.x = event.getX();
        control.y = event.getY();

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制两个数据据点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        canvas.drawPoint(start.x,start.y, mPaint);
        canvas.drawPoint(end.x,end.y, mPaint);

        //绘制控制点和辅助线
        canvas.drawPoint(control.x, control.y, mPaint);
        mPaint.setStrokeWidth(8);
        canvas.drawLine(start.x, start.y, control.x, control.y, mPaint);
        canvas.drawLine(end.x, end.y, control.x, control.y, mPaint);

        //绘制贝塞尔曲线

        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.quadTo(control.x, control.y, end.x, end.y);
        canvas.drawPath(path, mPaint);
    }
}
