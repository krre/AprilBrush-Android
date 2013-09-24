package ua.inf.krre.aprilbrush.data;

import android.graphics.Bitmap;
import android.graphics.Color;

public class CanvasData {
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void clear() {
        bitmap.eraseColor(Color.WHITE);
    }
}
