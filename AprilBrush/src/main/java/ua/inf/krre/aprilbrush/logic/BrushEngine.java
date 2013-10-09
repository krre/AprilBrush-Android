package ua.inf.krre.aprilbrush.logic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.view.MotionEvent;

import java.util.Observable;
import java.util.Observer;

import ua.inf.krre.aprilbrush.data.BrushData;
import ua.inf.krre.aprilbrush.data.CanvasData;
import ua.inf.krre.aprilbrush.view.SliderView;

public class BrushEngine implements Observer {
    private static BrushEngine brushEngine = new BrushEngine();
    private Paint paint;
    private Canvas canvas;
    private Path path;
    private CanvasData canvasData;
    private PathMeasure pathMeasure;
    private float[] pathMeasurePos = new float[2];
    private float[] pathMeasureTan = new float[2];
    private float pathLength;
    private float prevX;
    private float prevY;
    private float hsv[] = new float[3];
    private int alpha;
    private int[] colors = new int[3];
    private int[] eraserColors = new int[3];
    private float[] positions = new float[3];
    private int[] brushList;

    private BrushEngine() {
        canvasData = CanvasData.getInstance();

        paint = new Paint(Paint.DITHER_FLAG);
        paint.setAntiAlias(true);

        path = new Path();
        pathMeasure = new PathMeasure();

        brushList = new int[BrushData.getInstance().getList().size()];
    }

    public static BrushEngine getInstance() {
        return brushEngine;
    }

    public void setEraserColors(float[] hsv) {
        eraserColors[0] = Color.HSVToColor(alpha, hsv);
        eraserColors[1] = Color.HSVToColor(alpha, hsv);
        eraserColors[2] = Color.HSVToColor(0, hsv);
    }

    public float[] getHsv() {
        return hsv;
    }

    public void setHsv(float[] hsv) {
        System.arraycopy(hsv, 0, this.hsv, 0, hsv.length);

        setBrushColors();
        setValue(BrushData.Property.HUE, (int) hsv[0]);
        setValue(BrushData.Property.SATURATION, (int) (hsv[1] * 100));
        setValue(BrushData.Property.VALUE, (int) (hsv[2] * 100));
    }

    public int[] getBrushList() {
        return brushList;
    }

    public int getValue(BrushData.Property property) {
        return brushList[property.ordinal()];
    }

    public int getValue(int index) {
        return brushList[index];
    }

    public void setValue(BrushData.Property property, int value) {
        brushList[property.ordinal()] = value;
    }

    public void setValue(int index, int value) {
        brushList[index] = value;
    }

    private BrushData.Property property(int ordinal) {
        return BrushData.Property.values()[ordinal];
    }

    public void update(Observable obj, Object arg) {
        SliderView sliderView = (SliderView) arg;
        int index = sliderView.getId();
        int value = sliderView.getValue();
        brushList[index] = value;

        BrushData.Property property = property(index);
        if (property == BrushData.Property.HUE ||
                property == BrushData.Property.SATURATION ||
                property == BrushData.Property.VALUE ||
                property == BrushData.Property.FLOW ||
                property == BrushData.Property.OPACITY ||
                property == BrushData.Property.HARDNESS) {
            setupColor();
        }
    }

    public void setupColor() {

        hsv[0] = getValue(BrushData.Property.HUE);
        hsv[1] = getValue(BrushData.Property.SATURATION) / 100f;
        hsv[2] = getValue(BrushData.Property.VALUE) / 100f;

        alpha = Math.round((float) getValue(BrushData.Property.FLOW) / 100 * 255);
        float hardness = getValue(BrushData.Property.HARDNESS) / 100f;

        setBrushColors();

        positions[0] = 0;
        positions[1] = (float) (Math.sqrt(hardness));
        positions[2] = 1;

        if (canvasData != null) {
            canvasData.setOpacity(getValue(BrushData.Property.OPACITY));
        }
    }

    private void setBrushColors() {
        colors[0] = Color.HSVToColor(alpha, hsv);
        colors[1] = Color.HSVToColor(alpha, hsv);
        colors[2] = Color.HSVToColor(0, hsv);
    }

    public void setTouch(Canvas canvas, MotionEvent event) {
        this.canvas = canvas;
        float x = event.getX();
        float y = event.getY();
        paintOneDab(x, y);

        path.reset();
        path.moveTo(x, y);
        pathLength = 0;

        prevX = x;
        prevY = y;
    }

    public void paintDabs(MotionEvent event) {
        float x, y;
        for (int i = 0; i < event.getHistorySize(); i++) {
            x = event.getHistoricalX(i);
            y = event.getHistoricalY(i);
            interpolateDabs(x, y);
        }
        x = event.getX();
        y = event.getY();
        interpolateDabs(x, y);
    }

    private void interpolateDabs(float x, float y) {
        double pointSpace = Math.sqrt(Math.pow(prevX - x, 2)
                + Math.pow(prevY - y, 2));

        int size = getValue(BrushData.Property.SIZE);
        int spacing = getValue(BrushData.Property.SPACING);
        float deltaDab = size * (float) spacing / 100;
        if (pointSpace >= deltaDab) {
            path.quadTo(prevX, prevY, (x + prevX) / 2, (y + prevY) / 2);
        } else {
            path.lineTo(x, y);
        }
        pathMeasure.setPath(path, false);
        while (pathMeasure.getLength() >= pathLength) {
            pathMeasure.getPosTan(pathLength, pathMeasurePos, pathMeasureTan);
            if (pathLength > 0) {
                paintOneDab(pathMeasurePos[0], pathMeasurePos[1]);
            }
            pathLength += deltaDab;
        }
        prevX = x;
        prevY = y;
    }

    private void paintOneDab(float x, float y) {
        float radius = getValue(BrushData.Property.SIZE) / 2f;

        float scatter = getValue(BrushData.Property.SCATTER) / 100f;
        x += radius * 2 * scatter * (1 - 2 * (float) Math.random());
        y += radius * 2 * scatter * (1 - 2 * (float) Math.random());

        if (BrushData.getInstance().isBrushMode()) {
            paint.setShader(new RadialGradient(x, y, radius, colors, positions, Shader.TileMode.CLAMP));
        } else {
            paint.setShader(new RadialGradient(x, y, radius, eraserColors, positions, Shader.TileMode.CLAMP));
        }

        canvas.save();

        int angle = getValue(BrushData.Property.ANGLE);
        canvas.rotate(angle, x, y);

        int roundness = getValue(BrushData.Property.ROUNDNESS);
        canvas.scale(1.0f, 100f / roundness, x, y);

        canvas.drawCircle(x, y, radius, paint);

        canvas.restore();
    }

    @Override
    public String toString() {
        String properties = "";
        for (int i = 0; i < brushList.length; i++) {
            BrushData.Property property = BrushData.Property.values()[i];
            String name = property.toString();
            int value = getValue(property);
            properties = name + " = " + value + "\n";
        }
        return properties;
    }
}
