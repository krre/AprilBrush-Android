package ua.inf.krre.aprilbrush.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.data.BrushData;
import ua.inf.krre.aprilbrush.data.CanvasData;
import ua.inf.krre.aprilbrush.dialog.ColorDialog;
import ua.inf.krre.aprilbrush.logic.JSONSharedPreferences;
import ua.inf.krre.aprilbrush.logic.UndoManager;
import ua.inf.krre.aprilbrush.view.PaintView;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    public static final String PREFS_NAME = "prefs";
    private static final int SELECT_PICTURE = 1;
    private ColorDialog colorDialog;
    private UndoManager undoManager;
    private PaintView paintView;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        ImageButton newButton = (ImageButton) findViewById(R.id.newImageButton);
        newButton.setOnClickListener(this);
        ImageButton loadButton = (ImageButton) findViewById(R.id.loadImageButton);
        loadButton.setOnClickListener(this);
        ImageButton saveButton = (ImageButton) findViewById(R.id.saveImageButton);
        saveButton.setOnClickListener(this);
        ImageButton helpButton = (ImageButton) findViewById(R.id.helpImageButton);
        helpButton.setOnClickListener(this);

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

        try {
            JSONSharedPreferences.loadJSONArray(getBaseContext(), PREFS_NAME, BrushData.PREF_ITEM_NAME);
        } catch (JSONException e) {
            throw new RuntimeException();
        }

        undoManager = UndoManager.getInstance();
        paintView = (PaintView) findViewById(R.id.paintView);
        colorDialog = new ColorDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newImageButton:
                CanvasData.getInstance().newImage();
                paintView.invalidate();
                break;
            case R.id.loadImageButton:
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                break;
            case R.id.saveImageButton:
                CanvasData.getInstance().saveImage();
                break;
            case R.id.helpImageButton:
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
                colorDialog.show(getSupportFragmentManager(), "color");
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
    protected void onStop() {
        super.onStop();
        JSONSharedPreferences.saveJSONArray(getBaseContext(), PREFS_NAME, BrushData.PREF_ITEM_NAME, new JSONArray(BrushData.getInstance().getList()));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                CanvasData.getInstance().loadImage(selectedImagePath);
                paintView.invalidate();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }
}
