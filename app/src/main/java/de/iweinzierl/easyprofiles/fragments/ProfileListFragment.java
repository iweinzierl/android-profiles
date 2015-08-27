package de.iweinzierl.easyprofiles.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import de.iweinzierl.easyprofiles.EditProfileActivity;
import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.ProfileAdapter;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileListFragment extends Fragment {

    public interface Callback {
        void onProfileClick(Profile profile);
    }

    private Callback callback;
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
        profileList.setOnItemClickListener(new OnActivateProfileClickListener(callback));

        ImageButton addProfileButton = (ImageButton) view.findViewById(R.id.addProfileButton);
        addProfileButton.setOnClickListener(new OnAddProfileClickListener(getActivity()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callback) {
            callback = (Callback) activity;
        }
    }

    public void setProfiles(List<Profile> profiles) {
        profileList.setAdapter(new ProfileAdapter(getActivity(), profiles));
    }

    private static class OnActivateProfileClickListener implements ListView.OnItemClickListener {

        private Callback callback;

        public OnActivateProfileClickListener(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Profile item = (Profile) adapterView.getAdapter().getItem(i);
            if (callback != null) {
                callback.onProfileClick(item);
            } else {
                Log.w("easyprofiles", "No Callback set for ProfileListFragment! Cannot switch Profiles!");
            }
        }
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
