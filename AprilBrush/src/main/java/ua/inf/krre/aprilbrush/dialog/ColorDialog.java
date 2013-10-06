package ua.inf.krre.aprilbrush.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import ua.inf.krre.aprilbrush.R;

public class ColorDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();
        Resources resources = context.getResources();

        /*
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = resources.getDimensionPixelSize(R.dimen.activity_all_margin);
        layout.setPadding(padding, padding, padding, padding);

        BrushEngine brushEngine = BrushEngine.getInstance();
        List<BrushData.Brush> brushList = brushEngine.getBrushList();

        int colorsPropertyNum = 5;
        for (int i = brushList.size() - colorsPropertyNum; i < brushList.size(); i++) {
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
*/
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.colorpicker, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(linearLayout);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return builder.create();
    }
}

