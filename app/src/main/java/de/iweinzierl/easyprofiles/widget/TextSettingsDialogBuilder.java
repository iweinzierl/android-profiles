package de.iweinzierl.easyprofiles.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.iweinzierl.easyprofiles.R;

public class TextSettingsDialogBuilder extends AbstractSettingsDialogBuilder<String> {

    private EditText valueField;

    protected View createContent() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_settings_view_edit_text, null);

        final TextView titleField = (TextView) dialogView.findViewById(R.id.title);
        final TextView labelField = (TextView) dialogView.findViewById(R.id.label);
        valueField = (EditText) dialogView.findViewById(R.id.value);

        titleField.setText(R.string.edittextdialog_title);
        labelField.setText(getLabel());
        valueField.setText(getOldValue());

        return dialogView;
    }

    protected String getNewValue() {
        return valueField.getText().toString();
    }
}
