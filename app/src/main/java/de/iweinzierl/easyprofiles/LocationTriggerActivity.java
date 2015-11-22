package de.iweinzierl.easyprofiles;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toolbar;

import com.orm.SugarRecord;

import org.slf4j.Logger;

import java.util.List;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.LocationBasedTrigger;
import de.iweinzierl.easyprofiles.fragments.LocationTriggerTabFragment;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class LocationTriggerActivity extends FragmentActivity implements LocationTriggerTabFragment.Callback {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(LocationTriggerActivity.class.getName());

    private LocationTriggerTabFragment locationTriggerTabFragment;

    private LocationBasedTrigger locationTrigger = new LocationBasedTrigger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_trigger);

        locationTriggerTabFragment = new LocationTriggerTabFragment();

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        toolbarBottom.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
        toolbarBottom.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, locationTriggerTabFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateProfileList();
    }

    @Override
    public void onLocationSelected(double lat, double lon, int radius) {
        locationTrigger.setLat(lat);
        locationTrigger.setLon(lon);
        locationTrigger.setRadius(radius);
    }

    @Override
    public void onEnterProfileSelected(Profile profile) {
        locationTrigger.setOnActivateProfile(profile);
    }

    @Override
    public void onExitProfileSelected(Profile profile) {
        locationTrigger.setOnDeactivateProfile(profile);
    }

    private void updateProfileList() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                List<Profile> profiles = SugarRecord.listAll(Profile.class);
                LOG.debug("Found {} Profiles", profiles.size());

                locationTriggerTabFragment.setProfiles(profiles);

                return null;
            }
        }.execute();
    }

    private void cancel() {
        finish();
    }

    private void save() {
        if (locationTrigger.getOnActivateProfile() != null && locationTrigger.getLat() != 0 && locationTrigger.getLon() != 0 && locationTrigger.getRadius() != 0) {
            PersistentTrigger persistentTrigger = locationTrigger.export();
            SugarRecord.save(persistentTrigger);
            finish();
        } else {
            LOG.warn("Incomplete configuration for location based trigger");
            new AlertDialog.Builder(this)
                    .setTitle(R.string.activity_location_trigger_incomplete_error_title)
                    .setMessage(R.string.activity_location_trigger_incomplete_error_message)
                    .create()
                    .show();
        }
    }
}
