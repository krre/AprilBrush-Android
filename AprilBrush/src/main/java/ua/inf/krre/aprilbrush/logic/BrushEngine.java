package ua.inf.krre.aprilbrush.logic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.MotionEvent;

public class BrushEngine {
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
    private int spacing = 15;
    private int size = 7;
    private float angle = 45.0f;
    private float roundness = 1;
    private int color = Color.BLACK;
    private int opacity = 30;

    private BrushEngine() {
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setAlpha(Math.round((float) opacity / 100 * 255));

        path = new Path();
        pathMeasure = new PathMeasure();
    }

    public static BrushEngine getInstance() {
        return engine;
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

        float deltaDab = size * spacing / 100;
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
        canvas.scale(1.0f, 1 / roundness, x, y);
        canvas.drawCircle(x, y, size / 2, paint);
        canvas.restore();
    }
}
