package de.iweinzierl.easyprofiles.util;

import de.iweinzierl.easyprofiles.persistence.Trigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class TriggerBuilder {

    private TriggerType triggerType;

    private String data;

    private long onActivateProfile;
    private long onDeactivateProfile;

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

    public TriggerBuilder setOnActivateProfile(long activateProfile) {
        this.onActivateProfile = activateProfile;
        return this;
    }

    public TriggerBuilder setOnDeactivateProfile(long deactivateProfile) {
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

        throw new IllegalArgumentException("Wrong trigger type set: " + triggerType);
    }

    private Trigger buildWifiTrigger() {
        return Trigger.of(TriggerType.WIFI, data, onActivateProfile);
    }

    private Trigger buildTimeBasedTrigger() {
        // TODO
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }
}
