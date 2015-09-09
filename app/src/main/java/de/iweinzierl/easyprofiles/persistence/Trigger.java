package de.iweinzierl.easyprofiles.persistence;

import com.google.common.base.MoreObjects;
import com.orm.SugarRecord;

public class Trigger extends SugarRecord<Trigger> {

    private TriggerType type;
    private String data;
    private long profileId;

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

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("data", data)
                .add("profileId", profileId)
                .toString();
    }

    public static Trigger of(TriggerType type, String data, long profileId) {
        Trigger trigger = new Trigger();
        trigger.setType(type);
        trigger.setData(data);
        trigger.setProfileId(profileId);
        return trigger;
    }
}
