package de.iweinzierl.easyprofiles.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import de.iweinzierl.easyprofiles.EditProfileActivity;
import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.ProfileAdapter;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileListFragment extends Fragment {

    private ListView profileList;

    public ProfileListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileList = (ListView) view.findViewById(R.id.profile_list);

        ImageButton addProfileButton = (ImageButton) view.findViewById(R.id.addProfileButton);
        addProfileButton.setOnClickListener(new OnAddProfileClickListener(getActivity()));
    }

    public void setProfiles(List<Profile> profiles) {
        profileList.setAdapter(new ProfileAdapter(getActivity(), profiles));
    }

    private static class OnAddProfileClickListener implements View.OnClickListener {

        private Context context;

        public OnAddProfileClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Log.d("easyprofiles", "Clicked to add new profile");
            context.startActivity(new Intent(context, EditProfileActivity.class));
        }
    }
}
