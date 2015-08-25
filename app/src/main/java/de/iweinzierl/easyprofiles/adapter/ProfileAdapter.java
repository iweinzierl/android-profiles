package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileAdapter extends BaseAdapter {

    private final Context context;
    private final List<Profile> profiles;

    public ProfileAdapter(Context context, List<Profile> profiles) {
        this.context = context;
        this.profiles = profiles;
    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    @Override
    public Object getItem(int i) {
        return profiles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return profiles.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Profile profile = (Profile) getItem(i);

        TextView tv = new TextView(context);
        tv.setText(profile.getName());

        return tv;
    }
}
