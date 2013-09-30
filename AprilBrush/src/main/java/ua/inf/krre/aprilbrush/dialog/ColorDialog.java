package ua.inf.krre.aprilbrush.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.logic.BrushEngine;

public class ColorDialog extends DialogFragment {
    private static final int HUE = 0;
    private static final int SATURATION = 1;
    private static final int VALUE = 2;
    private static final int SEEKBAR_HUE_MAX = 360;
    private static final int SEEKBAR_SAT_MAX = 100;
    private static final int SEEKBAR_VAL_MAX = 100;
    private float hsv[] = new float[3];
    private BrushEngine brushEngine;
    private TextView hueText;
    private TextView satText;
    private TextView valText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        brushEngine = BrushEngine.getInstance();
        Color.colorToHSV(brushEngine.getColor(), hsv);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_color, null);

        hueText = (TextView) layout.findViewById(R.id.hue_value_textView);
        hueText.setText(String.format("%d", Math.round(hsv[HUE])));
        satText = (TextView) layout.findViewById(R.id.sat_value_textView);
        satText.setText(String.format("%d", Math.round(hsv[SATURATION] * SEEKBAR_SAT_MAX)));
        valText = (TextView) layout.findViewById(R.id.val_value_textView);
        valText.setText(String.format("%d", Math.round(hsv[VALUE] * SEEKBAR_VAL_MAX)));

        SeekBar seekBarHue = (SeekBar) layout.findViewById(R.id.hue_seekBar);
        seekBarHue.setMax(SEEKBAR_HUE_MAX);
        seekBarHue.setProgress((int) hsv[HUE]);
        seekBarHue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                hsv[HUE] = (float) progress;
                hueText.setText(String.format("%d", progress));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });

        SeekBar seekBarSat = (SeekBar) layout.findViewById(R.id.sat_seekBar);
        seekBarSat.setProgress(Math.round(hsv[SATURATION] * SEEKBAR_SAT_MAX));

        seekBarSat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                hsv[SATURATION] = (float) progress / SEEKBAR_SAT_MAX;
                satText.setText(String.format("%d", progress));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });

        SeekBar seekBarVal = (SeekBar) layout.findViewById(R.id.val_seekBar);
        seekBarVal.setProgress(Math.round(hsv[VALUE] * SEEKBAR_VAL_MAX));
        seekBarVal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                hsv[VALUE] = (float) progress / SEEKBAR_VAL_MAX;
                valText.setText(String.format("%d", progress));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });

        builder.setView(layout);
        builder.setMessage(R.string.action_color);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int color = Color.HSVToColor(brushEngine.getOpacity(), hsv);
                brushEngine.setColor(color);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return builder.create();
    }
}

