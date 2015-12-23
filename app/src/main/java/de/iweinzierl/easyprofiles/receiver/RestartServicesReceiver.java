package de.iweinzierl.easyprofiles.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orm.SugarRecord;

import org.slf4j.Logger;

import java.util.List;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.EasyProfilesApp;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;
import de.iweinzierl.easyprofiles.persistence.listener.TimeBasedTriggerActivator;

public class RestartServicesReceiver extends BroadcastReceiver {

    public static final String ACTION_RESTART = "de.iweinzierl.easyprofiles.service.ACTION_RESTART";

    private static final Logger LOG = AndroidLoggerFactory.getInstance(EasyProfilesApp.LOG_TAG).getLogger(RestartServicesReceiver.class.getName());

    @Override
    public void onReceive(Context context, Intent intent) {
        LOG.info("Restart services (received broadcast event)");

        restartTimeBasedTriggers(context);
    }

    private void restartTimeBasedTriggers(Context context) {
        LOG.info("Going to restart time based triggers if existing");

        List<PersistentTrigger> triggers = SugarRecord.find(
                PersistentTrigger.class,
                "type = ? and enabled = 1",
                TriggerType.TIME_BASED.name());

        if (triggers != null && !triggers.isEmpty()) {
            int restarted = restartTimeBasedTriggers(context, triggers);
            LOG.info("Restarted {} time based triggers", restarted);
        }
    }

    private int restartTimeBasedTriggers(Context context, List<PersistentTrigger> triggers) {
        int restarted = 0;

        for (PersistentTrigger trigger : triggers) {
            if (restartTimeBasedTrigger(context, trigger)) {
                restarted++;
            }
        }

        return restarted;
    }

    private boolean restartTimeBasedTrigger(Context context, PersistentTrigger persistentTrigger) {
        // TODO cancel potential existing alarm
        new TimeBasedTriggerActivator(context).setProfileAlarms(persistentTrigger);
        return true;
    }
}
