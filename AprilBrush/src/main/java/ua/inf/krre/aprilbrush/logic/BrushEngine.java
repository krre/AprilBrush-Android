package ua.inf.krre.aprilbrush.logic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ua.inf.krre.aprilbrush.data.BrushData;
import ua.inf.krre.aprilbrush.view.SliderView;

public class BrushEngine implements Observer {
    private static BrushEngine engine = new BrushEngine();
    private Paint paint;
    private Canvas canvas;
    private Path path;
    private PathMeasure pathMeasure;
    private float[] pathMeasurePos = new float[2];
    private float[] pathMeasureTan = new float[2];
    private float pathLength;
    private float prevX;
    private float prevY;
    private int color;
    private int alpha;
    private List<BrushData.Brush> brushList;

    private BrushEngine() {

        paint = new Paint();
        paint.setAntiAlias(true);

        path = new Path();
        pathMeasure = new PathMeasure();

        getBrushValues();
    }

    public static BrushEngine getInstance() {
        return engine;
    }

    public int getColor() {
        return color;
    }

    public List<BrushData.Brush> getBrushList() {

        return brushList;
    }

    private int value(BrushData.Property property) {
        return brushList.get(property.ordinal()).getCurrentValue();
    }

    private BrushData.Property property(int ordinal) {
        return BrushData.Property.values()[ordinal];
    }

    public void update(Observable obj, Object arg) {
        SliderView sliderView = (SliderView) arg;
        int id = sliderView.getId();
        BrushData.Brush brush = brushList.get(id);
        int value = sliderView.getValue();
        brush.setCurrentValue(value);

        BrushData.Property property = property(id);
        if (property == BrushData.Property.HUE || property == BrushData.Property.SATURATION || property == BrushData.Property.VALUE) {
            float[] hsv = {value(BrushData.Property.HUE), value(BrushData.Property.SATURATION), value(BrushData.Property.VALUE)};
            color = Color.HSVToColor(alpha, hsv);
            paint.setColor(color);
        }

        if (property == BrushData.Property.OPACITY) {
            alpha = Math.round((float) value / 100 * 255);
            paint.setAlpha(alpha);
        }
    }

    private void getBrushValues() {
        brushList = BrushData.getInstance().getList();
        brushList = new ArrayList<BrushData.Brush>(brushList);

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

        int size = value(BrushData.Property.SIZE);
        int spacing = value(BrushData.Property.SPACING);
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
        canvas.save();
        int angle = value(BrushData.Property.ANGLE);
        canvas.rotate(angle, x, y);
        int roundness = value(BrushData.Property.ROUNDNESS);
        canvas.scale(1.0f, 100f / roundness, x, y);
        int size = value(BrushData.Property.SIZE);
        canvas.drawCircle(x, y, size / 2, paint);
        canvas.restore();
    }

    @Override
    public String toString() {
        String properties = "";
        for (int i = 0; i < brushList.size(); i++) {
            BrushData.Property property = BrushData.Property.values()[i];
            String name = property.toString();
            int value = value(property);
            properties = name + " = " + value + "\n";
        }
        return properties;
    }
}
