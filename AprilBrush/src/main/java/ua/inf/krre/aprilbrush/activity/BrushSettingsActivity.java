package ua.inf.krre.aprilbrush.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.List;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.data.BrushData;
import ua.inf.krre.aprilbrush.logic.BrushEngine;
import ua.inf.krre.aprilbrush.view.SliderView;

public class BrushSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brush_settings);
        BrushEngine brushEngine = BrushEngine.getInstance();
        List<BrushData.Brush> brushList = brushEngine.getBrushList();

        LinearLayout slidersContainer = (LinearLayout) findViewById(R.id.sliders_container);
        for (int i = 0; i < brushList.size(); i++) {
            SliderView sliderView = new SliderView(getApplicationContext());
            BrushData.Brush brush = brushList.get(i);

            sliderView.setName(brush.getName());
            sliderView.setMin(brush.getMinValue());
            sliderView.setMax(brush.getMaxValue());
            sliderView.setValue(brush.getCurrentValue());
            sliderView.setId(i);
            sliderView.addObserver(brushEngine);
            slidersContainer.addView(sliderView);
        }
    }
}
