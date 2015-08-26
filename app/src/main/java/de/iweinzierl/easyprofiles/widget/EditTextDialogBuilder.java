package de.iweinzierl.easyprofiles.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.iweinzierl.easyprofiles.R;

class EditTextDialogBuilder {
    private Context context;
    private SettingsViewEditText.OnEditFinishedListener onEditFinishedListener;
    private String label;
    private String value;

    public EditTextDialogBuilder withContext(Context context) {
        this.context = context;
        return this;
    }

    public EditTextDialogBuilder withOnEditFinishedListener(SettingsViewEditText.OnEditFinishedListener onEditFinished) {
        this.onEditFinishedListener = onEditFinished;
        return this;
    }

    public EditTextDialogBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public EditTextDialogBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public void show() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_settings_view_edit_text, null);

        builder.setView(dialogView);
        builder.setCancelable(false);

        final TextView titleField = (TextView) dialogView.findViewById(R.id.title);
        final TextView labelField = (TextView) dialogView.findViewById(R.id.label);
        final TextView valueField = (TextView) dialogView.findViewById(R.id.value);
        final Button finish = (Button) dialogView.findViewById(R.id.ok);
        final Button cancel = (Button) dialogView.findViewById(R.id.cancel);

        titleField.setText(R.string.edittextdialog_title);
        labelField.setText(this.label);
        valueField.setText(this.value);

        final AlertDialog alertDialog = builder.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                onEditFinishedListener.onEditFinished(value, valueField.getText().toString());
            }
        });

        alertDialog.show();
    }

}
