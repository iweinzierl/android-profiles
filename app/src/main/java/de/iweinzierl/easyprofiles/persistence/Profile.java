package de.iweinzierl.easyprofiles.persistence;

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
}
