package ua.inf.krre.aprilbrush.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import ua.inf.krre.aprilbrush.data.CanvasData;
import ua.inf.krre.aprilbrush.logic.BrushEngine;

public class PaintView extends View {
    private BrushEngine engine;
    private CanvasData canvasData;
    private Canvas canvas;
    private Paint bitmapPaint;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        engine = BrushEngine.getInstance();
    }

    public void setCanvasData(CanvasData canvasData) {
        this.canvasData = canvasData;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (canvasData.getBitmap() == null) {
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvasData.setBitmap(bitmap);
        }
        canvas = new Canvas(canvasData.getBitmap());
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(canvasData.getBitmap(), 0, 0, bitmapPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                engine.setTouch(canvas, event);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                engine.paintDabs(event);
                invalidate();
                break;
        }
        return true;
    }
}
