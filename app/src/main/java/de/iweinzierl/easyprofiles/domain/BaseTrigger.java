package de.iweinzierl.easyprofiles.domain;

import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public abstract class BaseTrigger implements Trigger {

    protected Profile onActivateProfile;
    protected Profile onDeactivateProfile;

    protected TriggerType type;

    protected boolean enabled;

    protected BaseTrigger(TriggerType type) {
        this.type = type;
    }

    @Override
    public TriggerType getType() {
        return type;
    }

    @Override
    public Profile getOnActivateProfile() {
        return onActivateProfile;
    }

    @Override
    public Profile getOnDeactivateProfile() {
        return onDeactivateProfile;
    }

    @Override
    public void setOnActivateProfile(Profile onActivateProfile) {
        this.onActivateProfile = onActivateProfile;
    }

    @Override
    public void setOnDeactivateProfile(Profile onDeactivateProfile) {
        this.onDeactivateProfile = onDeactivateProfile;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
