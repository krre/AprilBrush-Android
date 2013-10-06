package ua.inf.krre.aprilbrush.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.logic.BrushEngine;

public class ColorDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.colorpicker, null);

        int color = BrushEngine.getInstance().getColor() | 0xFF000000;

        ImageView colorPrevImageView = (ImageView) layout.findViewById(R.id.colorPrevImageView);
        colorPrevImageView.setBackgroundColor(color);

        ImageView colorNewImageView = (ImageView) layout.findViewById(R.id.colorNewImageView);
        colorNewImageView.setBackgroundColor(color);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return builder.create();
    }
}

