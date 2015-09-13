package de.iweinzierl.easyprofiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import java.util.List;

import de.iweinzierl.easyprofiles.fragments.WifiListFragment;
import de.iweinzierl.easyprofiles.util.WifiManagerHelper;

public class WifiSelectionListActivity extends Activity implements WifiListFragment.Callback {

    public static final int REQUEST_WIFI_SSID = 100;

    public static final String EXTRA_WIFI_SSID = "extra.wifi.ssid";

    private WifiListFragment wifiListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        if (toolbar != null) {
            setActionBar(toolbar);
            toolbar.setTitle(R.string.activity_wifi_selection);
            setTitle(R.string.activity_wifi_selection);
        } else {
            setTitle(R.string.activity_wifi_selection);
        }

        wifiListFragment = new WifiListFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.wifi_list_fragment, wifiListFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWifiList();
    }

    @Override
    public void onWifiSelected(String ssid) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_WIFI_SSID, ssid);

        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateWifiList() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiManagerHelper wifiManagerHelper = new WifiManagerHelper(wifiManager);

        List<String> ssids = wifiManagerHelper.listSSIDs();
        Log.d("easyprofiles", "Found " + ssids.size() + " Wifi SSIDs");

        wifiListFragment.setWifiSSIDs(ssids);
    }
}
