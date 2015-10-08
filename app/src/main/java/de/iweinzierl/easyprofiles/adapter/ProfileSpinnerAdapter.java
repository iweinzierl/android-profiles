package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileSpinnerAdapter implements SpinnerAdapter {

    private final Context context;
    private final List<Profile> profiles;

    public ProfileSpinnerAdapter(Context context, List<Profile> profiles) {
        this.context = context;
        this.profiles = profiles;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        final Profile profile = (Profile) getItem(i);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.spinner_item_dropdown_profile, null, false);

        TextView tv = (TextView) item.findViewById(R.id.name);
        tv.setText(profile.getName());

        return item;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // TODO
        final Profile profile = (Profile) getItem(i);

        TextView tv = new TextView(context);
        tv.setText(profile.getName());

        return tv;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return profiles == null || profiles.isEmpty();
    }
}
