package de.iweinzierl.easyprofiles.widget;

import android.content.Context;
import android.util.AttributeSet;

public class SettingsViewEditText extends AbstractSettingsView<String> {

    public SettingsViewEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setValue(String value) {
        valueField.setText(value);
        invalidate();
    }

    public String getValue() {
        validate(valueField.getText().toString());
        return valueField.getText().toString();
    }

    protected void showEditDialog() {
        new TextSettingsDialogBuilder()
                .withContext(getContext())
                .withLabel(labelField.getText().toString())
                .withOldValue(valueField.getText().toString())
                .withOnEditFinishedListener(new OnEditFinishedListener<String>() {
                    @Override
                    public void onEditFinished(String oldValue, String newValue) {
                        notifyEditFinished(oldValue, newValue);
                    }
                }).show();
    }
}
