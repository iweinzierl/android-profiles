package de.iweinzierl.easyprofiles.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.google.common.base.Strings;

import de.iweinzierl.easyprofiles.R;

public class SettingsViewSlider extends AbstractSettingsView<Integer> {

    public SettingsViewSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Integer rawValue;

    @Override
    public void setValue(Integer value) {
        this.rawValue = value;

        String volumeSetTo = getContext().getResources().getString(R.string.editprofile_volume_set_to);
        volumeSetTo = volumeSetTo.replace("{volume}", String.valueOf(value));
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
}
