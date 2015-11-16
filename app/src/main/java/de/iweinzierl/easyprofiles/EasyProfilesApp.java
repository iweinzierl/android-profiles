package de.iweinzierl.easyprofiles;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.orm.SugarApp;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;

public class EasyProfilesApp extends SugarApp {

    private static final String LOG_TAG = "easyprofiles";

    private static final Logger LOG = AndroidLoggerFactory.getInstance(LOG_TAG).getLogger(EasyProfilesApp.class.getName());

    private static GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

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
