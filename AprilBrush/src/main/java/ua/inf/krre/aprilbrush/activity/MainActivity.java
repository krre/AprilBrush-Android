package ua.inf.krre.aprilbrush.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.data.CanvasData;
import ua.inf.krre.aprilbrush.dialog.ColorDialog;
import ua.inf.krre.aprilbrush.logic.BrushEngine;
import ua.inf.krre.aprilbrush.view.PaintView;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    public static final String PREFS_NAME = "prefs";
    private CanvasData canvasData;
    private ColorDialog colorDialog;
    private BrushEngine brushEngine;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        ImageButton undoButton = (ImageButton) findViewById(R.id.undoImageButton);
        undoButton.setOnClickListener(this);
        ImageButton brushButton = (ImageButton) findViewById(R.id.brushImageButton);
        brushButton.setOnClickListener(this);
        ImageButton colorButton = (ImageButton) findViewById(R.id.colorImageButton);
        colorButton.setOnClickListener(this);
        ImageButton fillButton = (ImageButton) findViewById(R.id.fillImageButton);
        fillButton.setOnClickListener(this);
        ImageButton redoButton = (ImageButton) findViewById(R.id.redoImageButton);
        redoButton.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.undoImageButton:
                break;
            case R.id.brushImageButton:
                Intent intent = new Intent(this, BrushSettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.colorImageButton:
                colorDialog = new ColorDialog();
                colorDialog.show(getSupportFragmentManager(), "color");
                break;
            case R.id.fillImageButton:
                canvasData.clear();
                PaintView paintView = (PaintView) findViewById(R.id.paintView);
                paintView.invalidate();
                break;
            case R.id.redoImageButton:
                break;
        }
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
