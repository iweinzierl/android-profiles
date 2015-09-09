package de.iweinzierl.easyprofiles.persistence;

import com.google.common.base.MoreObjects;
import com.orm.SugarRecord;

public class VolumeSettings extends SugarRecord<VolumeSettings> {

    private int alarmVolume;
    private int mediaVolume;
    private int ringtoneVolume;
    private int notificationVolume;

    private RingtoneMode ringtoneMode;

    public VolumeSettings() {
        this.alarmVolume = 4;
        this.mediaVolume = 4;
        this.ringtoneVolume = 4;
        this.notificationVolume = 4;
        this.ringtoneMode = RingtoneMode.NORMAL;
    }

    public VolumeSettings(int alarmVolume, int mediaVolume, int ringtoneVolume, int notificationVolume, RingtoneMode ringtoneMode) {
        this.alarmVolume = alarmVolume;
        this.mediaVolume = mediaVolume;
        this.ringtoneVolume = ringtoneVolume;
        this.notificationVolume = notificationVolume;
        this.ringtoneMode = ringtoneMode;
    }

    public int getAlarmVolume() {
        return alarmVolume;
    }

    public void setAlarmVolume(int alarmVolume) {
        this.alarmVolume = alarmVolume;
    }

    public int getMediaVolume() {
        return mediaVolume;
    }

    public void setMediaVolume(int mediaVolume) {
        this.mediaVolume = mediaVolume;
    }

    public int getRingtoneVolume() {
        return ringtoneVolume;
    }

    public void setRingtoneVolume(int ringtoneVolume) {
        this.ringtoneVolume = ringtoneVolume;
    }

    public int getNotificationVolume() {
        return notificationVolume;
    }

    public void setNotificationVolume(int notificationVolume) {
        this.notificationVolume = notificationVolume;
    }

    public RingtoneMode getRingtoneMode() {
        return ringtoneMode;
    }

    public void setRingtoneMode(RingtoneMode ringtoneMode) {
        this.ringtoneMode = ringtoneMode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("alarmVolume", alarmVolume)
                .add("mediaVolume", mediaVolume)
                .add("ringtoneVolume", ringtoneVolume)
                .add("notificationVolume", notificationVolume)
                .add("ringtoneMode", ringtoneMode)
                .toString();

    }
}
