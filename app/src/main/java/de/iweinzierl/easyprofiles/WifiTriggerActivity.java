package de.iweinzierl.easyprofiles;

import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toolbar;

import com.google.common.base.Strings;
import com.orm.SugarRecord;

import org.slf4j.Logger;

import java.util.List;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;
import de.iweinzierl.easyprofiles.fragments.WifiTriggerTabFragment;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.util.wifi.WifiManagerHelper;

public class WifiTriggerActivity extends FragmentActivity implements WifiTriggerTabFragment.Callback {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(WifiTriggerActivity.class.getName());

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

        getSupportFragmentManager()
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
            LOG.warn("Incomplete configuration for wifi based trigger");
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
        LOG.debug("Found {} Wifi SSIDs", ssids.size());

        wifiTriggerTabFragment.setWifiSSIDs(ssids);
    }

    private void updateProfileList() {
        List<Profile> profiles = SugarRecord.listAll(Profile.class);
        LOG.debug("Found {} Profiles", profiles.size());

        wifiTriggerTabFragment.setProfiles(profiles);
    }
}
