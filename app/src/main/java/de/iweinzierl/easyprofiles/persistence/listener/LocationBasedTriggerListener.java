package de.iweinzierl.easyprofiles.persistence.listener;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.common.collect.Lists;
import com.orm.entity.annotation.PostPersist;
import com.orm.entity.annotation.PreRemove;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.EasyProfilesApp;
import de.iweinzierl.easyprofiles.domain.LocationBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;
import de.iweinzierl.easyprofiles.service.GeofenceTransitionsService;
import de.iweinzierl.easyprofiles.util.geo.GeofenceRequestIdGenerator;

public class LocationBasedTriggerListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(LocationBasedTriggerListener.class.getName());

    private final Context context;

    private PendingIntent geofencingIntent;

    public LocationBasedTriggerListener(Context context) {
        this.context = context;
    }

    @Override
    public void onConnected(Bundle bundle) {
        LOG.info("Connected to Google LocationService API");
    }

    @Override
    public void onConnectionSuspended(int i) {
        LOG.info("Connection to Google LocationService API suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        LOG.warn("Connection to Google LocationService API failed ({}): {}",
                connectionResult.getErrorCode(),
                connectionResult.getErrorMessage());
    }

    @PostPersist
    public void postPersist(PersistentTrigger persistentTrigger) {
        if (persistentTrigger.getType() != TriggerType.LOCATION_BASED) {
            return;
        }

        LOG.debug("received prePersist event for location based trigger");

        LocationBasedTrigger trigger = new LocationBasedTrigger();
        trigger.apply(persistentTrigger);

        if (trigger.isEnabled()) {
            addGeofence(trigger);
        } else {
            removeGeofence(trigger);
        }
    }

    @PreRemove
    public void preRemove(PersistentTrigger persistentTrigger) {
        if (persistentTrigger.getType() != TriggerType.LOCATION_BASED) {
            return;
        }

        LOG.debug("received preRemove event for location based trigger");

        LocationBasedTrigger trigger = new LocationBasedTrigger();
        trigger.apply(persistentTrigger);

        removeGeofence(trigger);
    }

    private void addGeofence(LocationBasedTrigger trigger) {
        Geofence geofence = createGeofence(trigger);

        GoogleApiClient googleApiClient = EasyProfilesApp.getGoogleApiClient();

        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    createGeofenceRequest(geofence),
                    createPendingIntent());
        } else {
            LOG.error("GoogleApiClient is null or not connected!");
        }
    }

    private Geofence createGeofence(LocationBasedTrigger trigger) {
        return new Geofence.Builder()
                .setRequestId(new GeofenceRequestIdGenerator().generateRequestId(trigger.getId()))
                .setExpirationDuration(365 * 24 * 60 * 60 * 1000)
                .setCircularRegion(trigger.getLat(), trigger.getLon(), trigger.getRadius())
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT)
                .build();
    }

    private PendingIntent createPendingIntent() {
        if (geofencingIntent == null) {
            Intent intent = new Intent(context, GeofenceTransitionsService.class);
            geofencingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return geofencingIntent;
    }

    private void removeGeofence(LocationBasedTrigger trigger) {
        GoogleApiClient googleApiClient = EasyProfilesApp.getGoogleApiClient();

        String requestId = new GeofenceRequestIdGenerator().generateRequestId(trigger.getId());
        LocationServices.GeofencingApi.removeGeofences(googleApiClient, Lists.newArrayList(requestId));

        // TODO evaluate result
    }
}
