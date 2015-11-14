package de.iweinzierl.easyprofiles.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.GeofencingEvent;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;

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

        // TODO
    }
}
