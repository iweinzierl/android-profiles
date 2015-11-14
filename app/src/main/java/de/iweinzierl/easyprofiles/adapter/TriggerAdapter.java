package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Set;

import de.inselhome.android.utils.UiUtils;
import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.domain.Day;
import de.iweinzierl.easyprofiles.domain.LocationBasedTrigger;
import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;
import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;

public class TriggerAdapter extends ListAdapter<PersistentTrigger> {

    public interface Callback {
        void onTriggerEnabled(PersistentTrigger persistentTrigger);

        void onTriggerDisabled(PersistentTrigger persistentTrigger);
    }

    private Callback callback;

    public TriggerAdapter(Context context, List<PersistentTrigger> items, Callback callback) {
        super(context, items);
        this.callback = callback;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final PersistentTrigger persistentTrigger = (PersistentTrigger) getItem(i);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (persistentTrigger.getType()) {
            case WIFI:
                return buildWifiView(persistentTrigger, layoutInflater, viewGroup);
            case TIME_BASED:
                return buildTimeBasedView(persistentTrigger, layoutInflater, viewGroup);
            case LOCATION_BASED:
                return buildLocationBasedView(persistentTrigger, layoutInflater, viewGroup);
            default:
                return new View(context);
        }
    }

    private View buildTimeBasedView(final PersistentTrigger persistentTrigger, final LayoutInflater layoutInflater, final ViewGroup viewGroup) {
        TimeBasedTrigger trigger = new TimeBasedTrigger();
        trigger.apply(persistentTrigger);

        View view = layoutInflater.inflate(R.layout.list_item_time_based_trigger, viewGroup, false);

        Switch enabled = (Switch) view.findViewById(R.id.enabled);
        TextView timeActivation = (TextView) view.findViewById(R.id.time_activation);
        TextView timeDeactivation = (TextView) view.findViewById(R.id.time_deactivation);
        TextView profileActivation = (TextView) view.findViewById(R.id.profile_activation);
        TextView profileDeactivation = (TextView) view.findViewById(R.id.profile_deactivation);

        enabled.setChecked(persistentTrigger.isEnabled());
        timeActivation.setText(trigger.getActivationTime().toString("HH:mm"));
        profileActivation.setText(trigger.getOnActivateProfile().getName());

        if (trigger.getDeactivationTime() != null && trigger.getOnDeactivateProfile() != null) {
            timeDeactivation.setText(trigger.getDeactivationTime().toString("HH:mm"));
            profileDeactivation.setText(trigger.getOnDeactivateProfile().getName());
        }

        enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    callback.onTriggerEnabled(persistentTrigger);
                } else {
                    callback.onTriggerDisabled(persistentTrigger);
                }
            }
        });

        markDayIfActivatedForRepeat(trigger.getRepeatOnDays(), Day.MONDAY, (TextView) view.findViewById(R.id.monday));
        markDayIfActivatedForRepeat(trigger.getRepeatOnDays(), Day.TUESDAY, (TextView) view.findViewById(R.id.tuesday));
        markDayIfActivatedForRepeat(trigger.getRepeatOnDays(), Day.WEDNESDAY, (TextView) view.findViewById(R.id.wednesday));
        markDayIfActivatedForRepeat(trigger.getRepeatOnDays(), Day.THURSDAY, (TextView) view.findViewById(R.id.thursday));
        markDayIfActivatedForRepeat(trigger.getRepeatOnDays(), Day.FRIDAY, (TextView) view.findViewById(R.id.friday));
        markDayIfActivatedForRepeat(trigger.getRepeatOnDays(), Day.SATURDAY, (TextView) view.findViewById(R.id.saturday));
        markDayIfActivatedForRepeat(trigger.getRepeatOnDays(), Day.SUNDAY, (TextView) view.findViewById(R.id.sunday));

        return view;
    }

    private void markDayIfActivatedForRepeat(Set<Day> repeatDays, Day day, TextView dayView) {
        if (repeatDays != null && repeatDays.contains(day)) {
            Resources resources = context.getResources();
            dayView.setTextColor(resources.getColor(R.color.ColorPrimary));
            dayView.setTypeface(null, Typeface.BOLD);
        }
    }

    private View buildWifiView(final PersistentTrigger persistentTrigger, final LayoutInflater layoutInflater, final ViewGroup viewGroup) {
        WifiBasedTrigger wifiTrigger = new WifiBasedTrigger();
        wifiTrigger.apply(persistentTrigger);

        View view = layoutInflater.inflate(R.layout.list_item_wifi_based_trigger, viewGroup, false);

        TextView wifi = (TextView) view.findViewById(R.id.wifi);
        TextView profileActivation = (TextView) view.findViewById(R.id.profile_activation);
        TextView profileDeactivation = (TextView) view.findViewById(R.id.profile_deactivation);
        Switch enabled = (Switch) view.findViewById(R.id.enabled);

        wifi.setText(wifiTrigger.getSsid().replaceAll("\"", ""));
        profileActivation.setText(wifiTrigger.getOnActivateProfile().getName());
        if (wifiTrigger.getOnDeactivateProfile() == null) {
            profileDeactivation.setText("");
        } else {
            profileDeactivation.setText(wifiTrigger.getOnDeactivateProfile().getName());
        }
        enabled.setChecked(persistentTrigger.isEnabled());

        enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    callback.onTriggerEnabled(persistentTrigger);
                } else {
                    callback.onTriggerDisabled(persistentTrigger);
                }
            }
        });

        return view;
    }

    private View buildLocationBasedView(final PersistentTrigger persistentTrigger, LayoutInflater layoutInflater, ViewGroup viewGroup) {
        LocationBasedTrigger trigger = new LocationBasedTrigger();
        trigger.apply(persistentTrigger);

        View view = layoutInflater.inflate(R.layout.list_item_location_based_trigger, viewGroup, false);

        TextView location = UiUtils.getGeneric(TextView.class, view, R.id.location);
        TextView profileActivation = (TextView) view.findViewById(R.id.profile_activation);
        TextView profileDeactivation = (TextView) view.findViewById(R.id.profile_deactivation);
        Switch enabled = (Switch) view.findViewById(R.id.enabled);

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);
        location.setText(nf.format(trigger.getLat()) + " / " + nf.format(trigger.getLon()));

        profileActivation.setText(trigger.getOnActivateProfile().getName());
        if (trigger.getOnDeactivateProfile() == null) {
            profileDeactivation.setText("");
        } else {
            profileDeactivation.setText(trigger.getOnDeactivateProfile().getName());
        }

        enabled.setChecked(persistentTrigger.isEnabled());
        enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    callback.onTriggerEnabled(persistentTrigger);
                } else {
                    callback.onTriggerDisabled(persistentTrigger);
                }
            }
        });

        return view;
    }

}
