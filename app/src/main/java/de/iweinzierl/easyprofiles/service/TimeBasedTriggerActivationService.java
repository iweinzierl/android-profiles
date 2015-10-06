package de.iweinzierl.easyprofiles.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.orm.SugarRecord;

import org.joda.time.LocalTime;

import java.util.List;

import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.TriggerType;
import de.iweinzierl.easyprofiles.util.NotificationHelper;

public class TimeBasedTriggerActivationService extends Service {

    public static final String EXTRA_TRIGGER_ID = "extra.trigger.id";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long triggerId = intent.getLongExtra(EXTRA_TRIGGER_ID, -1);
        Log.i("easyprofiles", "Service to start/stop time based trigger " + triggerId);

        TimeBasedTrigger trigger = findTrigger(triggerId);

        if (isActivationTime(trigger)) {
            activate(trigger);
        } else if (isDeactivationTime(trigger)) {
            deactivate(trigger);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void activate(TimeBasedTrigger trigger) {
        if (trigger.getOnActivateProfile() != null) {
            setProfile(trigger.getOnActivateProfile());
        } else {
            Log.w("easyprofiles", "Profile for 'activation' is null!");
        }
    }

    private void deactivate(TimeBasedTrigger trigger) {
        if (trigger.getOnDeactivateProfile() != null) {
            setProfile(trigger.getOnDeactivateProfile());
        } else {
            Log.w("easyprofiles", "Profile for 'deactivation' is null!");
        }
    }

    private void setProfile(Profile profile) {
        profile.activate(getApplicationContext());

        NotificationHelper helper = new NotificationHelper(getApplicationContext());
        helper.publishProfileNotification(profile);
    }

    private TimeBasedTrigger findTrigger(long triggerId) {
        if (triggerId > 0) {
            Log.d("easyprofiles", "Find trigger by id: " + triggerId);

            PersistentTrigger trigger = SugarRecord.findById(PersistentTrigger.class, triggerId);
            return transformTrigger(trigger);
        }

        List<PersistentTrigger> triggers = SugarRecord.find(
                PersistentTrigger.class,
                "type = ? and enabled = 1",
                TriggerType.TIME_BASED.name());

        for (PersistentTrigger option : triggers) {
            if (matchesCondition(option)) {
                return transformTrigger(option);
            }
        }

        return null;
    }

    private boolean matchesCondition(PersistentTrigger trigger) {
        TimeBasedTrigger timeBasedTrigger = transformTrigger(trigger);
        return isActivationTime(timeBasedTrigger) || isDeactivationTime(timeBasedTrigger);
    }

    private boolean isActivationTime(TimeBasedTrigger trigger) {
        return isNow(trigger.getActivationTime());
    }

    private boolean isDeactivationTime(TimeBasedTrigger trigger) {
        return isNow(trigger.getDeactivationTime());
    }

    private boolean isNow(LocalTime time) {
        LocalTime now = LocalTime.now();
        return now.getHourOfDay() == time.getHourOfDay() && now.getMinuteOfHour() == time.getMinuteOfHour();
    }

    @NonNull
    private TimeBasedTrigger transformTrigger(PersistentTrigger trigger) {
        TimeBasedTrigger timeBasedTrigger = new TimeBasedTrigger();
        timeBasedTrigger.apply(trigger);
        return timeBasedTrigger;
    }
}
