package de.iweinzierl.easyprofiles.util.geo;

import android.content.Context;
import android.location.Location;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.util.ProfileActivator;

public class QuitLocationProcessor extends LocationProcessor {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(QuitLocationProcessor.class.getName());

    public QuitLocationProcessor(Context context) {
        super(context);
    }

    @Override
    protected void doProcess(PersistentTrigger trigger, Location location) {
        try {
            new ProfileActivator(context).deactivate(trigger);
        } catch (ProfileActivator.NotFoundException e) {
            LOG.warn("Unable to deactivate trigger", e);
        } catch (ProfileActivator.NotPermittedException e) {
            LOG.warn("Deactivation of trigger is not permitted", e);
        }
    }
}
