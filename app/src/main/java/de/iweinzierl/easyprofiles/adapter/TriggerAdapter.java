package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;

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
        final Profile profile = Profile.findById(Profile.class, persistentTrigger.getOnActivateProfileId());

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = layoutInflater.inflate(R.layout.list_item_trigger, viewGroup, false);

        ImageView triggerType = (ImageView) view.findViewById(R.id.trigger_type);
        TextView triggerDetails = (TextView) view.findViewById(R.id.trigger_details);
        TextView profileName = (TextView) view.findViewById(R.id.profile);
        Switch enabled = (Switch) view.findViewById(R.id.enabled);

        Resources resources = context.getResources();
        Resources.Theme theme = context.getTheme();
        switch (persistentTrigger.getType()) {
            case WIFI:
                triggerType.setImageDrawable(resources.getDrawable(R.drawable.ic_network_wifi_black_48px, theme));
        }
        triggerDetails.setText(persistentTrigger.getData());
        profileName.setText(profile.getName());
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
