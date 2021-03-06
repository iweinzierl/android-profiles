package de.iweinzierl.easyprofiles.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import org.joda.time.LocalTime;

import java.util.List;
import java.util.Set;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.domain.Day;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileSchedulerTabsFragment extends Fragment implements TabHost.OnTabChangeListener {

    private ProfileSchedulerFragment activationFragment;
    private ProfileSchedulerFragment deactivationFragment;
    private SelectableWeekDayListFragment weekDayListFragment;

    private static final String TAB_ACTIVATION = "activation";
    private static final String TAB_DEACTIVATION = "deactivation";
    private static final String TAB_REPEAT = "repeat";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs_profile_scheduler, container, false);

        activationFragment = new ProfileSchedulerFragment();
        deactivationFragment = new ProfileSchedulerFragment();
        weekDayListFragment = new SelectableWeekDayListFragment();

        TabHost tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabHost.setOnTabChangedListener(this);

        TabHost.TabSpec activation = tabHost.newTabSpec(TAB_ACTIVATION);
        activation.setIndicator("Activation");
        activation.setContent(R.id.activation);

        TabHost.TabSpec deactivation = tabHost.newTabSpec(TAB_DEACTIVATION);
        deactivation.setIndicator("Deactivation");
        deactivation.setContent(R.id.deactivation);

        TabHost.TabSpec repeat = tabHost.newTabSpec(TAB_REPEAT);
        repeat.setIndicator("Repeat");
        repeat.setContent(R.id.repeat);

        tabHost.addTab(activation);
        tabHost.addTab(deactivation);
        tabHost.addTab(repeat);

        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        switch (tabId) {
            case TAB_ACTIVATION:
                getFragmentManager().beginTransaction().replace(R.id.activation, activationFragment).commit();
                break;
            case TAB_DEACTIVATION:
                getFragmentManager().beginTransaction().replace(R.id.deactivation, deactivationFragment).commit();
                break;
            case TAB_REPEAT:
                getFragmentManager().beginTransaction().replace(R.id.repeat, weekDayListFragment).commit();
                break;
        }
    }

    public void setAvailableProfiles(List<Profile> profiles) {
        activationFragment.setAvailableProfiles(profiles);
        deactivationFragment.setAvailableProfiles(profiles);
    }

    public LocalTime getActivationTime() {
        return activationFragment.getTime();
    }

    public LocalTime getDeactivationTime() {
        return deactivationFragment.getTime();
    }

    public Profile getActivationProfile() {
        return activationFragment.getProfile();
    }

    public Profile getDeactivationProfile() {
        return deactivationFragment.getProfile();
    }

    public Set<Day> getRepeatingDays() {
        return weekDayListFragment.getSelectedDays();
    }
}
