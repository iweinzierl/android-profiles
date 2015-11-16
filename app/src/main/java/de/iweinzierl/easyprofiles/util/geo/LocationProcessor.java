package de.iweinzierl.easyprofiles.util.geo;

import android.content.Context;
import android.location.Location;

import com.orm.SugarRecord;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;

public abstract class LocationProcessor {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(LocationProcessor.class.getName());

    private final Context context;

    public LocationProcessor(Context context) {
        this.context = context;
    }

    public void process(long triggerId, Location location) {
        PersistentTrigger trigger = SugarRecord.findById(PersistentTrigger.class, triggerId);

        if (trigger == null) {
            LOG.error("Did not find trigger for trigger id: {}", triggerId);
        } else {
            LOG.debug("Found trigger for trigger id {}: {}", triggerId, trigger);
            doProcess(trigger, location);
        }
    }

    protected abstract void doProcess(PersistentTrigger trigger, Location location);
}
