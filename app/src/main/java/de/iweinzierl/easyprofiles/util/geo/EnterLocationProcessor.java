package de.iweinzierl.easyprofiles.util.geo;

import android.content.Context;
import android.location.Location;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.util.ProfileActivator;

public class EnterLocationProcessor extends LocationProcessor {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(EnterLocationProcessor.class.getName());

    public EnterLocationProcessor(Context context) {
        super(context);
    }

    @Override
    protected void doProcess(PersistentTrigger trigger, Location location) {
        try {
            new ProfileActivator(context).activate(trigger);
        } catch (ProfileActivator.NotFoundException e) {
            LOG.warn("Unable to activate trigger", e);
        } catch (ProfileActivator.NotPermittedException e) {
            LOG.warn("Activation of trigger is not permitted", e);
        }
    }
}
