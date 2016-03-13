package de.iweinzierl.easyprofiles.util.time;

import android.content.Context;
import android.content.Intent;

import de.iweinzierl.easyprofiles.domain.BaseTrigger;
import de.iweinzierl.easyprofiles.receiver.TimeBasedTriggerReceiver;

public class TriggerActivationServiceIntent extends Intent {

    public TriggerActivationServiceIntent(Context packageContext, BaseTrigger trigger) {
        super(packageContext, TimeBasedTriggerReceiver.class);
        putExtra(TimeBasedTriggerReceiver.EXTRA_TRIGGER_ID, trigger.getId());
    }
}
