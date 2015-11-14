package de.iweinzierl.easyprofiles.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.orm.SugarRecord;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class LocationTriggerTabFragment extends Fragment implements TabHost.OnTabChangeListener {

    private static final String TAB_MAP = "location";
    private static final String TAB_ENTER = "enter";
    private static final String TAB_QUIT = "quit";

    private static final String SAVED_ENTER_PROFILE_ID = "save.enter.profile.id";
    private static final String SAVED_QUIT_PROFILE_ID = "save.quit.profile.id";

    public interface Callback {
        void onLocationSelected(double lat, double lon, int radius);

        void onEnterProfileSelected(Profile profile);

        void onExitProfileSelected(Profile profile);
    }

    private Callback callback;

    private PickLocationFragment pickLocationFragment;
    private SelectableProfileListFragment enterProfileFragment;
    private SelectableProfileListFragment quitProfileFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs_location_scheduler, container, false);

        pickLocationFragment = new PickLocationFragment();
        enterProfileFragment = new SelectableProfileListFragment();
        quitProfileFragment = new SelectableProfileListFragment();

        pickLocationFragment.setCallback(new PickLocationFragment.Callback() {
            @Override
            public void onLocationSelected(double lat, double lon, int radius) {
                if (callback != null) {
                    callback.onLocationSelected(lat, lon, radius);
                }
            }
        });

        enterProfileFragment.setCallback(new SelectableProfileListFragment.Callback() {
            @Override
            public void onProfileSelected(Profile profile) {
                if (callback != null) {
                    callback.onEnterProfileSelected(profile);
                }
            }
        });

        quitProfileFragment.setCallback(new SelectableProfileListFragment.Callback() {
            @Override
            public void onProfileSelected(Profile profile) {
                if (callback != null) {
                    callback.onExitProfileSelected(profile);
                }
            }
        });

        TabHost tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabHost.setOnTabChangedListener(this);

        TabHost.TabSpec activation = tabHost.newTabSpec(TAB_MAP);
        activation.setIndicator("location");
        activation.setContent(R.id.location);

        TabHost.TabSpec deactivation = tabHost.newTabSpec(TAB_ENTER);
        deactivation.setIndicator("enter");
        deactivation.setContent(R.id.enter);

        TabHost.TabSpec repeat = tabHost.newTabSpec(TAB_QUIT);
        repeat.setIndicator("quit");
        repeat.setContent(R.id.quit);

        tabHost.addTab(activation);
        tabHost.addTab(deactivation);
        tabHost.addTab(repeat);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof Callback) {
            callback = (Callback) activity;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (enterProfileFragment != null && outState != null) {
            saveEnterProfileFragmentState(outState);
        }

        if (quitProfileFragment != null && outState != null) {
            saveQuitProfileFragment(outState);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            restoreConnectProfileFragmentState(savedInstanceState);
            restoreQuitProfileFragmentState(savedInstanceState);
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (tabId) {
            case TAB_MAP:
                if (fragmentManager.findFragmentByTag(TAB_MAP) != null) {
                    fragmentTransaction.show(pickLocationFragment).commit();
                } else {
                    fragmentTransaction.replace(R.id.location, pickLocationFragment, TAB_MAP).commit();
                }
                break;
            case TAB_ENTER:
                if (fragmentManager.findFragmentByTag(TAB_ENTER) != null) {
                    fragmentTransaction.show(enterProfileFragment).commit();
                } else {
                    fragmentTransaction.replace(R.id.enter, enterProfileFragment, TAB_ENTER).commit();
                }
                break;
            case TAB_QUIT:
                if (fragmentManager.findFragmentByTag(TAB_QUIT) != null) {
                    fragmentTransaction.show(quitProfileFragment).commit();
                } else {
                    fragmentTransaction.replace(R.id.quit, quitProfileFragment, TAB_QUIT).commit();
                }
                break;
        }
    }

    public void setProfiles(List<Profile> profiles) {
        enterProfileFragment.setProfiles(profiles);
        quitProfileFragment.setProfiles(profiles);
    }

    private void saveEnterProfileFragmentState(Bundle outState) {
        Long profileId = enterProfileFragment.getSelectedProfile().getId();
        outState.putLong(SAVED_ENTER_PROFILE_ID, profileId);
    }

    private void saveQuitProfileFragment(Bundle outState) {
        Long profileId = quitProfileFragment.getSelectedProfile().getId();
        outState.putLong(SAVED_QUIT_PROFILE_ID, profileId);
    }

    private void restoreConnectProfileFragmentState(Bundle savedInstanceState) {
        long profileId = savedInstanceState.getLong(SAVED_ENTER_PROFILE_ID, -1);
        if (profileId > -1) {
            Profile profile = SugarRecord.findById(Profile.class, profileId);
            enterProfileFragment.setSelectedProfile(profile);
        }
    }

    private void restoreQuitProfileFragmentState(Bundle savedInstanceState) {
        long profileId = savedInstanceState.getLong(SAVED_QUIT_PROFILE_ID, -1);
        if (profileId > -1) {
            Profile profile = SugarRecord.findById(Profile.class, profileId);
            quitProfileFragment.setSelectedProfile(profile);
        }
    }
}
