package de.iweinzierl.easyprofiles.widget;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;

public class SingleOptionsSettingsDialogBuilder extends AbstractSettingsDialogBuilder<SettingsViewSingleOptions.Option> {

    private List<SettingsViewSingleOptions.Option> options;
    private int selected;

    @Override
    protected View createContent() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.dialog_settings_view_singleoptions, null);

        RadioGroup radioGroup = (RadioGroup) content.findViewById(R.id.options_group);
        int idx = 0;
        for (SettingsViewSingleOptions.Option option : options) {
            final int cur = idx++;
            RadioButton rb = new RadioButton(new ContextThemeWrapper(context, R.style.AppTheme_SingleOptionsSettingsDialog_Value));
            rb.setId(cur);
            rb.setText(option.label);
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected = cur;
                }
            });

            radioGroup.addView(rb);
        }

        final TextView titleField = (TextView) content.findViewById(R.id.title);
        final TextView labelField = (TextView) content.findViewById(R.id.label);

        titleField.setText(R.string.edittextdialog_title);
        labelField.setText(getLabel());

        return content;
    }

    @Override
    protected SettingsViewSingleOptions.Option getNewValue() {
        return options.get(selected);
    }

    public SingleOptionsSettingsDialogBuilder withOptions(List<SettingsViewSingleOptions.Option> options) {
        this.options = options;
        return this;
    }
}
