package ua.inf.krre.aprilbrush.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.data.CanvasData;
import ua.inf.krre.aprilbrush.dialog.ColorDialog;
import ua.inf.krre.aprilbrush.logic.BrushEngine;
import ua.inf.krre.aprilbrush.view.PaintView;

public class MainActivity extends FragmentActivity {
    public static final String PREFS_NAME = "prefs";
    CanvasData canvasData;
    ColorDialog colorDialog;
    BrushEngine brushEngine;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        brushEngine = BrushEngine.getInstance();

        canvasData = (CanvasData) getLastNonConfigurationInstance();
        if (canvasData == null) {
            canvasData = new CanvasData();
        }
        PaintView paintView = (PaintView) findViewById(R.id.paintView);
        paintView.setCanvasData(canvasData);

        settings = getSharedPreferences(PREFS_NAME, 0);
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(requestedOrientation);
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
            case R.id.action_color:
                colorDialog = new ColorDialog();
                colorDialog.show(getSupportFragmentManager(), "color");
                return true;
            case R.id.action_brush:
                Intent intent = new Intent(this, BrushSettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("color", brushEngine.getColor());
        editor.commit();
    }
}
