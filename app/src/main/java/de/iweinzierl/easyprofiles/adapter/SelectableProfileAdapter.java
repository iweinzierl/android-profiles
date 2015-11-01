package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class SelectableProfileAdapter extends ListAdapter<Profile> {

    public SelectableProfileAdapter(Context context, List<Profile> profiles) {
        super(context, profiles);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Profile profile = (Profile) getItem(i);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = layoutInflater.inflate(getLayoutId(), null);

        TextView tv = (TextView) itemView.findViewById(R.id.name);
        tv.setText(profile.getName());

        return itemView;
    }

    private int getLayoutId() {
        return R.layout.list_item_profile_selectable;
    }
}
