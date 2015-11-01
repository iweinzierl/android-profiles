package de.iweinzierl.easyprofiles.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.SelectableProfileAdapter;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class SelectableProfileListFragment extends Fragment {

    public static final String SAVED_EXTRA_SELECTION_POSITION = "extra.saved.selection.position";

    public interface Callback {
        void onProfileSelected(Profile profile);
    }

    private Callback callback;
    private ListView profileList;
    private List<Profile> profiles;

    public SelectableProfileListFragment() {
        Log.d("easyprofiles", "Setup new instance");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_list, container, false);
        profileList = (ListView) view.findViewById(R.id.profile_list);
        profileList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        profileList.setSelector(R.color.ColorPrimary);
        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                profileList.setSelection(i);

                Profile profile = (Profile) adapterView.getAdapter().getItem(i);
                Log.d("easyprofiles", "Selected profile: " + profile.getName());

                callback.onProfileSelected(profile);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View emptyListView = view.findViewById(R.id.list_empty_view);
        profileList.setEmptyView(emptyListView);
    }

    @Override
    public void onStart() {
        super.onStart();
        profileList.setAdapter(new SelectableProfileAdapter(getActivity(), profiles));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null && profileList != null) {
            Log.d("easyprofiles", "Save state");
            outState.putInt(SAVED_EXTRA_SELECTION_POSITION, profileList.getSelectedItemPosition());
        } else {
            Log.d("easyprofiles", "Cannot save state");
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null && profileList != null) {
            Log.d("easyprofiles", "Restored saved state");
            profileList.setSelection(savedInstanceState.getInt(SAVED_EXTRA_SELECTION_POSITION));
        } else {
            Log.d("easyprofiles", "Cannot restore saved state");
        }
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public void setSelectedProfile(Profile profile) {
        if (profileList != null) {
            int pos = ((SelectableProfileAdapter) profileList.getAdapter()).getItemPosition(profile);
            profileList.setSelection(pos);
        }
    }

    public Profile getSelectedProfile() {
        return profileList == null ? null : (Profile) profileList.getSelectedItem();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
