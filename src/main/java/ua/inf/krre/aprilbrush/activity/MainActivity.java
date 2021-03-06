package ua.inf.krre.aprilbrush.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.data.BrushData;
import ua.inf.krre.aprilbrush.data.CanvasData;
import ua.inf.krre.aprilbrush.dialog.ColorDialog;
import ua.inf.krre.aprilbrush.logic.BrushEngine;
import ua.inf.krre.aprilbrush.logic.UndoManager;
import ua.inf.krre.aprilbrush.view.PaintView;

public class MainActivity extends FragmentActivity implements View.OnClickListener, View.OnLongClickListener {
    public static final String PREFS_NAME = "prefs";
    private static final int SELECT_PICTURE = 1;
    private ColorDialog colorDialog;
    private UndoManager undoManager;
    private PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loadPreferences();
        setupButtons();

        undoManager = UndoManager.getInstance();
        paintView = (PaintView) findViewById(R.id.paintView);
        colorDialog = new ColorDialog();
    }

    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        BrushEngine brushEngine = BrushEngine.getInstance();
        BrushData brushData = BrushData.getInstance();
        for (int i = 0; i < brushEngine.getBrushList().length; i++) {
            int value = settings.getInt(BrushData.Property.values()[i].toString(), brushData.getProperty(i));
            brushEngine.setValue(i, value);
        }
        brushEngine.setupColor();

        boolean setBrushMode = settings.getBoolean("brushMode", true);
        BrushData.getInstance().setBrushMode(setBrushMode);
        int fillColor = settings.getInt("fillColor", Color.WHITE);
        float[] hsv = new float[3];
        Color.colorToHSV(fillColor, hsv);
        CanvasData.getInstance().setFillColor(hsv);
    }

    private void setupButtons() {
        // top tool bar
        ImageButton newButton = (ImageButton) findViewById(R.id.newImageButton);
        newButton.setOnClickListener(this);

        ImageButton loadButton = (ImageButton) findViewById(R.id.loadImageButton);
        loadButton.setOnClickListener(this);

        ImageButton saveButton = (ImageButton) findViewById(R.id.saveImageButton);
        saveButton.setOnClickListener(this);

        ImageButton helpButton = (ImageButton) findViewById(R.id.helpImageButton);
        helpButton.setOnClickListener(this);

        // bottom tool bar
        ImageButton undoButton = (ImageButton) findViewById(R.id.undoImageButton);
        undoButton.setOnClickListener(this);

        ImageButton brushButton = (ImageButton) findViewById(R.id.brushImageButton);
        brushButton.setOnClickListener(this);
        brushButton.setOnLongClickListener(this);

        ImageButton colorButton = (ImageButton) findViewById(R.id.colorImageButton);
        colorButton.setOnClickListener(this);

        ImageButton fillButton = (ImageButton) findViewById(R.id.fillImageButton);
        fillButton.setOnClickListener(this);
        fillButton.setOnLongClickListener(this);

        ImageButton redoButton = (ImageButton) findViewById(R.id.redoImageButton);
        redoButton.setOnClickListener(this);

        if (BrushData.getInstance().isBrushMode()) {
            brushButton.setImageResource(R.drawable.paintbrush);
        } else {
            brushButton.setImageResource(R.drawable.draw_eraser);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newImageButton:
                CanvasData.getInstance().newImage();
                paintView.invalidate();
                break;
            case R.id.loadImageButton:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                break;
            case R.id.saveImageButton:
                CanvasData.getInstance().saveImage();
                break;
            case R.id.helpImageButton:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.undoImageButton:
                undoManager.undo();
                paintView.invalidate();
                break;
            case R.id.brushImageButton:
                intent = new Intent(this, BrushSettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.colorImageButton:
                colorDialog.show(getSupportFragmentManager(), "brush");
                break;
            case R.id.fillImageButton:
                CanvasData.getInstance().clear();
                paintView.invalidate();
                break;
            case R.id.redoImageButton:
                undoManager.redo();
                paintView.invalidate();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.brushImageButton:
                BrushData brushData = BrushData.getInstance();
                brushData.toggleBrushMode();
                ImageButton brushButton = (ImageButton) findViewById(R.id.brushImageButton);
                if (brushData.isBrushMode()) {
                    brushButton.setImageResource(R.drawable.paintbrush);
                } else {
                    brushButton.setImageResource(R.drawable.draw_eraser);
                }
                break;
            case R.id.fillImageButton:
                colorDialog.show(getSupportFragmentManager(), "fill");
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePreferences();
    }

    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("brushMode", BrushData.getInstance().isBrushMode());
        int color = Color.HSVToColor(CanvasData.getInstance().getFillColor());
        editor.putInt("fillColor", color);

        BrushEngine brushEngine = BrushEngine.getInstance();
        for (int i = 0; i < brushEngine.getBrushList().length; i++) {
            int value = brushEngine.getValue(i);
            editor.putInt(BrushData.Property.values()[i].toString(), value);
        }

        editor.commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                CanvasData.getInstance().loadImage(selectedImagePath);
                paintView.invalidate();
            }
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }
}
