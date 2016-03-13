package de.iweinzierl.easyprofiles.persistence.listener;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.orm.entity.annotation.PostPersist;

import org.slf4j.Logger;

import java.util.Date;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;
import de.iweinzierl.easyprofiles.util.time.PendingIntentGenerator;
import de.iweinzierl.easyprofiles.util.time.TimeBasedRequestCodeGenerator;
import de.iweinzierl.easyprofiles.util.time.TriggerActivationServiceIntent;

public class TimeBasedTriggerActivator {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(TimeBasedTriggerActivator.class.getName());

    private final Context context;

    public TimeBasedTriggerActivator(Context context) {
        this.context = context;
    }

    @PostPersist
    public void setProfileAlarms(PersistentTrigger trigger) {
        LOG.info("Activate trigger: {}", trigger);

        if (trigger.getType() == TriggerType.TIME_BASED && trigger.isEnabled()) {
            TimeBasedTrigger timeTrigger = new TimeBasedTrigger();
            timeTrigger.apply(trigger);

            setProfileActivation(timeTrigger);
            setProfileDeactivation(timeTrigger);
        }
    }

    private void setProfileActivation(TimeBasedTrigger trigger) {
        long millisToday = trigger.getActivationTime().toDateTimeToday().getMillis();
        Date startDate = new Date(millisToday);

        int requestCode = new TimeBasedRequestCodeGenerator(trigger).createActivationRequestCode();

        LOG.info("Set up alarm (request code = {}) to start time based trigger at: {}", requestCode, startDate);

        PendingIntent operation = new PendingIntentGenerator(context, trigger)
                .createDeactivationPendingIntent(new TriggerActivationServiceIntent(context, trigger));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                millisToday,
                AlarmManager.INTERVAL_DAY,
                operation);
    }

    private void setProfileDeactivation(TimeBasedTrigger trigger) {
        if (trigger.getDeactivationTime() != null) {
            long millisToday = trigger.getDeactivationTime().toDateTimeToday().getMillis();
            Date stopDate = new Date(millisToday);

            int requestCode = new TimeBasedRequestCodeGenerator(trigger).createDeactivationRequestCode();

            LOG.info("Set up alarm (request code = {}) to stop time based trigger at: {}", requestCode, stopDate);

            PendingIntent operation = new PendingIntentGenerator(context, trigger)
                    .createActivationPendingIntent(new TriggerActivationServiceIntent(context, trigger));

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    millisToday,
                    AlarmManager.INTERVAL_DAY,
                    operation);
        }
    }
}
