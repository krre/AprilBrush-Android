package ua.inf.krre.aprilbrush;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import ua.inf.krre.aprilbrush.data.CanvasData;
import ua.inf.krre.aprilbrush.view.PaintView;

public class MainActivity extends Activity {
    CanvasData canvasData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        canvasData = (CanvasData) getLastNonConfigurationInstance();
        if (canvasData == null) {
            canvasData = new CanvasData();
        }
        PaintView paintView = (PaintView) findViewById(R.id.paintView);
        paintView.setCanvasData(canvasData);
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return canvasData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
