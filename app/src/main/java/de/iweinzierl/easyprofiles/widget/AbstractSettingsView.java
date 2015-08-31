package de.iweinzierl.easyprofiles.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import de.iweinzierl.easyprofiles.R;

public abstract class AbstractSettingsView<T> extends FrameLayout {

    public interface OnEditFinishedListener<T> {
        void onEditFinished(T oldValue, T newValue);
    }

    protected OnEditFinishedListener<T> onEditFinishedListener;

    protected TextView labelField;
    protected TextView valueField;


    public AbstractSettingsView(Context context, AttributeSet attrs) {
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

    public abstract void setValue(T value);

    public abstract T getValue();

    protected abstract void showEditDialog();

    public void setLabel(String label) {
        labelField.setText(label);
        invalidate();
    }

    public String getLabel() {
        return labelField.getText().toString();
    }

    protected void registerOnStartEditing() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    protected void notifyEditFinished(T oldValue, T newValue) {
        setValue(newValue);

        if (onEditFinishedListener != null) {
            onEditFinishedListener.onEditFinished(oldValue, newValue);
        }
    }
}
