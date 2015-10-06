package de.iweinzierl.easyprofiles.persistence;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.google.common.base.MoreObjects;
import com.orm.dsl.Table;
import com.orm.entity.annotation.EntityListeners;

import de.iweinzierl.easyprofiles.persistence.listener.ProfileEntityListener;
import de.iweinzierl.easyprofiles.util.AudioManagerHelper;
import de.iweinzierl.easyprofiles.util.NotificationHelper;

@EntityListeners({ProfileEntityListener.class})
@Table
public class Profile {

    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void activate(Context context) {
        Log.i("easyprofiles", "Activate profile: " + getName());
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        new AudioManagerHelper(audioManager).adjustVolume(getVolumeSettings());
        new NotificationHelper(context).publishProfileNotification(this);
    }

    public static Profile of(long id, String name, VolumeSettings volumeSettings) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setName(name);
        profile.setVolumeSettings(volumeSettings);
        return profile;
    }
}
