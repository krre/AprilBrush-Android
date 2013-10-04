package ua.inf.krre.aprilbrush.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import ua.inf.krre.aprilbrush.data.BrushData;
import ua.inf.krre.aprilbrush.data.CanvasData;
import ua.inf.krre.aprilbrush.logic.BrushEngine;

public class PaintView extends View {
    private BrushEngine engine;
    private CanvasData canvasData;
    private Canvas canvas;
    private Canvas canvasBuffer;
    private Paint bitmapPaint;
    private Paint bufferPaint;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        bufferPaint = new Paint(Paint.DITHER_FLAG);
        bufferPaint.setAlpha(128);
        if (!isInEditMode()) {
            engine = BrushEngine.getInstance();
        }
    }

    public void setCanvasData(CanvasData canvasData) {
        this.canvasData = canvasData;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (isInEditMode()) {
            return;
        }

        if (canvasData.getBitmap() == null) {
            canvasData.createBitmaps(w, h);
        }

        canvas = new Canvas(canvasData.getBitmap());
        canvasBuffer = new Canvas(canvasData.getBuffer());
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            return;
        }
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(canvasData.getBitmap(), 0, 0, bitmapPaint);
        int alpha = Math.round((float) (BrushData.getInstance().getProperty(BrushData.Property.OPACITY)) / 100 * 255);
        bufferPaint.setAlpha(alpha);
        canvas.drawBitmap(canvasData.getBuffer(), 0, 0, bufferPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                engine.setTouch(canvasBuffer, event);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                engine.paintDabs(event);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                canvasData.applyBuffer();
                invalidate();
        }
        return true;
    }
}
