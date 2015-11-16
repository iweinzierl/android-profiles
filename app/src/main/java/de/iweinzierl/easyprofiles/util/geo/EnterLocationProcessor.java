package de.iweinzierl.easyprofiles.util.geo;

import android.content.Context;
import android.location.Location;

import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;

public class EnterLocationProcessor extends LocationProcessor {

    public EnterLocationProcessor(Context context) {
        super(context);
    }

    @Override
    protected void doProcess(PersistentTrigger trigger, Location location) {
        // TODO do connect
    }
}
