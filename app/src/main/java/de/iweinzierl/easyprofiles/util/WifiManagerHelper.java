package de.iweinzierl.easyprofiles.util;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class WifiManagerHelper {

    private final WifiManager wifiManager;

    public WifiManagerHelper(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    public List<String> listSSIDs() {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();

        if (configuredNetworks == null) {
            return Collections.emptyList();
        }

        return Lists.transform(configuredNetworks, new Function<WifiConfiguration, String>() {
            @Override
            public String apply(WifiConfiguration input) {
                return input.SSID;
            }
        });
    }

    public String getConnectedWifiSSID() {
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        return connectionInfo == null ? null : connectionInfo.getSSID();
    }
}
