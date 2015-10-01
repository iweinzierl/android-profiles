package de.iweinzierl.easyprofiles.domain;

import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public interface Trigger {

    TriggerType getType();

    Profile getOnActivateProfile();

    Profile getOnDeactivateProfile();

    void setOnActivateProfile(Profile onActivateProfile);

    void setOnDeactivateProfile(Profile onDeactivateProfile);

    boolean isEnabled();

    void setEnabled(boolean enabled);

    void apply(PersistentTrigger persistentTrigger);

    void applyData(String data);

    PersistentTrigger export();
}
