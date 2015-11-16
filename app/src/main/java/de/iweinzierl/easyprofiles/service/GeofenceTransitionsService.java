package de.iweinzierl.easyprofiles.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import org.slf4j.Logger;

import java.util.List;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.util.geo.EnterLocationProcessor;
import de.iweinzierl.easyprofiles.util.geo.GeofenceRequestIdGenerator;
import de.iweinzierl.easyprofiles.util.geo.QuitLocationProcessor;

public class GeofenceTransitionsService extends IntentService {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(GeofenceTransitionsService.class.getName());

    public GeofenceTransitionsService() {
        super(GeofenceTransitionsService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LOG.info("Received Geofencing Event");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            LOG.error("Geofencing Event has error: {}", geofencingEvent.getErrorCode());
            return;
        }

        List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
        Location location = geofencingEvent.getTriggeringLocation();
        LOG.debug("geofencing event was triggered by {} geofences at {} | {}", geofences.size(), location.getLatitude(), location.getLongitude());

        try {
            long triggerId = new GeofenceRequestIdGenerator().extractTriggerId(geofences.get(0).getRequestId());

            if (geofencingEvent.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_ENTER) {
                new EnterLocationProcessor(getApplicationContext()).process(triggerId, geofencingEvent.getTriggeringLocation());
            } else {
                new QuitLocationProcessor(getApplicationContext()).process(triggerId, geofencingEvent.getTriggeringLocation());
            }
        } catch (IllegalArgumentException iae) {
            LOG.error("Unable to process geofencing event!", iae);
        }
    }
}
