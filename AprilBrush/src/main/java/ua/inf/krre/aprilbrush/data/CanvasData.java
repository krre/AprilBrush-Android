package ua.inf.krre.aprilbrush.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ua.inf.krre.aprilbrush.logic.UndoManager;

public class CanvasData {
    private static CanvasData canvasData = new CanvasData();
    private Bitmap bitmap;
    private Bitmap buffer;
    private Paint bufferPaint;
    private BrushData brushData;
    private UndoManager undoManager;

    private CanvasData() {
        brushData = BrushData.getInstance();
        undoManager = UndoManager.getInstance();
        bufferPaint = new Paint(Paint.DITHER_FLAG);
        setOpacity(brushData.getProperty(BrushData.Property.OPACITY));
    }

    public static CanvasData getInstance() {
        return canvasData;
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

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = Bitmap.createBitmap(bitmap);
    }

    public void setOpacity(int opacity) {
        int alpha = Math.round((float) opacity / 100 * 255);
        bufferPaint.setAlpha(alpha);
    }

    public void clear() {
        bitmap.eraseColor(Color.WHITE);
        undoManager.add(bitmap);
    }

    public void createBitmaps(int width, int height) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        buffer = Bitmap.createBitmap(bitmap);
        undoManager.add(bitmap);
    }

    public void applyBuffer() {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int alpha = Math.round((float) (brushData.getProperty(BrushData.Property.OPACITY)) / 100 * 255);
        paint.setAlpha(alpha);
        canvas.drawBitmap(buffer, 0, 0, paint);
        buffer.eraseColor(Color.TRANSPARENT);

        undoManager.add(bitmap);
    }
}
