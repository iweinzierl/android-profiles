package de.iweinzierl.easyprofiles;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.orm.SugarApp;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.logging.DbLogger;

public class EasyProfilesApp extends SugarApp {

    private static final String LOG_TAG = "easyprofiles";

    private Logger LOG;

    private static GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidLoggerFactory androidLoggerFactory = AndroidLoggerFactory.getInstance();
        androidLoggerFactory.setLogTag(LOG_TAG);
        androidLoggerFactory.setContext(this);
        androidLoggerFactory.setLoggerClass(DbLogger.class);

        LOG = androidLoggerFactory.getLogger(EasyProfilesApp.class.getName());
        LOG.info("Application startup");
    }

    public static GoogleApiClient setupGoogleApiClient(Context context, GoogleApiClient.ConnectionCallbacks connectionCallback, GoogleApiClient.OnConnectionFailedListener connectFailureListener) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(connectionCallback)
                    .addOnConnectionFailedListener(connectFailureListener)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }

        return googleApiClient;
    }

    public static GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }
}
