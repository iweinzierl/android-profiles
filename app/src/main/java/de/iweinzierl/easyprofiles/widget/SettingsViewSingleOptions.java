package de.iweinzierl.easyprofiles.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

public class SettingsViewSingleOptions extends AbstractSettingsView<SettingsViewSingleOptions.Option> {

    public static class Option {
        public String label;
        public Object value;

        public Option(String label, Object value) {
            this.label = label;
            this.value = value;
        }
    }

    public SettingsViewSingleOptions(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private List<Option> options;
    private Option rawValue;

    @Override
    public void setValue(Option value) {
        this.rawValue = value;

        valueField.setText(value.label);
        invalidate();
    }

    @Override
    public Option getValue() {
        validate(rawValue);
        return rawValue;
    }

    @Override
    protected void showEditDialog() {
        new SingleOptionsSettingsDialogBuilder()
                .withOptions(this.options)
                .withContext(getContext())
                .withLabel(labelField.getText().toString())
                .withOldValue(getValue())
                .withOnEditFinishedListener(new OnEditFinishedListener<Option>() {
                    @Override
                    public void onEditFinished(Option oldValue, Option newValue) {
                        notifyEditFinished(oldValue, newValue);
                    }
                }).show();
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
