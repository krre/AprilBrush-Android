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
import android.widget.ImageView;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.logic.BrushEngine;

public class ColorPickerView extends View {
    private final static float RING_RATIO = 0.7f;
    private final static int HUE = 0;
    private final static int SATURATION = 1;
    private final static int VALUE = 2;
    boolean hueGrab = false;
    boolean satValGrab = false;
    ImageView colorNewImageView;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint satPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap ringBitmap;
    private Bitmap satBitmap;
    private Bitmap valBitmap;
    private Bitmap hueSelectorBitmap;
    private Bitmap satValSelectorBitmap;
    private Canvas satCanvas;
    private float outerRingRadius;
    private float innerRingRadius;
    private int containerWidth;
    private float rectSize;
    private float[] hsv = new float[3];
    private BrushEngine brushEngine;
    private float xyRect;

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        brushEngine = BrushEngine.getInstance();
    }

    public float[] getHsv() {
        return hsv;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        containerWidth = Math.min(w, h);
        if (containerWidth > 0) {
            hsv = brushEngine.getHsv();
            drawColorPicker(containerWidth);

            View colorPickerLayout = (View) getParent();
            colorNewImageView = (ImageView) colorPickerLayout.findViewById(R.id.colorNewImageView);
        }
    }

    private void drawColorPicker(int width) {
        drawColorRing(width);

        rectSize = (float) (width * RING_RATIO / Math.sqrt(2f));
        xyRect = (width - rectSize) / 2f;

        satBitmap = Bitmap.createBitmap((int) rectSize, (int) rectSize, Bitmap.Config.ARGB_8888);
        satCanvas = new Canvas(satBitmap);

        drawSatRectangle(rectSize);
        drawValRectangle(rectSize);

        drawHueSelector(rectSize);
        drawSatValSelector(rectSize);

        outerRingRadius = width / 2;
        innerRingRadius = outerRingRadius * RING_RATIO;
    }

    private void drawColorRing(int width) {
        ringBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        ringBitmap.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(ringBitmap);

        int center = width / 2;
        Shader ringShader = new SweepGradient(center, center, gradientColors(), null);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(ringShader);

        canvas.drawCircle(center, center, center, paint);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        canvas.drawCircle(center, center, Math.round(RING_RATIO * center), paint);
    }

    private void drawSatRectangle(float width) {
        int hueColor = Color.HSVToColor(255, new float[]{hsv[HUE], 1.0f, 1.0f});
        Shader saturationShader = new LinearGradient(0, 0, width, 0, Color.WHITE, hueColor, Shader.TileMode.CLAMP);
        satPaint.setShader(saturationShader);
        satCanvas.drawRect(0, 0, width, width, satPaint);
    }

    private void drawValRectangle(float width) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Shader valueShader = new LinearGradient(0, 0, 0, 0 + width, 0x00000000, 0xFF000000, Shader.TileMode.CLAMP);
        paint.setShader(valueShader);

        valBitmap = Bitmap.createBitmap((int) width, (int) width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(valBitmap);
        canvas.drawRect(0, 0, width, width, paint);
    }

    private void drawHueSelector(float width) {
        int selectorWidth = (int) (width * (1 - RING_RATIO));
        int selectorHeight = (int) (width / 30);

        hueSelectorBitmap = Bitmap.createBitmap(selectorWidth, selectorHeight, Bitmap.Config.ARGB_8888);
        hueSelectorBitmap.eraseColor(Color.WHITE);
    }

    private void drawSatValSelector(float width) {
        int strokeWidth = 3;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(Color.WHITE);

        int circleSize = Math.round(width / 10);
        satValSelectorBitmap = Bitmap.createBitmap(circleSize, circleSize, Bitmap.Config.ARGB_8888);
        satValSelectorBitmap.eraseColor(Color.TRANSPARENT);

        Canvas canvas = new Canvas(satValSelectorBitmap);
        canvas.drawCircle(circleSize / 2f, circleSize / 2f, (circleSize - strokeWidth) / 2, paint);
    }

    int[] gradientColors() {
        int segments = 7;
        int hueMax = 360;
        float hueStep = hueMax / (segments - 1);
        int[] colors = new int[segments];
        float[] hsv = {0, 100, 100};
        for (int i = 0; i < segments; i++) {
            colors[i] = Color.HSVToColor(hsv);
            hsv[HUE] += hueStep;
        }
        return colors;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(ringBitmap, 0, 0, paint);
        canvas.drawBitmap(satBitmap, xyRect, xyRect, paint);
        canvas.drawBitmap(valBitmap, xyRect, xyRect, paint);
        canvas.drawBitmap(hueSelectorBitmap, 0, 0, paint);
        canvas.drawBitmap(satValSelectorBitmap, 0, 0, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // origin moved to the center of color ring
        float x = event.getX() - containerWidth / 2;
        float y = containerWidth / 2 - event.getY();
        Log.d("AB", "origin: x = " + x + " y = " + y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                processTouchDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                processTouchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                processTouchUp();
        }
        return true;
    }

    private void processTouchDown(float x, float y) {
        double angle = Math.atan2(x, y);
        double length = Math.hypot(x, y);
        if (length >= innerRingRadius && length <= outerRingRadius) {
            changeHue(angle);
            hueGrab = true;
        }

        if (length < innerRingRadius) {
            changeSatVal(x, y);
            satValGrab = true;
        }
        invalidate();
    }

    private void processTouchMove(float x, float y) {
        double angle = Math.atan2(x, y);
        if (hueGrab) {
            changeHue(angle);
        }
        if (satValGrab) {
            changeSatVal(x, y);
        }
        invalidate();
    }

    private void changeHue(double angle) {
        int hue = (int) (angle * 180 / Math.PI - 90);
        if (hue < 0) {
            hue += 360;
        }
        hsv[HUE] = hue;
        drawSatRectangle(rectSize);
        colorNewImageView.setBackgroundColor(Color.HSVToColor(hsv));
    }

    private void changeSatVal(float x, float y) {
        float saturation = (x + rectSize / 2) / rectSize;
        hsv[SATURATION] = Math.max(0, Math.min(saturation, 1));

        float value = (y + rectSize / 2) / rectSize;
        hsv[VALUE] = Math.max(0, Math.min(value, 1));

        colorNewImageView.setBackgroundColor(Color.HSVToColor(hsv));
    }

    private void processTouchUp() {
        hueGrab = false;
        satValGrab = false;
    }
}
