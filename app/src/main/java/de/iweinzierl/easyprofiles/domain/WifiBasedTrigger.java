package de.iweinzierl.easyprofiles.domain;

import com.google.gson.Gson;
import com.orm.SugarRecord;

import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class WifiBasedTrigger extends BaseTrigger {

    private static class Data {
        private String ssid;

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }
    }

    private String ssid;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public WifiBasedTrigger() {
        super(TriggerType.WIFI);
    }

    @Override
    public void apply(PersistentTrigger persistentTrigger) {
        setId(getId());
        setEnabled(persistentTrigger.isEnabled());
        setOnActivateProfile(SugarRecord.findById(Profile.class, persistentTrigger.getOnActivateProfileId()));
        setOnDeactivateProfile(SugarRecord.findById(Profile.class, persistentTrigger.getOnDeactivateProfileId()));

        Data data = readJsonData(persistentTrigger.getData());
        setSsid(data.getSsid());
    }

    @Override
    public void applyData(String jsonData) {
        Data data = readJsonData(jsonData);
        setSsid(data.getSsid());
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

    private String createJsonData() {
        Data data = new Data();
        data.setSsid(getSsid());

        return new Gson().toJson(data);
    }

    private Data readJsonData(String jsonData) {
        return new Gson().fromJson(jsonData, Data.class);
    }
}
