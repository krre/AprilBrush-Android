package ua.inf.krre.aprilbrush.data;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import ua.inf.krre.aprilbrush.AppAprilBrush;
import ua.inf.krre.aprilbrush.R;

public class BrushData {
    private static final BrushData brushData = new BrushData();
    private List<Brush> list;

    private BrushData() {
        Context context = AppAprilBrush.getContext();
        Resources res = context.getResources();
        list = new ArrayList<Brush>();

        list.add(new Brush(res.getString(R.string.brush_size), 1, 150, 20));
        list.add(new Brush(res.getString(R.string.brush_hardness), 0, 100, 80));
        list.add(new Brush(res.getString(R.string.brush_spacing), 1, 100, 15));
        list.add(new Brush(res.getString(R.string.brush_roundness), 1, 100, 100));
        list.add(new Brush(res.getString(R.string.brush_angle), 0, 180, 0));
        list.add(new Brush(res.getString(R.string.brush_scatter), 0, 500, 0));
        list.add(new Brush(res.getString(R.string.color_hue), 0, 360, 0));
        list.add(new Brush(res.getString(R.string.color_saturation), 0, 100, 0));
        list.add(new Brush(res.getString(R.string.color_value), 0, 100, 0));
        list.add(new Brush(res.getString(R.string.brush_opacity), 0, 100, 90));
        list.add(new Brush(res.getString(R.string.brush_flow), 0, 100, 25));
    }

    public static BrushData getInstance() {
        return brushData;
    }

    public List<Brush> getList() {
        return list;
    }

    public int getProperty(Property property) {
        int index = property.ordinal();
        return list.get(index).getCurrentValue();
    }

    // order of the elements is same as order of adding its to the list
    public enum Property {
        SIZE, HARDNESS, SPACING, ROUNDNESS, ANGLE, SCATTER, HUE, SATURATION, VALUE, OPACITY, FLOW
    }

    public class Brush {
        private String name;
        private int minValue;
        private int maxValue;
        private int currentValue;

        public Brush(String name, int minValue, int maxValue, int currentValue) {
            this.name = name;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.currentValue = currentValue;
        }

        public String getName() {
            return name;
        }

        public int getMinValue() {
            return minValue;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public int getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(int currentValue) {
            this.currentValue = currentValue;
        }
    }
}
