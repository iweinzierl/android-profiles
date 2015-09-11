package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.Trigger;

public class TriggerAdapter extends ListAdapter<Trigger> {

    public TriggerAdapter(Context context, List<Trigger> items) {
        super(context, items);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Trigger trigger = (Trigger) getItem(i);
        final Profile profile = Profile.findById(Profile.class, trigger.getProfileId());

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = layoutInflater.inflate(R.layout.list_item_trigger, viewGroup, false);

        TextView triggerType = (TextView) view.findViewById(R.id.trigger_type);
        TextView triggerDetails = (TextView) view.findViewById(R.id.trigger_details);
        TextView profileName = (TextView) view.findViewById(R.id.profile);

        triggerType.setText(trigger.getType().name());
        triggerDetails.setText(trigger.getData());
        profileName.setText(profile.getName());

        return view;
    }
}
