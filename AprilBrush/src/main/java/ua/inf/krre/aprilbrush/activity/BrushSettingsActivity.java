package ua.inf.krre.aprilbrush.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.List;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.data.BrushData;
import ua.inf.krre.aprilbrush.logic.BrushEngine;
import ua.inf.krre.aprilbrush.view.SliderView;

public class BrushSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getBaseContext();
        BrushEngine brushEngine = BrushEngine.getInstance();
        List<BrushData.Brush> brushList = brushEngine.getBrushList();

        ScrollView scrollView = new ScrollView(context);
        setContentView(scrollView);
        ScrollView.LayoutParams scrollViewParams = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = getResources().getDimensionPixelSize(R.dimen.brush_settings_padding);
        layout.setPadding(padding, padding, padding, padding);
        
        scrollView.addView(layout, scrollViewParams);

        for (int i = 0; i < brushList.size(); i++) {
            SliderView sliderView = new SliderView(getApplicationContext());
            BrushData.Brush brush = brushList.get(i);

            sliderView.setName(brush.getName());
            sliderView.setMin(brush.getMinValue());
            sliderView.setMax(brush.getMaxValue());
            sliderView.setValue(brush.getCurrentValue());
            sliderView.setId(i);
            sliderView.addObserver(brushEngine);

            layout.addView(sliderView);
        }
    }
}
