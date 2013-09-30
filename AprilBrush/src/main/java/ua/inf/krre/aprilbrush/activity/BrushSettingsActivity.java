package ua.inf.krre.aprilbrush.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import ua.inf.krre.aprilbrush.R;
import ua.inf.krre.aprilbrush.view.SliderView;

public class BrushSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brush_settings);

        LinearLayout slidersContainer = (LinearLayout) findViewById(R.id.sliders_container);
        for (int i = 0; i < 4; i++) {
            SliderView sliderView = new SliderView(getApplicationContext());
            sliderView.setTitle("Name");
            sliderView.setValue(100);
            slidersContainer.addView(sliderView);
        }
    }
}
