package de.iweinzierl.easyprofiles.persistence;

import com.google.common.base.MoreObjects;
import com.orm.SugarRecord;
import com.orm.entity.annotation.EntityListeners;

import de.iweinzierl.easyprofiles.persistence.listener.TimeBasedTriggerActivationListener;

@EntityListeners({TimeBasedTriggerActivationListener.class})
public class PersistentTrigger extends SugarRecord {

    private TriggerType type;

    private String data;

    private long onActivateProfileId;
    private long onDeactivateProfileId;

    private boolean enabled;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TriggerType getType() {
        return type;
    }

    public void setType(TriggerType type) {
        this.type = type;
    }

    public long getOnActivateProfileId() {
        return onActivateProfileId;
    }

    public void setOnActivateProfileId(long onActivateProfileId) {
        this.onActivateProfileId = onActivateProfileId;
    }

    public long getOnDeactivateProfileId() {
        return onDeactivateProfileId;
    }

    public void setOnDeactivateProfileId(long onDeactivateProfileId) {
        this.onDeactivateProfileId = onDeactivateProfileId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("data", data)
                .add("onActivateProfileId", onActivateProfileId)
                .add("onDeactivateProfileId", onDeactivateProfileId)
                .add("enabled", enabled)
                .toString();
    }

    public static PersistentTrigger of(TriggerType type, String data, long onActivateProfileId) {
        PersistentTrigger persistentTrigger = new PersistentTrigger();
        persistentTrigger.setType(type);
        persistentTrigger.setData(data);
        persistentTrigger.setOnActivateProfileId(onActivateProfileId);
        persistentTrigger.setEnabled(true);
        return persistentTrigger;
    }
}
