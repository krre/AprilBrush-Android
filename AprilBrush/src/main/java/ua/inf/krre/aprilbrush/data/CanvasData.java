package ua.inf.krre.aprilbrush.data;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import ua.inf.krre.aprilbrush.AppAprilBrush;
import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.logic.UndoManager;

public class CanvasData {
    private static CanvasData canvasData = new CanvasData();
    private Bitmap bitmap;
    private Bitmap buffer;
    private Paint bufferPaint;
    private BrushData brushData;
    private UndoManager undoManager;
    private String filename;
    private Context context;
    private Resources resources;

    private CanvasData() {
        context = AppAprilBrush.getContext();
        resources = context.getResources();
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
        newImage();
    }

    public void newImage() {
        bitmap.eraseColor(Color.WHITE);
        buffer.eraseColor(Color.TRANSPARENT);
        undoManager.clear();
        undoManager.add(bitmap);
        filename = System.currentTimeMillis() + ".png";

        Toast.makeText(context, resources.getString(R.string.message_new_picture), Toast.LENGTH_SHORT).show();
    }

    public void loadImage() {
        Toast.makeText(context, resources.getString(R.string.message_load_picture), Toast.LENGTH_SHORT).show();
    }

    public void saveImage() {
        String path = Environment.getExternalStorageDirectory().toString() + "/AprilBrush";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(path, filename);
        OutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (Exception e) {
            Log.d("Image Writer", "Problem with the image. Stacktrace: ", e);
        }

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

        Toast.makeText(context, resources.getString(R.string.message_save_picture), Toast.LENGTH_SHORT).show();
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
