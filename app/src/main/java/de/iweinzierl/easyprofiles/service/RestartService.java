package de.iweinzierl.easyprofiles.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.EasyProfilesApp;

public class RestartService extends BroadcastReceiver {

    public static final String ACTION_RESTART = "de.iweinzierl.easyprofiles.service.ACTION_RESTART";

    private static final Logger LOG = AndroidLoggerFactory.getInstance(EasyProfilesApp.LOG_TAG).getLogger(RestartService.class.getName());

    @Override
    public void onReceive(Context context, Intent intent) {
        LOG.info("Restart services (received broadcast event)");
    }
}
