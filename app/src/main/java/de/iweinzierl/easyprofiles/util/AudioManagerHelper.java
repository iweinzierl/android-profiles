package de.iweinzierl.easyprofiles.util;

import android.media.AudioManager;
import android.os.Build;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.persistence.VolumeSettings;

public class AudioManagerHelper {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(AudioManagerHelper.class.getName());

    private AudioManager audioManager;

    private int maxAlarmVolume;
    private int maxMediaVolume;
    private int maxRingtoneVolume;
    private int maxNotificationVolume;

    public AudioManagerHelper(AudioManager audioManager) {
        this.audioManager = audioManager;
        determineMaxVolumes();
    }

    public boolean adjustVolume(VolumeSettings volumeSettings) {
        LOG.debug("Adjust volume settings to: {}", volumeSettings);

        if (audioManager.isVolumeFixed()) {
            LOG.warn("Volume is fixed and cannot be modified programmatically!");
        }

        if (volumeSettings != null) {
            setMediaVolume(volumeSettings.getMediaVolume());
            setAlarmVolume(volumeSettings.getAlarmVolume());
            setRingtoneVolume(volumeSettings.getRingtoneVolume());
            setNotificationVolume(volumeSettings.getNotificationVolume());
            setRingerMode(RingtoneModeHelper.translateToAudioManagerRingerMode(volumeSettings.getRingtoneMode()));

            return true;
        } else {
            LOG.warn("VolumeSettings object is null!");
            return false;
        }
    }

    public VolumeSettings getCurrentVolumeSettings() {
        return new VolumeSettings(
                audioManager.getStreamVolume(AudioManager.STREAM_ALARM),
                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
                audioManager.getStreamVolume(AudioManager.STREAM_RING),
                audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION),
                RingtoneModeHelper.translateToRingtoneMode(audioManager.getRingerMode())
        );
    }

    private void determineMaxVolumes() {
        maxAlarmVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        maxMediaVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        maxRingtoneVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        maxNotificationVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
    }

    private void setAlarmVolume(int volume) {
        int calculatedVolume = calculateVolume(maxAlarmVolume, volume);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);

        LOG.debug("Current alarm volume is: {}", currentVolume);
        LOG.debug("Set alarm volume to: {} / {}", calculatedVolume, maxAlarmVolume);

        setVolume(AudioManager.STREAM_ALARM, currentVolume, calculatedVolume);
    }

    private void setMediaVolume(int volume) {
        int calculatedVolume = calculateVolume(maxMediaVolume, volume);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        LOG.debug("Current music volume is: {}", currentVolume);
        LOG.debug("Set media volume to: {} / {}", calculatedVolume, maxMediaVolume);

        setVolume(AudioManager.STREAM_MUSIC, currentVolume, calculatedVolume);
    }

    private void setRingtoneVolume(int volume) {
        int calculatedVolume = calculateVolume(maxRingtoneVolume, volume);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);

        LOG.debug("Current ringtone volume is: {}", currentVolume);
        LOG.debug("Set ringtone volume to: {} / {}", calculatedVolume, maxRingtoneVolume);

        setVolume(AudioManager.STREAM_RING, currentVolume, calculatedVolume);
    }

    private void setNotificationVolume(int volume) {
        int calculatedVolume = calculateVolume(maxNotificationVolume, volume);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);

        LOG.debug("Current notification volume is: {}", currentVolume);
        LOG.debug("Set notification volume to: {} / {}", calculatedVolume, maxNotificationVolume);

        setVolume(AudioManager.STREAM_NOTIFICATION, currentVolume, calculatedVolume);
    }

    private void setVolume(int stream, int currentVolume, int newVolume) {
        if (newVolume == 0) {
            if (AndroidUtils.getBuildVersion() >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(stream, AudioManager.ADJUST_MUTE, 0);
            } else {
                audioManager.setStreamMute(stream, true);
            }

            return;
        }

        if (currentVolume == 0) {
            if (AndroidUtils.getBuildVersion() >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(stream, AudioManager.ADJUST_UNMUTE, 0);
            } else {
                audioManager.setStreamMute(stream, false);
            }
        }

        if (newVolume > currentVolume) {
            audioManager.setStreamVolume(stream, newVolume, 0);
        } else {
            audioManager.setStreamVolume(stream, newVolume, 0);
        }
    }

    private void setRingerMode(int ringerMode) {
        LOG.debug("Set ringer mode to: {}", ringerMode);
        audioManager.setRingerMode(ringerMode);
    }

    private int calculateVolume(int max, int volume) {
        double ratio = (double) volume / 10;
        return (int) Math.ceil(max * ratio);
    }
}
