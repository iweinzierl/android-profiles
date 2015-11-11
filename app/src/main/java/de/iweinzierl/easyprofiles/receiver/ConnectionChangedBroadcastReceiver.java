package de.iweinzierl.easyprofiles.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.util.wifi.WifiProcessorFactory;

public class ConnectionChangedBroadcastReceiver extends BroadcastReceiver {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(ConnectionChangedBroadcastReceiver.class.getName());

    @Override
    public void onReceive(Context context, Intent intent) {
        LOG.debug("received network change broadcast event");
        WifiProcessorFactory.createProcessor(context).process();
    }
}
