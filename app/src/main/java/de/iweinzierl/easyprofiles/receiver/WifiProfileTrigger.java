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
import de.iweinzierl.easyprofiles.persistence.Trigger;
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

        Trigger trigger = findTrigger(ssid);
        if (trigger != null) {
            Log.d("easyprofiles", "Found Wifi trigger: " + trigger);

            Profile profile = Profile.findById(Profile.class, trigger.getProfileId());
            if (profile != null) {
                profile.activate(context);
            } else {
                Log.w("easyprofiles", "Could not find profile for trigger: " + trigger);
            }
        }
    }

    private Trigger findTrigger(String ssid) {
        List<Trigger> triggers = Trigger.find(
                Trigger.class, "type = ? and data = ?",
                TriggerType.WIFI.name(), ssid);

        if (triggers != null && !triggers.isEmpty()) {
            if (triggers.size() > 1) {
                Log.w("easyprofiles", "Found more than one Wifi trigger for ssid '" + ssid + "': " + triggers);
            }

            return triggers.get(0);
        }

        return null;
    }
}
