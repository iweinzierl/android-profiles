package de.iweinzierl.easyprofiles.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.orm.SugarRecord;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;

import java.util.List;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.EasyProfilesApp;
import de.iweinzierl.easyprofiles.domain.Day;
import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;
import de.iweinzierl.easyprofiles.util.ProfileActivator;

public class TimeBasedTriggerReceiver extends BroadcastReceiver {

    public static final String EXTRA_TRIGGER_ID = "extra.trigger.id";

    private static final Logger LOG = AndroidLoggerFactory.getInstance(EasyProfilesApp.LOG_TAG).getLogger(TimeBasedTriggerReceiver.class.getName());

    public TimeBasedTriggerReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long triggerId = intent.getLongExtra(EXTRA_TRIGGER_ID, -1);

        TimeBasedTrigger trigger = findTrigger(triggerId);

        LOG.info("=======================================");
        LOG.info("Received alarm broadcast with intent: {}", intent);
        LOG.info("Trigger: {}", trigger);

        if (trigger != null && trigger.isEnabled()) {
            if (isActivationTime(trigger) && isRepeatingDay(trigger)) {
                activate(context, trigger);
            } else if (isDeactivationTime(trigger) && isRepeatingDay(trigger)) {
                deactivate(context, trigger);
            } else {
                LOG.warn("Neither activation nor deactivation time fits now");
            }
        }

        LOG.info("=======================================");
    }

    private TimeBasedTrigger findTrigger(long triggerId) {
        List<PersistentTrigger> triggers = SugarRecord.find(
                PersistentTrigger.class,
                "type = ? and enabled = 1 and id = ?",
                TriggerType.TIME_BASED.name(),
                String.valueOf(triggerId));

        return triggers == null || triggers.isEmpty()
                ? null
                : transformTrigger(triggers.get(0));
    }

    private void activate(Context context, TimeBasedTrigger trigger) {
        try {
            LOG.info("Activate time based profile: {}", trigger.getOnActivateProfile());
            new ProfileActivator(context).activate(trigger.export());
        } catch (ProfileActivator.NotFoundException e) {
            LOG.warn("Unable to activate time based trigger", e);
        } catch (ProfileActivator.NotPermittedException e) {
            LOG.warn("Activation of time based trigger not permitted", e);
        }
    }

    private void deactivate(Context context, TimeBasedTrigger trigger) {
        try {
            LOG.info("Deactivate time based profile: {}", trigger.getOnDeactivateProfile());
            new ProfileActivator(context).deactivate(trigger.export());
        } catch (ProfileActivator.NotFoundException e) {
            LOG.warn("Unable to deactivate time based trigger", e);
        } catch (ProfileActivator.NotPermittedException e) {
            LOG.warn("Deactivation of time based trigger not permitted", e);
        }
    }

    private boolean isActivationTime(TimeBasedTrigger trigger) {
        return isNow(trigger.getActivationTime());
    }

    private boolean isDeactivationTime(TimeBasedTrigger trigger) {
        return isNow(trigger.getDeactivationTime());
    }

    private boolean isRepeatingDay(TimeBasedTrigger trigger) {
        LocalDate now = LocalDate.now();
        for (Day day : trigger.getRepeatOnDays()) {
            if (day.getDayOfWeek() == now.getDayOfWeek()) {
                return true;
            }
        }

        return false;
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
