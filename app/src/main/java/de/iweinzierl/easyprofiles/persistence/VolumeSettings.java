package de.iweinzierl.easyprofiles.persistence;

import com.orm.SugarRecord;

public class VolumeSettings extends SugarRecord<VolumeSettings> {

    private int alarmVolume;
    private int mediaVolume;
    private int ringtoneVolume;
    private int notificationVolume;

    private boolean shouldVibrate;

    public VolumeSettings() {
        this.alarmVolume = 4;
        this.mediaVolume = 4;
        this.ringtoneVolume = 4;
        this.notificationVolume = 4;
        this.shouldVibrate = true;
    }

    public VolumeSettings(int alarmVolume, int mediaVolume, int ringtoneVolume, int notificationVolume, boolean shouldVibrate) {
        this.alarmVolume = alarmVolume;
        this.mediaVolume = mediaVolume;
        this.ringtoneVolume = ringtoneVolume;
        this.notificationVolume = notificationVolume;
        this.shouldVibrate = shouldVibrate;
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

    public boolean isShouldVibrate() {
        return shouldVibrate;
    }

    public void setShouldVibrate(boolean shouldVibrate) {
        this.shouldVibrate = shouldVibrate;
    }
}
