package de.iweinzierl.easyprofiles.domain;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import com.orm.SugarRecord;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Set;

import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class TimeBasedTrigger extends BaseTrigger {

    private static final String DATE_PATTERN = "HH:mm";

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

        public static Data from(LocalTime activationTime, LocalTime deactivationTime, Set<Day> repeatOnDays) {
            Data data = new Data();
            data.setActivationTime(activationTime.toString(DATE_PATTERN));
            data.setDeactivationTime(deactivationTime.toString(DATE_PATTERN));
            data.setRepeatOnDays(repeatOnDays);
            return data;
        }
    }

    private LocalTime activationTime;
    private LocalTime deactivationTime;

    private Set<Day> repeatOnDays;

    public TimeBasedTrigger() {
        super(TriggerType.TIME_BASED);
    }

    public LocalTime getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(LocalTime activationTime) {
        this.activationTime = activationTime;
    }

    public LocalTime getDeactivationTime() {
        return deactivationTime;
    }

    public void setDeactivationTime(LocalTime deactivationTime) {
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
        setId(persistentTrigger.getId());
        setEnabled(persistentTrigger.isEnabled());
        setOnActivateProfile(SugarRecord.findById(Profile.class, persistentTrigger.getOnActivateProfileId()));
        setOnDeactivateProfile(SugarRecord.findById(Profile.class, persistentTrigger.getOnDeactivateProfileId()));

        Data data = readJsonData(persistentTrigger.getData());
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_PATTERN);
        setActivationTime(dateTimeFormatter.parseLocalTime(data.getActivationTime()));
        setDeactivationTime(dateTimeFormatter.parseLocalTime(data.getDeactivationTime()));
        setRepeatOnDays(data.getRepeatOnDays());
    }

    @Override
    public void applyData(String dataJson) {
        Data data = readJsonData(dataJson);

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_PATTERN);
        setActivationTime(dateTimeFormatter.parseLocalTime(data.getActivationTime()));
        setDeactivationTime(dateTimeFormatter.parseLocalTime(data.getDeactivationTime()));
        setRepeatOnDays(data.getRepeatOnDays());
    }

    @Override
    public PersistentTrigger export() {
        PersistentTrigger trigger = new PersistentTrigger();
        trigger.setId(getId());
        trigger.setEnabled(isEnabled());
        trigger.setType(getType());
        trigger.setOnActivateProfileId(getOnActivateProfile().getId());

        if (getOnDeactivateProfile() != null) {
            trigger.setOnDeactivateProfileId(getOnDeactivateProfile().getId());
        }

        trigger.setData(createJsonData());

        return trigger;
    }

    public boolean fulfillsCondition(LocalTime testTime) {
        if (testTime == null) {
            return false;
        }

        return (testTime.isEqual(activationTime) || testTime.isAfter(activationTime))
                && (testTime.isBefore(deactivationTime) || testTime.isEqual(deactivationTime));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
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
        data.setActivationTime(getActivationTime().toString(DATE_PATTERN));
        data.setDeactivationTime(getDeactivationTime().toString(DATE_PATTERN));
        data.setRepeatOnDays(getRepeatOnDays());

        return new Gson().toJson(data);
    }

    private Data readJsonData(String data) {
        return new Gson().fromJson(data, Data.class);
    }
}
