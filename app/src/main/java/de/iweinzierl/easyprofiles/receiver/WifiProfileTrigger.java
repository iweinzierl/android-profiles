package de.iweinzierl.easyprofiles.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;
import de.iweinzierl.easyprofiles.util.WifiManagerHelper;

public class WifiProfileTrigger extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            onWifiConnected(context, new WifiManagerHelper(wifiManager).getConnectedWifiSSID());
        }
    }

    private void onWifiConnected(Context context, String ssid) {
        Log.d("easyprofiles", "Received WiFi connection broadcast: " + ssid);

        PersistentTrigger persistentTrigger = findTrigger(ssid);
        if (persistentTrigger != null) {
            Log.d("easyprofiles", "Found Wifi trigger: " + persistentTrigger);

            Profile profile = Profile.findById(Profile.class, persistentTrigger.getOnActivateProfileId());
            if (profile != null) {
                profile.activate(context);
            } else {
                Log.w("easyprofiles", "Could not find profile for trigger: " + persistentTrigger);
            }
        }
    }

    private PersistentTrigger findTrigger(String ssid) {
        List<PersistentTrigger> persistentTriggers = PersistentTrigger.find(
                PersistentTrigger.class, "type = ? and data = ? and enabled = 1",
                TriggerType.WIFI.name(), ssid);

        if (persistentTriggers != null && !persistentTriggers.isEmpty()) {
            if (persistentTriggers.size() > 1) {
                Log.w("easyprofiles", "Found more than one Wifi trigger for ssid '" + ssid + "': " + persistentTriggers);
            }

            return persistentTriggers.get(0);
        }

        return null;
    }
}
