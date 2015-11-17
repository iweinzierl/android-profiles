package de.iweinzierl.easyprofiles.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orm.SugarRecord;

import org.joda.time.LocalTime;
import org.slf4j.Logger;

import java.util.List;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;
import de.iweinzierl.easyprofiles.util.ProfileActivator;

public class TimeBasedTriggerActivationService extends Service {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(TimeBasedTriggerActivationService.class.getName());

    public static final String EXTRA_TRIGGER_ID = "extra.trigger.id";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long triggerId = intent.getLongExtra(EXTRA_TRIGGER_ID, -1);
        LOG.info("Service to start/stop time based trigger: {}", triggerId);

        TimeBasedTrigger trigger = findTrigger(triggerId);

        if (isActivationTime(trigger)) {
            activate(trigger);
        } else if (isDeactivationTime(trigger)) {
            deactivate(trigger);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void activate(TimeBasedTrigger trigger) {
        try {
            new ProfileActivator(getApplicationContext()).activate(trigger.export());
        } catch (ProfileActivator.NotFoundException e) {
            LOG.warn("Unable to activate time based trigger", e);
        } catch (ProfileActivator.NotPermittedException e) {
            LOG.warn("Activation of time based trigger not permitted", e);
        }
    }

    private void deactivate(TimeBasedTrigger trigger) {
        try {
            new ProfileActivator(getApplicationContext()).deactivate(trigger.export());
        } catch (ProfileActivator.NotFoundException e) {
            LOG.warn("Unable to deactivate time based trigger", e);
        } catch (ProfileActivator.NotPermittedException e) {
            LOG.warn("Deactivation of time based trigger not permitted", e);
        }
    }

    private TimeBasedTrigger findTrigger(long triggerId) {
        if (triggerId > 0) {
            LOG.debug("Find trigger by id: {}", triggerId);

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
