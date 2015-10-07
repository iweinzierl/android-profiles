package de.iweinzierl.easyprofiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import com.orm.SugarRecord;

import org.joda.time.LocalTime;

import java.util.List;
import java.util.Set;

import de.iweinzierl.easyprofiles.domain.Day;
import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;
import de.iweinzierl.easyprofiles.fragments.ProfileSchedulerTabsFragment;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileSchedulerActivity extends Activity {

    public static final String EXTRA_TIME_TRIGGER_ID = "extra.timebasedtrigger.data";

    private ProfileSchedulerTabsFragment profileSchedulerTabsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_scheduler);

        profileSchedulerTabsFragment = new ProfileSchedulerTabsFragment();

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);

        toolbarBottom.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        toolbarBottom.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.profile_scheduler_fragment, profileSchedulerTabsFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateAvailableProfiles();
    }

    private void updateAvailableProfiles() {
        List<Profile> profiles = SugarRecord.listAll(Profile.class);
        profileSchedulerTabsFragment.setAvailableProfiles(profiles);
    }

    private void save() {
        Profile activationProfile = profileSchedulerTabsFragment.getActivationProfile();
        Profile deactivationProfile = profileSchedulerTabsFragment.getDeactivationProfile();
        LocalTime activationTime = profileSchedulerTabsFragment.getActivationTime();
        LocalTime deactivationTime = profileSchedulerTabsFragment.getDeactivationTime();
        Set<Day> repeatingDays = profileSchedulerTabsFragment.getRepeatingDays();

        Log.d("easyprofiles", "Schedule start: " + activationTime.toString("HH:mm") + " --> " + activationProfile);
        Log.d("easyprofiles", "Schedule stop :   " + deactivationTime.toString("HH:mm") + " --> " + deactivationProfile);

        TimeBasedTrigger trigger = new TimeBasedTrigger();
        trigger.setActivationTime(activationTime);
        trigger.setOnActivateProfile(activationProfile);
        trigger.setDeactivationTime(deactivationTime);
        trigger.setOnDeactivateProfile(deactivationProfile);
        trigger.setRepeatOnDays(repeatingDays);
        trigger.setEnabled(true);

        long id = SugarRecord.save(trigger.export());

        Intent data = new Intent();
        data.putExtra(EXTRA_TIME_TRIGGER_ID, id);

        setResult(RESULT_OK, data);
        finish();
    }

    private void cancel() {
        finish();
    }
}
