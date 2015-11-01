package de.iweinzierl.easyprofiles.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.ModifiableProfileAdapter;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileListFragment extends Fragment implements ModifiableProfileAdapter.ClickListener {

    public interface Callback {
        void onProfileClick(Profile profile);

        void onProfileModify(Profile profile);
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

        View emptyListView = view.findViewById(R.id.list_empty_view);

        profileList = (ListView) view.findViewById(R.id.profile_list);
        profileList.setEmptyView(emptyListView);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callback) {
            callback = (Callback) activity;
        }
    }

    @Override
    public void onNameClicked(Profile profile) {
        callback.onProfileClick(profile);
    }

    @Override
    public void onModifyClicked(Profile profile) {
        callback.onProfileModify(profile);
    }

    public void setProfiles(List<Profile> profiles, boolean modifiable) {
        profileList.setAdapter(new ModifiableProfileAdapter(getActivity(), this, profiles, modifiable));
    }
}
