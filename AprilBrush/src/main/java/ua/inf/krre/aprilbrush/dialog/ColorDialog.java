package ua.inf.krre.aprilbrush.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.LinearLayout;

import java.util.List;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.data.BrushData;
import ua.inf.krre.aprilbrush.logic.BrushEngine;
import ua.inf.krre.aprilbrush.view.SliderView;

public class ColorDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();
        Resources resources = context.getResources();

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = resources.getDimensionPixelSize(R.dimen.activity_all_margin);
        layout.setPadding(padding, padding, padding, padding);

        BrushEngine brushEngine = BrushEngine.getInstance();
        List<BrushData.Brush> brushList = brushEngine.getBrushList();

        for (int i = brushList.size() - 4; i < brushList.size(); i++) {
            SliderView sliderView = new SliderView(context);
            BrushData.Brush brush = brushList.get(i);

            sliderView.setName(brush.getName());
            sliderView.setMin(brush.getMinValue());
            sliderView.setMax(brush.getMaxValue());
            sliderView.setValue(brush.getCurrentValue());
            sliderView.setId(i);
            sliderView.addObserver(brushEngine);
            layout.addView(sliderView);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setMessage(resources.getString(R.string.action_color));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return builder.create();
    }
}

