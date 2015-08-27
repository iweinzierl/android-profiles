package de.iweinzierl.easyprofiles.persistence;

import com.google.common.base.MoreObjects;
import com.orm.SugarRecord;

public class Profile extends SugarRecord<Profile> {

    private String name;

    private WifiSettings wifiSettings;
    private VolumeSettings volumeSettings;
    private DataSettings dataSettings;
    private ExtraSettings extraSettings;

    public Profile() {
        this.wifiSettings = new WifiSettings();
        this.volumeSettings = new VolumeSettings();
        this.dataSettings = new DataSettings();
        this.extraSettings = new ExtraSettings();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WifiSettings getWifiSettings() {
        return wifiSettings;
    }

    public void setWifiSettings(WifiSettings wifiSettings) {
        this.wifiSettings = wifiSettings;
    }

    public VolumeSettings getVolumeSettings() {
        return volumeSettings;
    }

    public void setVolumeSettings(VolumeSettings volumeSettings) {
        this.volumeSettings = volumeSettings;
    }

    public DataSettings getDataSettings() {
        return dataSettings;
    }

    public void setDataSettings(DataSettings dataSettings) {
        this.dataSettings = dataSettings;
    }

    public ExtraSettings getExtraSettings() {
        return extraSettings;
    }

    public void setExtraSettings(ExtraSettings extraSettings) {
        this.extraSettings = extraSettings;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("wifiSettings", wifiSettings)
                .add("volumeSettings", volumeSettings)
                .add("dataSettings", dataSettings)
                .add("extraSettings", extraSettings)
                .toString();
    }

    /**
     * Save a profile to database and also persist relations.
     *
     * NOTE: this is a hack to work around the problem that SugarORM is not saving relations.
     */
    @Override
    public void save() {
        if (wifiSettings != null) {
            wifiSettings.save();
        }

        if (volumeSettings != null) {
            volumeSettings.save();
        }

        if (dataSettings != null) {
            dataSettings.save();
        }

        if (extraSettings != null) {
            extraSettings.save();
        }

        super.save();
    }

    public static Profile of(long id, String name, VolumeSettings volumeSettings) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setName(name);
        profile.setVolumeSettings(volumeSettings);
        return profile;
    }
}
