package de.iweinzierl.easyprofiles.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;
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

            Profile profile = SugarRecord.findById(Profile.class, persistentTrigger.getOnActivateProfileId());
            if (profile != null) {
                profile.activate(context);
            } else {
                Log.w("easyprofiles", "Could not find profile for trigger: " + persistentTrigger);
            }
        }
    }

    private PersistentTrigger findTrigger(String ssid) {
        List<PersistentTrigger> persistentTriggerList = SugarRecord.find(
                PersistentTrigger.class,
                "enabled = 1 and type = ?",
                TriggerType.WIFI.name());

        Log.d("easyprofiles", "Found " + persistentTriggerList.size() + " Wifi triggers");

        for (PersistentTrigger trigger : persistentTriggerList) {
            if (trigger.getType() == TriggerType.WIFI) {
                Log.d("easyprofiles", "Test wifi trigger: " + trigger);

                WifiBasedTrigger wifiTrigger = new WifiBasedTrigger();
                wifiTrigger.apply(trigger);

                String triggerSsid = wifiTrigger.getSsid().replaceAll("\"", "");
                String testSsid = ssid.replaceAll("\"", "");

                if (testSsid.equals(triggerSsid)) {
                    Log.i("easyprofiles", "Trigger matched.");
                    return trigger;
                } else {
                    Log.d("easyprofiles", "Trigger did not match.");
                }
            }
        }

        return null;
    }
}
