package de.iweinzierl.easyprofiles.util.wifi;

import android.content.Context;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;

public class WifiDisconnectProcessor extends WifiProcessor {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(WifiDisconnectProcessor.class.getName());

    public WifiDisconnectProcessor(Context context, String ssid) {
        super(context, ssid);
    }

    @Override
    public boolean matches(WifiBasedTrigger trigger) {
        return areSsidEqual(ssid, trigger.getSsid()) && trigger.getOnDeactivateProfile() != null;
    }

    @Override
    protected void enable(WifiBasedTrigger trigger) {
        if (trigger == null) {
            LOG.warn("Wifi trigger that should be activated is null!");
        } else if (trigger.getOnDeactivateProfile() == null) {
            LOG.warn("Deactivation profile of wifi trigger that should be activated is null!");
        } else {
            trigger.getOnDeactivateProfile().activate(context);
        }
    }
}
