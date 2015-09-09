package de.iweinzierl.easyprofiles.widget;

import android.content.Context;
import android.util.AttributeSet;

import de.iweinzierl.easyprofiles.R;

public class SettingsViewCheckbox extends AbstractSettingsView<Boolean> {

    public SettingsViewCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Boolean rawValue;

    @Override
    public void setValue(Boolean value) {
        this.rawValue = value;

        String volumeSetTo = getContext().getResources().getString(R.string.editprofile_volume_set_to);
        volumeSetTo = volumeSetTo.replace("{volume}", String.valueOf(value));
        valueField.setText(volumeSetTo);
        invalidate();
    }

    @Override
    public Boolean getValue() {
        validate(rawValue);
        if (rawValue == null) {
            return Boolean.FALSE;
        } else {
            return rawValue;
        }
    }

    @Override
    protected void showEditDialog() {
        new CheckboxSettingsDialogBuilder()
                .withContext(getContext())
                .withLabel(labelField.getText().toString())
                .withOldValue(getValue())
                .withOnEditFinishedListener(new OnEditFinishedListener<Boolean>() {
                    @Override
                    public void onEditFinished(Boolean oldValue, Boolean newValue) {
                        notifyEditFinished(oldValue, newValue);
                    }
                }).show();
    }
}
