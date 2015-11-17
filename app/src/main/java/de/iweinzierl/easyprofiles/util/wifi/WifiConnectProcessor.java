package de.iweinzierl.easyprofiles.util.wifi;

import android.content.Context;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;
import de.iweinzierl.easyprofiles.util.ProfileActivator;

public class WifiConnectProcessor extends WifiProcessor {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(WifiConnectProcessor.class.getName());

    public WifiConnectProcessor(Context context, String ssid) {
        super(context, ssid);
    }

    @Override
    public boolean matches(WifiBasedTrigger trigger) {
        return areSsidEqual(ssid, trigger.getSsid()) && trigger.getOnActivateProfile() != null;
    }

    @Override
    protected void enable(WifiBasedTrigger trigger) {
        try {
            new ProfileActivator(context).activate(trigger.export());
        } catch (ProfileActivator.NotFoundException e) {
            LOG.warn("Unable to activate wifi trigger", e);
        } catch (ProfileActivator.NotPermittedException e) {
            LOG.warn("Activation of wifi trigger is not permitted", e);
        }
    }
}
