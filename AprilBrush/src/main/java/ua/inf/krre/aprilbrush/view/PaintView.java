package ua.inf.krre.aprilbrush.view;

import ua.inf.krre.aprilbrush.logic.BrushEngine;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {
    private BrushEngine engine;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint bitmapPaint;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        engine = new BrushEngine();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                engine.setTouch(canvas, event);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                engine.paintDab(event);
                invalidate();
                break;
        }
        return true;
    }

}
