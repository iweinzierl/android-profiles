package de.iweinzierl.easyprofiles.util.geo;

import android.content.Context;
import android.location.Location;

import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;

public class QuitLocationProcessor extends LocationProcessor {

    public QuitLocationProcessor(Context context) {
        super(context);
    }

    @Override
    protected void doProcess(PersistentTrigger trigger, Location location) {
        // TODO do disconnect
    }
}
