package de.iweinzierl.easyprofiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import com.google.common.base.Strings;
import com.orm.SugarRecord;

import java.util.List;

import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;
import de.iweinzierl.easyprofiles.fragments.WifiTriggerTabFragment;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.util.WifiManagerHelper;

public class WifiTriggerActivity extends Activity implements WifiTriggerTabFragment.Callback {

    private WifiTriggerTabFragment wifiTriggerTabFragment;
    private WifiBasedTrigger wifiTrigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_trigger);

        wifiTriggerTabFragment = new WifiTriggerTabFragment();
        wifiTrigger = new WifiBasedTrigger();
        wifiTrigger.setEnabled(true);

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        toolbarBottom.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
        toolbarBottom.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content, wifiTriggerTabFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWifiList();
        updateProfileList();
    }

    @Override
    public void onWifiSelected(String ssid) {
        wifiTrigger.setSsid(ssid);
    }

    @Override
    public void onConnectProfileSelected(Profile profile) {
        wifiTrigger.setOnActivateProfile(profile);
    }

    @Override
    public void onDisconnectProfileSelected(Profile profile) {
        wifiTrigger.setOnDeactivateProfile(profile);
    }

    private void cancel() {
        finish();
    }

    private void save() {
        if (wifiTrigger.getOnActivateProfile() != null && !Strings.isNullOrEmpty(wifiTrigger.getSsid())) {
            SugarRecord.save(wifiTrigger.export());
            finish();
        } else {
            Log.w("easyprofiles", "Incomplete configuration for wifi based trigger");
            new AlertDialog.Builder(this)
                    .setTitle(R.string.activity_wifi_trigger_incomplete_error_title)
                    .setMessage(R.string.activity_wifi_trigger_incomplete_error_message)
                    .create()
                    .show();
        }
    }

    private void updateWifiList() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiManagerHelper wifiManagerHelper = new WifiManagerHelper(wifiManager);

        List<String> ssids = wifiManagerHelper.listSSIDs();
        Log.d("easyprofiles", "Found " + ssids.size() + " Wifi SSIDs");

        wifiTriggerTabFragment.setWifiSSIDs(ssids);
    }

    private void updateProfileList() {
        List<Profile> profiles = SugarRecord.listAll(Profile.class);
        Log.d("easyprofiles", "Found " + profiles.size() + " Profiles");

        wifiTriggerTabFragment.setProfiles(profiles);
    }
}
