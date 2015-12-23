package de.iweinzierl.easyprofiles.util.time;

import android.content.Context;
import android.content.Intent;

import de.iweinzierl.easyprofiles.domain.BaseTrigger;
import de.iweinzierl.easyprofiles.service.TimeBasedTriggerActivationService;

public class TriggerActivationServiceIntent extends Intent {

    public TriggerActivationServiceIntent(Context packageContext, BaseTrigger trigger) {
        super(packageContext, TimeBasedTriggerActivationService.class);
        putExtra(TimeBasedTriggerActivationService.EXTRA_TRIGGER_ID, trigger.getId());
    }
}
