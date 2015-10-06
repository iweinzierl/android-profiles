package de.iweinzierl.easyprofiles.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.joda.time.LocalTime;

import java.util.Collections;
import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.ProfileSpinnerAdapter;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileSchedulerFragment extends Fragment {

    private List<Profile> availableProfiles = Collections.emptyList();

    private TimePicker timePicker;
    private Spinner profileSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_scheduler, container, false);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        profileSpinner = (Spinner) view.findViewById(R.id.profileSpinner);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        profileSpinner.setAdapter(new ProfileSpinnerAdapter(getActivity(), availableProfiles));
    }

    public void setAvailableProfiles(List<Profile> profiles) {
        this.availableProfiles = profiles;
    }

    public LocalTime getTime() {
        return new LocalTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
    }

    public Profile getProfile() {
        return (Profile) profileSpinner.getSelectedItem();
    }
}
