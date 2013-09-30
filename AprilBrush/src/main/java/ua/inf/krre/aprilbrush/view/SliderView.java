package ua.inf.krre.aprilbrush.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.inf.krre.aprilbrush.R;

public class SliderView extends RelativeLayout {
    private TextView titleView;
    private TextView valueView;
    private SeekBar seekBar;

    public SliderView(Context context) {
        super(context);
        initComponent();
    }

    public void setValue(int value) {
        valueView.setText(String.format("%d", value));
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.slider, this, true);
        titleView = (TextView) findViewById(R.id.slider_title_textView);
        valueView = (TextView) findViewById(R.id.slider_value_textView);
        seekBar = (SeekBar) findViewById(R.id.slider_seekBar);
    }
}
