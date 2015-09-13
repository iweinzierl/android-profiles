package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileAdapter extends ListAdapter<Profile> {

    public interface ClickListener {
        void onNameClicked(Profile profile);

        void onModifyClicked(Profile profile);
    }

    private final ClickListener clickListener;
    private final boolean modifiable;

    public ProfileAdapter(Context context, ClickListener clickListener, List<Profile> profiles, boolean modifiable) {
        super(context, profiles);
        this.clickListener = clickListener;
        this.modifiable = modifiable;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Profile profile = (Profile) getItem(i);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = layoutInflater.inflate(getLayoutId(), null);

        TextView tv = (TextView) itemView.findViewById(R.id.name);
        tv.setText(profile.getName());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onNameClicked(profile);
            }
        });

        View modifyButton = itemView.findViewById(R.id.modify_button);
        if (modifyButton != null) {
            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onModifyClicked(profile);
                }
            });
        }

        return itemView;
    }

    private int getLayoutId() {
        return modifiable ? R.layout.list_item_profile_modifiable : R.layout.list_item_profile;
    }
}
