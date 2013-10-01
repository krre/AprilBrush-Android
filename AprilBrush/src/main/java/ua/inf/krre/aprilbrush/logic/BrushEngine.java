package ua.inf.krre.aprilbrush.logic;

import android.content.Context;
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

import ua.inf.krre.aprilbrush.AppAprilBrush;
import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.data.BrushData;
import ua.inf.krre.aprilbrush.view.SliderView;

public class BrushEngine extends Object implements Observer {
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
    private int hue;
    private int saturation;
    private int value;
    private int alpha;
    private int size;
    private int spacing;
    private int angle;
    private int roundness;
    private int color;
    private int opacity;
    private Context context;
    private List<BrushData.Brush> brushList;

    private BrushEngine() {
        context = AppAprilBrush.getContext();

        paint = new Paint();
        paint.setAntiAlias(true);

        path = new Path();
        pathMeasure = new PathMeasure();

        getBrushValues();
    }

    public static BrushEngine getInstance() {
        return engine;
    }

    public void update(Observable obj, Object arg) {
        SliderView sliderView = (SliderView) arg;
        setProperty(sliderView.getTitle(), sliderView.getValue());
    }

    public void setHue(int hue) {
        this.hue = hue;
        setHsv();
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
        setHsv();
    }

    public void setValue(int value) {
        this.value = value;
        setHsv();
    }

    private void setHsv() {
        float[] hsv = {hue, saturation, value};
        color = Color.HSVToColor(alpha, hsv);
        paint.setColor(color);
    }

    private void getBrushValues() {
        brushList = BrushData.getInstance().getList();
        brushList = new ArrayList<BrushData.Brush>(brushList);
        for (BrushData.Brush brush : brushList) {
            setProperty(brush.getName(), brush.getDefaultValue());
        }
    }

    private void setProperty(String propertyName, int property) {
        if (propertyName.equals(context.getString(R.string.brush_size))) {
            size = property;
            return;
        }
        if (propertyName.equals(context.getString(R.string.brush_spacing))) {
            spacing = property;
            return;
        }
        if (propertyName.equals(context.getString(R.string.brush_opacity))) {
            setOpacity(property);
            return;
        }
        if (propertyName.equals(context.getString(R.string.brush_roundness))) {
            roundness = property;
            return;
        }
        if (propertyName.equals(context.getString(R.string.brush_angle))) {
            angle = property;
            return;
        }
        if (propertyName.equals(context.getString(R.string.color_hue))) {
            setHue(property);
            return;
        }
        if (propertyName.equals(context.getString(R.string.color_saturation))) {
            setSaturation(property);
            return;
        }
        if (propertyName.equals(context.getString(R.string.color_value))) {
            setValue(property);
        }
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
        alpha = Math.round((float) opacity / 100 * 255);
        paint.setAlpha(alpha);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
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
        canvas.rotate(angle, x, y);
        canvas.scale(1.0f, 100f / roundness, x, y);
        canvas.drawCircle(x, y, size / 2, paint);
        canvas.restore();
    }

    @Override
    public String toString() {
        return "Color = " + color + "\n" +
                "Size = " + size + "\n" +
                "Spacing = " + spacing + "\n" +
                "Opacity = " + opacity + "\n" +
                "Roundness = " + roundness + "\n" +
                "Angle = " + angle + "\n";
    }
}
