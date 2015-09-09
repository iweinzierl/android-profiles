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

        List<Trigger> triggers = Trigger.find(Trigger.class, "type=?", TriggerType.WIFI.name());
        if (triggers != null && triggers.size() > 0) {
            Log.d("easyprofiles", "Found Wifi trigger: " + triggers.get(0));

            Profile profile = Profile.findById(Profile.class, triggers.get(0).getProfileId());
            if (profile != null) {
                profile.activate(context);
            } else {
                Log.w("easyprofiles", "Could not find profile for trigger: " + triggers.get(0));
            }
        }
    }
}
