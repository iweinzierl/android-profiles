package de.iweinzierl.easyprofiles.persistence;

import com.google.common.base.MoreObjects;
import com.orm.SugarRecord;

public class WifiSettings extends SugarRecord {

    private boolean wifiEnabled;
    private boolean wifiHotspotEnabled;

    public WifiSettings() {
        this.wifiEnabled = true;
        this.wifiHotspotEnabled = false;
    }

    public WifiSettings(boolean wifiEnabled, boolean wifiHotspotEnabled) {
        this.wifiEnabled = wifiEnabled;
        this.wifiHotspotEnabled = wifiHotspotEnabled;
    }

    public boolean isWifiEnabled() {
        return wifiEnabled;
    }

    public void setWifiEnabled(boolean wifiEnabled) {
        this.wifiEnabled = wifiEnabled;
    }

    public boolean isWifiHotspotEnabled() {
        return wifiHotspotEnabled;
    }

    public void setWifiHotspotEnabled(boolean wifiHotspotEnabled) {
        this.wifiHotspotEnabled = wifiHotspotEnabled;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("wifiEnabled", wifiEnabled)
                .add("wifiHotspotEnabled", wifiHotspotEnabled)
                .toString();
    }
}
