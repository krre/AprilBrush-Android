package ua.inf.krre.aprilbrush.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.data.CanvasData;
import ua.inf.krre.aprilbrush.logic.BrushEngine;
import ua.inf.krre.aprilbrush.view.ColorPickerView;

public class ColorDialog extends DialogFragment {

    private ColorPickerView colorPickerView;
    private BrushEngine brushEngine;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.colorpicker, null);

        colorPickerView = (ColorPickerView) layout.findViewById(R.id.colorPickerView);

        brushEngine = BrushEngine.getInstance();

        int color;
        if (getTag().equals("brush")) {
            color = Color.HSVToColor(brushEngine.getHsv());
        } else {
            color = Color.HSVToColor(CanvasData.getInstance().getFillColor());
        }

        ImageView colorPrevImageView = (ImageView) layout.findViewById(R.id.colorPrevImageView);
        colorPrevImageView.setBackgroundColor(color);

        ImageView colorNewImageView = (ImageView) layout.findViewById(R.id.colorNewImageView);
        colorNewImageView.setBackgroundColor(color);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getTag().equals("brush")) {
                    brushEngine.setHsv(colorPickerView.getHsv());
                } else {
                    CanvasData.getInstance().setFillColor(colorPickerView.getHsv());
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getTag().equals("brush")) {
            colorPickerView.setHsv(brushEngine.getHsv());
        } else {
            colorPickerView.setHsv(CanvasData.getInstance().getFillColor());
        }
    }
}

