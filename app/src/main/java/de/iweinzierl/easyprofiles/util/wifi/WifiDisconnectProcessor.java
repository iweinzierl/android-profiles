package de.iweinzierl.easyprofiles.util.wifi;

import android.content.Context;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;
import de.iweinzierl.easyprofiles.util.ProfileActivator;

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
        try {
            new ProfileActivator(context).deactivate(trigger.export());
        } catch (ProfileActivator.NotFoundException e) {
            LOG.warn("Unable to deactivate wifi trigger", e);
        } catch (ProfileActivator.NotPermittedException e) {
            LOG.warn("Deactivation of wifi trigger is not permitted", e);
        }
    }
}
