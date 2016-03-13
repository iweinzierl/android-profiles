package de.iweinzierl.easyprofiles.util.time;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;

public class PendingIntentGenerator {

    private final Context context;
    private final TimeBasedTrigger trigger;

    public PendingIntentGenerator(Context context, TimeBasedTrigger trigger) {
        this.context = context;
        this.trigger = trigger;
    }

    public PendingIntent createActivationPendingIntent(final Intent activationIntent) {
        int requestCode = new TimeBasedRequestCodeGenerator(trigger).createActivationRequestCode();
        return PendingIntent.getBroadcast(
                context,
                requestCode,
                activationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public PendingIntent createDeactivationPendingIntent(final Intent deactivationIntent) {
        int requestCode = new TimeBasedRequestCodeGenerator(trigger).createDeactivationRequestCode();
        return PendingIntent.getBroadcast(
                context,
                requestCode,
                deactivationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
