package de.iweinzierl.easyprofiles.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import de.iweinzierl.easyprofiles.R;

public class SettingsViewEditText extends FrameLayout {

    public interface OnEditFinishedListener {
        void onEditFinished(String oldValue, String newValue);
    }

    private OnEditFinishedListener onEditFinishedListener;

    private TextView labelField;
    private TextView valueField;

    public SettingsViewEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_settings_view_edit_text, this);

        labelField = (TextView) findViewById(R.id.label);
        valueField = (TextView) findViewById(R.id.value);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SettingsView,
                0, 0);

        try {
            String label = a.getString(R.styleable.SettingsView_label);
            String value = a.getString(R.styleable.SettingsView_value);

            labelField.setText(label);
            valueField.setText(value);
        } finally {
            a.recycle();
        }

        registerOnStartEditing();
    }

    public void setOnEditListener(OnEditFinishedListener OnEditFinishedListener) {
        this.onEditFinishedListener = OnEditFinishedListener;
    }


    public void setLabel(String label) {
        labelField.setText(label);
        invalidate();
    }

    public void setValue(String value) {
        valueField.setText(value);
        invalidate();
    }

    public String getLabel() {
        return labelField.getText().toString();
    }

    public String getValue() {
        return valueField.getText().toString();
    }

    protected void registerOnStartEditing() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    protected void showEditDialog() {
        new EditTextDialogBuilder()
                .withContext(getContext())
                .withLabel(labelField.getText().toString())
                .withValue(valueField.getText().toString())
                .withOnEditFinishedListener(new OnEditFinishedListener() {
                    @Override
                    public void onEditFinished(String oldValue, String newValue) {
                        notifyEditFinished(oldValue, newValue);
                    }
                })
                .show();
    }

    protected void notifyEditFinished(String oldValue, String newValue) {
        valueField.setText(newValue);

        if (onEditFinishedListener != null) {
            onEditFinishedListener.onEditFinished(oldValue, newValue);
        }
    }

}
