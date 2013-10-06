package ua.inf.krre.aprilbrush.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View {
    private final float RING_RATIO = 0.7f;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap;
    private int containerWidth;

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        containerWidth = Math.min(w, h);
        if (containerWidth > 0) {
            createColorPicker(containerWidth);
        }
    }

    private void createColorPicker(int width) {
        bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);

        createColorRing(width);

        float rectSize = (float) (width * RING_RATIO / Math.sqrt(2f));
        createRectangle(rectSize);
    }

    private void createColorRing(int width) {
        int center = width / 2;
        Shader ringShader = new SweepGradient(center, center, gradientColors(), null);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(ringShader);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(center, center, center, paint);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        canvas.drawCircle(center, center, Math.round(RING_RATIO * center), paint);
    }

    private void createRectangle(float width) {
        float x = (containerWidth - width) / 2f;
        float y = x;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Shader saturationShader = new LinearGradient(x, y, x + width, y, Color.WHITE, Color.RED, Shader.TileMode.CLAMP);
        paint.setShader(saturationShader);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(x, y, x + width, y + width, paint);

        Shader valueShader = new LinearGradient(x, y, x, y + width, 0x00000000, 0xFF000000, Shader.TileMode.CLAMP);
        paint.setShader(valueShader);
        canvas.drawRect(x, y, x + width, y + width, paint);
    }

    int[] gradientColors() {
        int segments = 7;
        int hueMax = 360;
        float hueStep = hueMax / (segments - 1);
        int[] colors = new int[segments];
        float[] hsv = {0, 100, 100};
        for (int i = 0; i < segments; i++) {
            colors[i] = Color.HSVToColor(hsv);
            hsv[0] += hueStep;
        }
        return colors;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("AB", "down: x = " + event.getX() + " y = " + event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("AB", "move: x = " + event.getX() + " y = " + event.getY());
                break;
        }
        return true;
    }
}
