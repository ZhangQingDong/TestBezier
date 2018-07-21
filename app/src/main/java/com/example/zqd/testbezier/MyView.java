package com.example.zqd.testbezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * <p>Title: com.example.zqd.testbezial</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 *
 * @author zhangqingdong
 * @date 2018/6/22 10:30
 */
public class MyView extends View {

    /**
     * 波浪画笔
     */
    private Paint wavePaint;
    /**
     * 圆圈画笔
     */
    private Paint circlePaint;
    /**
     * 文字画笔
     */
    private Paint textPaint;
    /**
     * 控件宽度
     */
    private int screenWidth;
    /**
     * 控件高度
     */
    private int screenHeignt;
    /**
     * 振幅
     */
    private int amplitude = 100;
    private Path path;
    /**
     * 进度
     */
    private float progress = 0;
    private float textProgress = 0;
    /**
     * 起始点
     */
    private Point startPoint = new Point();

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(float progress) {
        this.textProgress = progress;
        if (progress == 100) {
            this.progress = progress + amplitude;
        } else {
            this.progress = progress;
        }
    }

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        wavePaint = new Paint();
        wavePaint.setAntiAlias(true);
        wavePaint.setStrokeWidth(1F);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#999999"));
        textPaint.setTextSize(50f);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor("#999999"));
        circlePaint.setStrokeWidth(5F);
        circlePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(measureSize(400, widthMeasureSpec), measureSize(400, heightMeasureSpec));
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeignt = h;
        startPoint.x = -screenWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 剪裁圆形区域
         */
        clipCircle(canvas);
        /**
         * 画圆边线
         */
        drawCircle(canvas);
        /**
         * 画波浪线
         */
        drawWave(canvas);
        /**
         * 画进度文字
         */
        drawText(canvas);
        postInvalidateDelayed(10);
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Rect targetRect = new Rect(0, -screenHeignt, screenWidth, 0);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf((int) textProgress + "%"), targetRect.centerX(), baseline, textPaint);
    }

    /**
     * 画波浪
     *
     * @param canvas
     */
    private void drawWave(Canvas canvas) {
        int height = (int) (progress / 100 * screenHeignt);
        startPoint.y = -height;
        canvas.translate(0, screenHeignt);
        path = new Path();
        wavePaint.setStyle(Paint.Style.FILL);
        wavePaint.setColor(Color.GREEN);
        int wave = screenWidth / 4;
        path.moveTo(startPoint.x, startPoint.y);
        for (int i = 0; i < 4; i++) {
            int startX = startPoint.x + i * wave * 2;
            int endX = startX + 2 * wave;
            if (i % 2 == 0) {
                path.quadTo((startX + endX) / 2, startPoint.y + amplitude, endX, startPoint.y);
            } else {
                path.quadTo((startX + endX) / 2, startPoint.y - amplitude, endX, startPoint.y);
            }
        }
        path.lineTo(screenWidth, screenHeignt / 2);
        path.lineTo(-screenWidth, screenHeignt / 2);
        path.lineTo(-screenWidth, 0);
        path.close();
        canvas.drawPath(path, wavePaint);
        startPoint.x += 10;
        if (startPoint.x > 0) {
            startPoint.x = -screenWidth;
        }
        path.reset();
    }

    /**
     * 画圆形
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(screenHeignt / 2, screenHeignt / 2, screenHeignt / 2, circlePaint);
    }

    /**
     * 剪裁画圆
     *
     * @param canvas
     */
    private void clipCircle(Canvas canvas) {
        Path circlePath = new Path();
        circlePath.addCircle(screenWidth / 2, screenHeignt / 2, screenHeignt / 2, Path.Direction.CW);
        canvas.clipPath(circlePath);
    }


    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                result = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            default:
                break;
        }
        return result;
    }


}
