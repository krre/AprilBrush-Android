package ua.inf.krre.aprilbrush.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.data.BrushData;
import ua.inf.krre.aprilbrush.view.SliderView;

public class BrushSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brush_settings);
        BrushData brushData = BrushData.getInstance();

        LinearLayout slidersContainer = (LinearLayout) findViewById(R.id.sliders_container);
        for (int i = 0; i < brushData.getList().size(); i++) {
            SliderView sliderView = new SliderView(getApplicationContext());
            BrushData.Brush brush = brushData.getList().get(i);

            sliderView.setTitle(brush.getName());
            sliderView.setMin(brush.getMinValue());
            sliderView.setMax(brush.getMaxValue());
            sliderView.setValue(brush.getDefaultValue());
            slidersContainer.addView(sliderView);
        }
    }
}
