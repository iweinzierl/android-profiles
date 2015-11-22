package de.iweinzierl.easyprofiles.persistence.listener;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.orm.entity.annotation.PostPersist;

import org.slf4j.Logger;

import java.util.Date;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;
import de.iweinzierl.easyprofiles.service.TimeBasedTriggerActivationService;

public class TimeBasedTriggerActivator {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(TimeBasedTriggerActivator.class.getName());

    private final Context context;

    public TimeBasedTriggerActivator(Context context) {
        this.context = context;
    }

    @PostPersist
    public void postPersist(PersistentTrigger trigger) {
        LOG.info("Activate trigger: {}", trigger);

        if (trigger.getType() == TriggerType.TIME_BASED && trigger.isEnabled()) {
            TimeBasedTrigger timeTrigger = new TimeBasedTrigger();
            timeTrigger.apply(trigger);

            setProfileActivation(timeTrigger);
            setProfileDeactivation(timeTrigger);
        }
    }

    private void setProfileActivation(TimeBasedTrigger timeTrigger) {
        long millisToday = timeTrigger.getActivationTime().toDateTimeToday().getMillis();
        Date startDate = new Date(millisToday);
        LOG.info("Set up alarm manager to start time based trigger at: {}", startDate);

        setProfileAlarm(timeTrigger, millisToday);
    }

    private void setProfileDeactivation(TimeBasedTrigger timeTrigger) {
        if (timeTrigger.getDeactivationTime() != null) {
            long millisToday = timeTrigger.getDeactivationTime().toDateTimeToday().getMillis();
            Date stopDate = new Date(millisToday);
            LOG.info("Set up alarm manager to stop time based trigger at: {}", stopDate);

            setProfileAlarm(timeTrigger, millisToday);
        }
    }

    private void setProfileAlarm(TimeBasedTrigger trigger, long millisToday) {
        Intent intent = new Intent(context, TimeBasedTriggerActivationService.class);
        intent.putExtra(TimeBasedTriggerActivationService.EXTRA_TRIGGER_ID, trigger.getId());
        PendingIntent operation = PendingIntent.getService(context, (int) millisToday, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millisToday, operation);
    }
}
