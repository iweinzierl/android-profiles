package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileAdapter extends BaseAdapter {

    public interface ClickListener {
        void onNameClicked(Profile profile);

        void onModifyClicked(Profile profile);
    }

    private final Context context;
    private final ClickListener clickListener;
    private final List<Profile> profiles;

    public ProfileAdapter(Context context, ClickListener clickListener, List<Profile> profiles) {
        this.context = context;
        this.clickListener = clickListener;
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
        final Profile profile = (Profile) getItem(i);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = layoutInflater.inflate(R.layout.list_item_profile, null);

        TextView tv = (TextView) itemView.findViewById(R.id.name);
        tv.setText(profile.getName());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onNameClicked(profile);
            }
        });

        View modifyButton = itemView.findViewById(R.id.modify_button);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onModifyClicked(profile);
            }
        });

        return itemView;
    }
}
