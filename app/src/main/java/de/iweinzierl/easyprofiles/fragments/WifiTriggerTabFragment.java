package de.iweinzierl.easyprofiles.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class WifiTriggerTabFragment extends Fragment implements TabHost.OnTabChangeListener {

    public interface Callback {
        void onWifiSelected(String ssid);

        void onConnectProfileSelected(Profile profile);

        void onDisconnectProfileSelected(Profile profile);
    }

    private static final String TAB_WIFILIST = "Wifi";
    private static final String TAB_CONNECT = "Connect";
    private static final String TAB_DISCONNECT = "Disconnect";

    private static final String SAVED_CONNECT_PROFILE_ID = "save.connect.profile.id";
    private static final String SAVED_DISCONNECT_PROFILE_ID = "save.disconnect.profile.id";

    private Callback callback;

    private WifiListFragment wifiListFragment;
    private SelectableProfileListFragment connectProfileFragment;
    private SelectableProfileListFragment disconnectProfileFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs_wifi_scheduler, container, false);

        wifiListFragment = new WifiListFragment();
        connectProfileFragment = new SelectableProfileListFragment();
        disconnectProfileFragment = new SelectableProfileListFragment();

        wifiListFragment.setCallback(new WifiListFragment.Callback() {
            @Override
            public void onWifiSelected(String ssid) {
                if (callback != null) {
                    callback.onWifiSelected(ssid);
                }
            }
        });

        connectProfileFragment.setCallback(new SelectableProfileListFragment.Callback() {
            @Override
            public void onProfileSelected(Profile profile) {
                if (callback != null) {
                    callback.onConnectProfileSelected(profile);
                }
            }
        });

        disconnectProfileFragment.setCallback(new SelectableProfileListFragment.Callback() {
            @Override
            public void onProfileSelected(Profile profile) {
                if (callback != null) {
                    callback.onDisconnectProfileSelected(profile);
                }
            }
        });

        TabHost tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabHost.setOnTabChangedListener(this);

        TabHost.TabSpec activation = tabHost.newTabSpec(TAB_WIFILIST);
        activation.setIndicator("Wifi");
        activation.setContent(R.id.wifi);

        TabHost.TabSpec deactivation = tabHost.newTabSpec(TAB_CONNECT);
        deactivation.setIndicator("Connect");
        deactivation.setContent(R.id.connect);

        TabHost.TabSpec repeat = tabHost.newTabSpec(TAB_DISCONNECT);
        repeat.setIndicator("Disconnect");
        repeat.setContent(R.id.disconnect);

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
    public void onTabChanged(String tabId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (tabId) {
            case TAB_WIFILIST:
                if (fragmentManager.findFragmentByTag(TAB_WIFILIST) != null) {
                    fragmentTransaction.show(wifiListFragment).commit();
                } else {
                    fragmentTransaction.replace(R.id.wifi, wifiListFragment, TAB_WIFILIST).commit();
                }
                break;
            case TAB_CONNECT:
                if (fragmentManager.findFragmentByTag(TAB_CONNECT) != null) {
                    fragmentTransaction.show(connectProfileFragment).commit();
                } else {
                    fragmentTransaction.replace(R.id.connect, connectProfileFragment, TAB_CONNECT).commit();
                }
                break;
            case TAB_DISCONNECT:
                if (fragmentManager.findFragmentByTag(TAB_DISCONNECT) != null) {
                    fragmentTransaction.show(disconnectProfileFragment).commit();
                } else {
                    fragmentTransaction.replace(R.id.disconnect, disconnectProfileFragment, TAB_DISCONNECT).commit();
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (connectProfileFragment != null && outState != null) {
            saveConnectProfileFragmentState(outState);
        }

        if (disconnectProfileFragment != null && outState != null) {
            saveDisconnectProfileFragment(outState);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            restoreConnectProfileFragmentState(savedInstanceState);
            restoreDisconnectProfileFragmentState(savedInstanceState);
        }
    }

    public void setWifiSSIDs(List<String> ssids) {
        wifiListFragment.setWifiSSIDs(ssids);
    }

    public void setProfiles(List<Profile> profiles) {
        connectProfileFragment.setProfiles(profiles);
        disconnectProfileFragment.setProfiles(profiles);
    }

    private void saveConnectProfileFragmentState(Bundle outState) {
        Long profileId = connectProfileFragment.getSelectedProfile().getId();
        outState.putLong(SAVED_CONNECT_PROFILE_ID, profileId);
    }

    private void saveDisconnectProfileFragment(Bundle outState) {
        Long profileId = disconnectProfileFragment.getSelectedProfile().getId();
        outState.putLong(SAVED_DISCONNECT_PROFILE_ID, profileId);
    }

    private void restoreConnectProfileFragmentState(Bundle savedInstanceState) {
        long profileId = savedInstanceState.getLong(SAVED_CONNECT_PROFILE_ID, -1);
        if (profileId > -1) {
            Profile profile = SugarRecord.findById(Profile.class, profileId);
            connectProfileFragment.setSelectedProfile(profile);
        }
    }

    private void restoreDisconnectProfileFragmentState(Bundle savedInstanceState) {
        long profileId = savedInstanceState.getLong(SAVED_DISCONNECT_PROFILE_ID, -1);
        if (profileId > -1) {
            Profile profile = SugarRecord.findById(Profile.class, profileId);
            disconnectProfileFragment.setSelectedProfile(profile);
        }
    }
}
