package ua.inf.krre.aprilbrush.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ua.inf.krre.aprilbrush.logic.BrushEngine;

public class CanvasData {
    private Bitmap bitmap;
    private Bitmap buffer;
    private Paint bufferPaint;
    private BrushData brushData;

    public CanvasData() {
        brushData = BrushData.getInstance();
        bufferPaint = new Paint(Paint.DITHER_FLAG);
        BrushEngine.getInstance().setCanvasData(this);
        setOpacity(brushData.getProperty(BrushData.Property.OPACITY));
    }

    public Paint getBufferPaint() {
        return bufferPaint;
    }

    public Bitmap getBuffer() {
        return buffer;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setOpacity(int opacity) {
        int alpha = Math.round((float) opacity / 100 * 255);
        bufferPaint.setAlpha(alpha);
    }

    public void clear() {
        bitmap.eraseColor(Color.WHITE);
    }

    public void createBitmaps(int width, int height) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        buffer = Bitmap.createBitmap(bitmap);
    }

    public void applyBuffer() {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int alpha = Math.round((float) (brushData.getProperty(BrushData.Property.OPACITY)) / 100 * 255);
        paint.setAlpha(alpha);
        canvas.drawBitmap(buffer, 0, 0, paint);
        buffer.eraseColor(Color.TRANSPARENT);
    }
}
