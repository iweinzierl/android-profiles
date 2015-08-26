package de.iweinzierl.easyprofiles.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import de.iweinzierl.easyprofiles.R;

public abstract class AbstractSettingsDialogBuilder<T> {

    protected Context context;

    protected SettingsViewEditText.OnEditFinishedListener<T> onEditFinishedListener;

    protected String label;
    protected T oldValue;

    protected abstract View createContent();

    protected abstract T getNewValue();

    public AbstractSettingsDialogBuilder<T> withContext(Context context) {
        this.context = context;
        return this;
    }

    public AbstractSettingsDialogBuilder<T> withOnEditFinishedListener(SettingsViewEditText.OnEditFinishedListener<T> onEditFinished) {
        this.onEditFinishedListener = onEditFinished;
        return this;
    }

    public AbstractSettingsDialogBuilder<T> withLabel(String label) {
        this.label = label;
        return this;
    }

    public AbstractSettingsDialogBuilder<T> withOldValue(T value) {
        this.oldValue = value;
        return this;
    }

    public void show() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView = createContent();

        builder.setView(dialogView);
        builder.setCancelable(false);

        final Button finish = (Button) dialogView.findViewById(R.id.ok);
        final Button cancel = (Button) dialogView.findViewById(R.id.cancel);

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

                if (onEditFinishedListener != null) {
                    onEditFinishedListener.onEditFinished(getOldValue(), getNewValue());
                }
            }
        });

        alertDialog.show();
    }

    protected String getLabel() {
        return label;
    }

    protected T getOldValue() {
        return oldValue;
    }
}
