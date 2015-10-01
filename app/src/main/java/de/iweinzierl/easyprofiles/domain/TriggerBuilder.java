package de.iweinzierl.easyprofiles.domain;

import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class TriggerBuilder {

    private TriggerType triggerType;

    private String data;

    private Profile onActivateProfile;
    private Profile onDeactivateProfile;

    public TriggerBuilder() {
    }

    public TriggerBuilder setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
        return this;
    }

    public TriggerBuilder setData(String data) {
        this.data = data;
        return this;
    }

    public TriggerBuilder setOnActivateProfile(Profile activateProfile) {
        this.onActivateProfile = activateProfile;
        return this;
    }

    public TriggerBuilder setOnDeactivateProfile(Profile deactivateProfile) {
        this.onDeactivateProfile = deactivateProfile;
        return this;
    }

    public Trigger build() {
        switch (triggerType) {
            case WIFI:
                return buildWifiTrigger();
            case TIME_BASED:
                return buildTimeBasedTrigger();
        }

        throw new IllegalArgumentException("Unknown trigger type set: " + triggerType);
    }

    private Trigger buildWifiTrigger() {
        final WifiBasedTrigger trigger = new WifiBasedTrigger();
        trigger.setOnActivateProfile(onActivateProfile);
        trigger.setOnDeactivateProfile(onDeactivateProfile);
        trigger.setEnabled(true);
        trigger.applyData(data);

        return trigger;
    }

    private Trigger buildTimeBasedTrigger() {
        final TimeBasedTrigger trigger = new TimeBasedTrigger();
        trigger.setOnActivateProfile(onActivateProfile);
        trigger.setOnDeactivateProfile(onDeactivateProfile);
        trigger.setEnabled(true);
        trigger.applyData(data);

        return trigger;
    }
}
