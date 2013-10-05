package ua.inf.krre.aprilbrush.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

public class ColorPickerView extends View {
    private Shader shader;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int x = 200;
    private int y = 200;

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int segments = 7;
        int hueMax = 360;
        float hueStep = hueMax / (segments - 1);
        int[] colors = new int[segments];
        float[] hsv = {0, 100, 100};
        for (int i = 0; i < segments; i++) {
            colors[i] = Color.HSVToColor(hsv);
            hsv[0] += hueStep;
        }
        shader = new SweepGradient(x, y, colors, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setShader(shader);
        canvas.drawCircle(x, y, 150, paint);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, 100, paint);

    }
}
