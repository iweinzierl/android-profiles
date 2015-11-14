package de.iweinzierl.easyprofiles.domain;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import com.orm.SugarRecord;

import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class LocationBasedTrigger extends BaseTrigger {

    public static class Data {
        private double lat;
        private double lon;
        private int radius;

        public Data(double lat, double lon, int radius) {
            this.lat = lat;
            this.lon = lon;
            this.radius = radius;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(float lon) {
            this.lon = lon;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }
    }


    private double lat;
    private double lon;
    private int radius;

    public LocationBasedTrigger() {
        super(TriggerType.LOCATION_BASED);
    }

    @Override
    public void apply(PersistentTrigger persistentTrigger) {
        setId(persistentTrigger.getId());
        setEnabled(persistentTrigger.isEnabled());
        setOnActivateProfile(SugarRecord.findById(Profile.class, persistentTrigger.getOnActivateProfileId()));
        setOnDeactivateProfile(SugarRecord.findById(Profile.class, persistentTrigger.getOnDeactivateProfileId()));

        applyData(persistentTrigger.getData());
    }

    @Override
    public void applyData(String dataJson) {
        Data data = readJsonData(dataJson);
        setLat(data.getLat());
        setLon(data.getLon());
        setRadius(data.getRadius());
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("onActivateProfile", onActivateProfile)
                .add("onDeactivateProfile", onDeactivateProfile)
                .add("lat", lat)
                .add("lon", lon)
                .add("radius", radius)
                .toString();
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    private String createJsonData() {
        Data data = new Data(getLat(), getLon(), getRadius());
        return new Gson().toJson(data);
    }

    private Data readJsonData(String data) {
        return new Gson().fromJson(data, Data.class);
    }
}
