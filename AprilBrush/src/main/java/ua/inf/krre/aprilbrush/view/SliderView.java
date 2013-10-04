package ua.inf.krre.aprilbrush.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import ua.inf.krre.aprilbrush.R;

public class SliderView extends RelativeLayout {
    private TextView nameView;
    private TextView valueView;
    private SeekBar seekBar;
    private int minValue;
    private int value;
    private int id;
    private SliderObservable sliderObservable;

    public SliderView(Context context) {
        super(context);
        sliderObservable = new SliderObservable();
        initComponent();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        seekBar.setProgress(value);
        valueView.setText(String.format("%d", value));
    }

    public String getName() {
        return nameView.getText().toString();
    }

    public void setName(String name) {
        nameView.setText(name);
    }

    public void setMax(int value) {
        seekBar.setMax(value - minValue);
    }

    public void setMin(int value) {
        this.minValue = value;
    }

    public void addObserver(Observer observer) {
        sliderObservable.addObserver(observer);
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.slider, this, true);
        nameView = (TextView) findViewById(R.id.slider_name_textView);
        valueView = (TextView) findViewById(R.id.slider_value_textView);

        seekBar = (SeekBar) findViewById(R.id.slider_seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                value = minValue + progress;
                valueView.setText(String.format("%d", value));
                sliderObservable.onValueChanged(SliderView.this);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
    }

    public class SliderObservable extends Observable {
        public void onValueChanged(SliderView sliderView) {
            setChanged();
            notifyObservers(sliderView);
        }
    }
}
