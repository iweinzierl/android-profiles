package de.iweinzierl.easyprofiles.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import de.iweinzierl.easyprofiles.R;

public class CheckboxSettingsDialogBuilder extends AbstractSettingsDialogBuilder<Boolean> {

    private CheckBox valueField;

    @Override
    protected View createContent() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.dialog_settings_view_checkbox, null);

        final TextView titleField = (TextView) content.findViewById(R.id.title);
        final TextView labelField = (TextView) content.findViewById(R.id.label);
        valueField = (CheckBox) content.findViewById(R.id.value);

        titleField.setText(R.string.edittextdialog_title);
        labelField.setText(getLabel());
        valueField.setChecked(getOldValue());

        return content;
    }

    @Override
    protected Boolean getNewValue() {
        return valueField.isChecked();
    }
}
