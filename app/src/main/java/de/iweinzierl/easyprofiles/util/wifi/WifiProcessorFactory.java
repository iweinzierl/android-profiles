package de.iweinzierl.easyprofiles.util.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;

public final class WifiProcessorFactory {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(WifiProcessorFactory.class.getName());
    private static final WifiProcessor NOOP_WIFI_PROCESSOR = new WifiNoOpProcessor();

    private WifiProcessorFactory() {
    }

    public static WifiProcessor createProcessor(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        WifiStateHistory wifiStateHistory = new WifiStateHistory(context);

        if (networkInfo == null) {
            LOG.warn("skip processing due to missing network info");
            return NOOP_WIFI_PROCESSOR;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {
            wifiStateHistory.recordConnectedWifi(wifiManager.getConnectionInfo().getSSID());
            return new WifiConnectProcessor(context, wifiManager.getConnectionInfo().getSSID());
        } else if (networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
            WifiStateHistory.WifiState lastWifiState = wifiStateHistory.getLastWifiState();
            LOG.debug("Found lastWifiState: {}", lastWifiState);

            if (lastWifiState != null && lastWifiState.getDisconnectTime() <= 0) {
                wifiStateHistory.recordDisconnectedWifi();
                return new WifiDisconnectProcessor(context, lastWifiState.getSsid());
            } else {
                return NOOP_WIFI_PROCESSOR;
            }
        }

        return NOOP_WIFI_PROCESSOR;
    }
}
