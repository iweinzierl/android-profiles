package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class TriggerTypeAdapter extends ListAdapter<TriggerType> {

    private static List<TriggerType> triggerTypes = Lists.newArrayList(
            TriggerType.WIFI,
            TriggerType.TIME_BASED);

    public TriggerTypeAdapter(Context context) {
        super(context, triggerTypes);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TriggerType triggerType = (TriggerType) getItem(i);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItem = inflater.inflate(R.layout.list_item_trigger_type, null, false);
        ImageView icon = (ImageView) listItem.findViewById(R.id.icon);
        TextView name = (TextView) listItem.findViewById(R.id.name);

        Resources.Theme theme = context.getTheme();
        Resources res = context.getResources();

        switch (triggerType) {
            case WIFI:
                icon.setImageDrawable(res.getDrawable(R.drawable.ic_network_wifi_black_36px, theme));
                name.setText("WIFI");
                break;
            case TIME_BASED:
                icon.setImageDrawable(res.getDrawable(R.drawable.time_schedule_36px, theme));
                name.setText("TIME BASED");
        }

        return listItem;
    }
}
