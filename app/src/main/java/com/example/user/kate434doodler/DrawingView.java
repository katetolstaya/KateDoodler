package com.example.user.kate434doodler;

/**
 * Created by User on 3/10/2016.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Color;
import android.view.MotionEvent;

public class DrawingView extends View
{

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private int opacity = 255;
    private int brush_size = 20;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
//get drawing area setup for interaction
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAlpha(opacity);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brush_size);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setColor(String newColor){
//set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setOpacity (int o){
        opacity = o;
        drawPaint.setAlpha(o*85);
    }

    public void setBrushSize (int s){
        brush_size = s*10;
        if (brush_size == 10) {
            drawPaint.setStrokeWidth(10);
        } else if (brush_size ==20){
            drawPaint.setStrokeWidth(20);
        } else if (brush_size == 30) {
            drawPaint.setStrokeWidth(30);
        }
    }

    public Bitmap getCanvasBitmap(){
        return canvasBitmap;
    }

    public void resetBitmap(){
        drawCanvas.drawColor(Color.WHITE);
        invalidate();
    }

    public void eraser(){
        invalidate();
        paintColor = Color.WHITE;
        drawPaint.setColor(paintColor);
    }
}