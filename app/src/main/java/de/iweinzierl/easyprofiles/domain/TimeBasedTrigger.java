package de.iweinzierl.easyprofiles.domain;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;

import java.util.Set;

import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class TimeBasedTrigger extends BaseTrigger {

    public static class Data {
        private String activationTime;
        private String deactivationTime;
        private Set<Day> repeatOnDays;

        public String getActivationTime() {
            return activationTime;
        }

        public void setActivationTime(String activationTime) {
            this.activationTime = activationTime;
        }

        public String getDeactivationTime() {
            return deactivationTime;
        }

        public void setDeactivationTime(String deactivationTime) {
            this.deactivationTime = deactivationTime;
        }

        public Set<Day> getRepeatOnDays() {
            return repeatOnDays;
        }

        public void setRepeatOnDays(Set<Day> repeatOnDays) {
            this.repeatOnDays = repeatOnDays;
        }
    }

    private String activationTime;
    private String deactivationTime;

    private Set<Day> repeatOnDays;

    public TimeBasedTrigger() {
        super(TriggerType.TIME_BASED);
    }

    public String getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(String activationTime) {
        this.activationTime = activationTime;
    }

    public String getDeactivationTime() {
        return deactivationTime;
    }

    public void setDeactivationTime(String deactivationTime) {
        this.deactivationTime = deactivationTime;
    }

    public Set<Day> getRepeatOnDays() {
        return repeatOnDays;
    }

    public void setRepeatOnDays(Set<Day> repeatOnDays) {
        this.repeatOnDays = repeatOnDays;
    }

    @Override
    public void apply(PersistentTrigger persistentTrigger) {
        setEnabled(persistentTrigger.isEnabled());
        setOnActivateProfile(Profile.findById(Profile.class, persistentTrigger.getOnActivateProfileId()));
        setOnDeactivateProfile(Profile.findById(Profile.class, persistentTrigger.getOnDeactivateProfileId()));

        Data data = readJsonData(persistentTrigger.getData());
        setActivationTime(data.getActivationTime());
        setDeactivationTime(data.getDeactivationTime());
        setRepeatOnDays(data.getRepeatOnDays());
    }

    @Override
    public void applyData(String dataJson) {
        Data data = readJsonData(dataJson);
        setActivationTime(data.getActivationTime());
        setDeactivationTime(data.getDeactivationTime());
        setRepeatOnDays(data.getRepeatOnDays());
    }

    @Override
    public PersistentTrigger export() {
        PersistentTrigger trigger = new PersistentTrigger();
        trigger.setEnabled(isEnabled());
        trigger.setType(getType());
        trigger.setOnActivateProfileId(getOnActivateProfile().getId());

        if (getOnDeactivateProfile() != null) {
            trigger.setOnDeactivateProfileId(getOnDeactivateProfile().getId());
        }

        trigger.setData(createJsonData());

        return trigger;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("onActivateProfile", onActivateProfile)
                .add("onDeactivateProfile", onDeactivateProfile)
                .add("activationTime", activationTime)
                .add("deactivationTime", deactivationTime)
                .add("repeatOnDays", repeatOnDays)
                .add("enabled", enabled)
                .toString();
    }

    private String createJsonData() {
        Data data = new Data();
        data.setActivationTime(getActivationTime());
        data.setDeactivationTime(getDeactivationTime());
        data.setRepeatOnDays(getRepeatOnDays());

        return new Gson().toJson(data);
    }

    private Data readJsonData(String data) {
        return new Gson().fromJson(data, Data.class);
    }
}
