package de.iweinzierl.easyprofiles.util;

import android.media.AudioManager;
import android.util.Log;

import de.iweinzierl.easyprofiles.persistence.VolumeSettings;

public class AudioManagerHelper {

    private AudioManager audioManager;

    private int maxAlarmVolume;
    private int maxMediaVolume;
    private int maxRingtoneVolume;
    private int maxNotificationVolume;

    public AudioManagerHelper(AudioManager audioManager) {
        this.audioManager = audioManager;
        determineMaxVolumes();
    }

    public void adjustVolume(VolumeSettings volumeSettings) {
        Log.d("easyprofiles", "Adjust volume settings to: " + volumeSettings);
        setAlarmVolume(volumeSettings.getAlarmVolume());
        setMediaVolume(volumeSettings.getMediaVolume());
        setRingtoneVolume(volumeSettings.getRingtoneVolume());
        setNotificationVolume(volumeSettings.getNotificationVolume());
    }

    private void determineMaxVolumes() {
        maxAlarmVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        maxMediaVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        maxRingtoneVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        maxNotificationVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
    }

    private void setAlarmVolume(int volume) {
        int calculatedVolume = calculateVolume(maxAlarmVolume, volume);
        Log.d("easyprofiles", "Set alarm volume to: " + calculatedVolume + " / " + maxAlarmVolume);

        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, calculatedVolume, AudioManager.FLAG_VIBRATE);
    }

    private void setMediaVolume(int volume) {
        int calculatedVolume = calculateVolume(maxMediaVolume, volume);
        Log.d("easyprofiles", "Set media volume to: " + calculatedVolume + " / " + maxMediaVolume);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, calculatedVolume, AudioManager.FLAG_VIBRATE);
    }

    private void setRingtoneVolume(int volume) {
        int calculatedVolume = calculateVolume(maxRingtoneVolume, volume);
        Log.d("easyprofiles", "Set ringtone volume to: " + calculatedVolume + " / " + maxRingtoneVolume);

        audioManager.setStreamVolume(AudioManager.STREAM_RING, calculatedVolume, AudioManager.FLAG_VIBRATE);
    }

    private void setNotificationVolume(int volume) {
        int calculatedVolume = calculateVolume(maxNotificationVolume, volume);
        Log.d("easyprofiles", "Set notification volume to: " + calculatedVolume + " / " + maxNotificationVolume);

        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, calculatedVolume, AudioManager.FLAG_VIBRATE);
    }

    private int calculateVolume(int max, int volume) {
        double ratio = (double) volume / 10;
        return (int) Math.ceil(max * ratio);
    }
}
