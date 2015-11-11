package de.iweinzierl.easyprofiles.util.wifi;

import android.content.Context;
import android.content.SharedPreferences;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;

public final class WifiStateHistory {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(WifiStateHistory.class.getName());

    public static class WifiState {
        private String ssid;
        private long connectionTime;
        private long disconnectTime;

        public WifiState(String ssid, long connectionTime, long disconnectTime) {
            this.ssid = ssid;
            this.connectionTime = connectionTime;
            this.disconnectTime = disconnectTime;
        }

        public String getSsid() {
            return ssid;
        }

        public long getConnectTime() {
            return connectionTime;
        }

        public long getDisconnectTime() {
            return disconnectTime;
        }

        @Override
        public String toString() {
            return "WifiState{" +
                    "ssid='" + ssid + '\'' +
                    ", connectionTime=" + connectionTime +
                    ", disconnectTime=" + disconnectTime +
                    '}';
        }
    }

    private static final String WIFI_STATE_HISTORY_STORAGE = "prefs.wifi.state.history";
    private static final String PREFS_LAST_SSID = "prefs.wifi.last.ssid";
    private static final String PREFS_LAST_SSID_CONNECT_TIME = "prefs.wifi.last.ssid.connect.time";
    private static final String PREFS_LAST_SSID_DISCONNECT_TIME = "prefs.wifi.last.ssid.disconnect.time";

    private final Context context;

    public WifiStateHistory(Context context) {
        this.context = context;
    }

    public void recordConnectedWifi(String ssid) {
        SharedPreferences.Editor edit = getSharedPreferences().edit();
        edit.putString(PREFS_LAST_SSID, ssid);
        edit.putLong(PREFS_LAST_SSID_CONNECT_TIME, System.currentTimeMillis());
        edit.putLong(PREFS_LAST_SSID_DISCONNECT_TIME, -1l);
        edit.commit();

        LOG.info("recorded wifi connect: {}", ssid);
    }

    public void recordDisconnectedWifi() {
        WifiState lastWifiState = getLastWifiState();

        if (lastWifiState.getDisconnectTime() < 0) {
            SharedPreferences.Editor editor = getSharedPreferences().edit();
            editor.putLong(PREFS_LAST_SSID_DISCONNECT_TIME, System.currentTimeMillis());
            editor.commit();

            LOG.info("recorded wifi disconnect");
        }

        LOG.info("no wifi disconnect recorded");
    }

    public WifiState getLastWifiState() {
        SharedPreferences prefs = getSharedPreferences();

        return new WifiState(
                prefs.getString(PREFS_LAST_SSID, null),
                prefs.getLong(PREFS_LAST_SSID_CONNECT_TIME, -1l),
                prefs.getLong(PREFS_LAST_SSID_DISCONNECT_TIME, -1));
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(WIFI_STATE_HISTORY_STORAGE, Context.MODE_PRIVATE);
    }
}
