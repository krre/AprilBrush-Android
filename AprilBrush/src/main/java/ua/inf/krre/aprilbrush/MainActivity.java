package ua.inf.krre.aprilbrush;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                canvasData.clear();
                PaintView paintView = (PaintView) findViewById(R.id.paintView);
                paintView.invalidate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
