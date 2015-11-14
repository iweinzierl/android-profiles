package de.iweinzierl.easyprofiles.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import de.iweinzierl.easyprofiles.R;

public class SettingsViewSlider extends AbstractSettingsView<Integer> {

    private int maxValue;

    private Integer rawValue;

    public SettingsViewSlider(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SettingsViewSlider,
                0, 0);

        try {
            maxValue = a.getInt(R.styleable.SettingsViewSlider_maxValue, 10);
        } finally {
            a.recycle();
        }
    }

    @Override
    public void setValue(Integer value) {
        this.rawValue = value;

        String volumeSetTo = valueTemplate;
        volumeSetTo = volumeSetTo.replace("{value}", String.valueOf(value));
        volumeSetTo = volumeSetTo.replace("{maxValue}", String.valueOf(maxValue));
        valueField.setText(volumeSetTo);
        invalidate();
    }

    @Override
    public Integer getValue() {
        validate(rawValue);
        if (rawValue == null) {
            return 0;
        } else {
            return rawValue;
        }
    }

    @Override
    protected void showEditDialog() {
        new SlideSettingsDialogBuilder()
                .withMaxValue(getMaxValue())
                .withContext(getContext())
                .withLabel(labelField.getText().toString())
                .withOldValue(getValue())
                .withOnEditFinishedListener(new OnEditFinishedListener<Integer>() {
                    @Override
                    public void onEditFinished(Integer oldValue, Integer newValue) {
                        notifyEditFinished(oldValue, newValue);
                    }
                }).show();
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
