package de.iweinzierl.easyprofiles.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import de.iweinzierl.easyprofiles.R;

public class SlideSettingsDialogBuilder extends AbstractSettingsDialogBuilder<Integer> {

    private SeekBar valueField;

    private int maxValue = 10;

    @Override
    protected View createContent() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.dialog_settings_view_slider, null);

        final TextView titleField = (TextView) content.findViewById(R.id.title);
        final TextView labelField = (TextView) content.findViewById(R.id.label);
        valueField = (SeekBar) content.findViewById(R.id.value);

        titleField.setText(R.string.edittextdialog_title);
        labelField.setText(getLabel());
        valueField.setProgress(getOldValue());
        valueField.setMax(maxValue);

        return content;
    }

    @Override
    protected Integer getNewValue() {
        return valueField.getProgress();
    }

    public SlideSettingsDialogBuilder withMaxValue(int maxValue) {
        this.maxValue = maxValue;
        return this;
    }
}
