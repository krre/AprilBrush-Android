package ua.inf.krre.aprilbrush.data;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import ua.inf.krre.aprilbrush.AppAprilBrush;
import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.logic.BrushEngine;
import ua.inf.krre.aprilbrush.logic.UndoManager;

public class CanvasData {
    private static CanvasData canvasData = new CanvasData();
    private Bitmap bitmap;
    private Bitmap buffer;
    private Paint bufferPaint;
    private UndoManager undoManager;
    private Context context;
    private Resources resources;
    private String imageFolderPath;
    private String imagePath;
    private int fillColor;

    private CanvasData() {
        context = AppAprilBrush.getContext();
        resources = context.getResources();
        undoManager = UndoManager.getInstance();
        bufferPaint = new Paint(Paint.DITHER_FLAG);

        imageFolderPath = Environment.getExternalStorageDirectory().toString() + "/AprilBrush";
        File dir = new File(imageFolderPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static CanvasData getInstance() {
        return canvasData;
    }

    public float[] getFillColor() {
        float[] hsv = new float[3];
        Color.colorToHSV(fillColor, hsv);
        return hsv;
    }

    public void setFillColor(float[] hsv) {
        fillColor = Color.HSVToColor(hsv);
        BrushEngine.getInstance().setEraserColors(hsv);
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
        if (bitmap != null) {
            bitmap.eraseColor(fillColor);
            undoManager.add(bitmap);
        }
    }

    public void createBitmaps(int width, int height) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        buffer = Bitmap.createBitmap(bitmap);
        newImage();
    }

    public void newImage() {
        bitmap.eraseColor(fillColor);
        buffer.eraseColor(Color.TRANSPARENT);
        undoManager.clear();
        undoManager.add(bitmap);
        imagePath = imageFolderPath + "/" + System.currentTimeMillis() + ".png";

        Toast.makeText(context, resources.getString(R.string.message_new_picture), Toast.LENGTH_SHORT).show();
    }

    public void loadImage(String path) {
        imagePath = path;
        bitmap = BitmapFactory.decodeFile(path);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        buffer.eraseColor(Color.TRANSPARENT);
        undoManager.clear();
        undoManager.add(bitmap);

        Toast.makeText(context, resources.getString(R.string.message_load_picture), Toast.LENGTH_SHORT).show();
    }

    public void saveImage() {
        File file = new File(imagePath);
        file.delete(); // change the name of a picture to force update the gallery thumbnails
        imagePath = imageFolderPath + "/" + System.currentTimeMillis() + ".png";
        file = new File(imagePath);

        OutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            Log.d("Image Writer", "Problem with the image. Stacktrace: ", e);
        }

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        Toast.makeText(context, resources.getString(R.string.message_save_picture), Toast.LENGTH_SHORT).show();
    }

    public void applyBuffer() {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int opacity = BrushEngine.getInstance().getValue(BrushData.Property.OPACITY);
        int alpha = Math.round(opacity / 100f * 255);
        paint.setAlpha(alpha);
        canvas.drawBitmap(buffer, 0, 0, paint);
        buffer.eraseColor(Color.TRANSPARENT);

        undoManager.add(bitmap);
    }
}
