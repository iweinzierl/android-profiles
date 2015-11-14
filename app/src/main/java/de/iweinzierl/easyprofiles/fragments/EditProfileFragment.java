package de.iweinzierl.easyprofiles.fragments;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.inselhome.android.utils.UiUtils;
import de.inselhome.android.utils.audio.AudioUtils;
import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.RingtoneMode;
import de.iweinzierl.easyprofiles.persistence.VolumeSettings;
import de.iweinzierl.easyprofiles.util.RingtoneModeHelper;
import de.iweinzierl.easyprofiles.widget.AbstractSettingsView;
import de.iweinzierl.easyprofiles.widget.SettingsViewEditText;
import de.iweinzierl.easyprofiles.widget.SettingsViewSingleOptions;
import de.iweinzierl.easyprofiles.widget.SettingsViewSlider;
import de.iweinzierl.easyprofiles.widget.validation.NotEmptyStringValidator;
import de.iweinzierl.easyprofiles.widget.validation.NotNullValidator;
import de.iweinzierl.easyprofiles.widget.validation.ValidationError;

public class EditProfileFragment extends Fragment {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(EditProfileFragment.class.getName());

    private Profile editProfile;

    private AbstractSettingsView<String> name;
    private SettingsViewSlider alarmVolume;
    private SettingsViewSlider mediaVolume;
    private SettingsViewSlider ringtoneVolume;
    private SettingsViewSlider notificationVolume;
    private AbstractSettingsView<SettingsViewSingleOptions.Option> ringtoneMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = (SettingsViewEditText) view.findViewById(R.id.profile_name);
        alarmVolume = (SettingsViewSlider) view.findViewById(R.id.alarm_volume);
        mediaVolume = (SettingsViewSlider) view.findViewById(R.id.media_volume);
        ringtoneVolume = (SettingsViewSlider) view.findViewById(R.id.ringtone_volume);
        notificationVolume = (SettingsViewSlider) view.findViewById(R.id.notification_volume);
        ringtoneMode = (AbstractSettingsView<SettingsViewSingleOptions.Option>) view.findViewById(R.id.ringtone_mode);

        AudioManager audioManager = getAudioManager();
        alarmVolume.setMaxValue(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        mediaVolume.setMaxValue(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        ringtoneVolume.setMaxValue(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        notificationVolume.setMaxValue(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));

        ((SettingsViewSingleOptions) ringtoneMode).setOptions(Lists.newArrayList(
                new SettingsViewSingleOptions.Option(RingtoneModeHelper.getRingtoneModeLabel(getActivity(), RingtoneMode.NORMAL), RingtoneMode.NORMAL),
                new SettingsViewSingleOptions.Option(RingtoneModeHelper.getRingtoneModeLabel(getActivity(), RingtoneMode.VIBRATE), RingtoneMode.VIBRATE),
                new SettingsViewSingleOptions.Option(RingtoneModeHelper.getRingtoneModeLabel(getActivity(), RingtoneMode.SILENT), RingtoneMode.SILENT)
        ));

        initValidators();
        fixRingtoneAndNotificationSettings();
    }

    public Profile getProfile() throws ValidationError {
        Profile profile = new Profile();
        profile.setName(getName());
        profile.setVolumeSettings(constructVolumeSettings());

        if (editProfile != null) {
            profile.setId(editProfile.getId());
        }

        return profile;
    }

    public void setProfile(Profile profile) {
        editProfile = profile;

        name.setValue(profile.getName());

        VolumeSettings volumeSettings = profile.getVolumeSettings();
        if (volumeSettings != null) {
            alarmVolume.setValue(volumeSettings.getAlarmVolume());
            mediaVolume.setValue(volumeSettings.getMediaVolume());
            ringtoneVolume.setValue(volumeSettings.getRingtoneVolume());
            notificationVolume.setValue(volumeSettings.getNotificationVolume());

            RingtoneMode rm = profile.getVolumeSettings().getRingtoneMode();
            this.ringtoneMode.setValue(new SettingsViewSingleOptions.Option(RingtoneModeHelper.getRingtoneModeLabel(getActivity(), rm), rm));
        }
    }

    private void initValidators() {
        name.addValidator(new NotNullValidator<String>());
        name.addValidator(new NotEmptyStringValidator());
        alarmVolume.addValidator(new NotNullValidator<Integer>());
        mediaVolume.addValidator(new NotNullValidator<Integer>());
        ringtoneVolume.addValidator(new NotNullValidator<Integer>());
        notificationVolume.addValidator(new NotNullValidator<Integer>());
        ringtoneMode.addValidator(new NotNullValidator<SettingsViewSingleOptions.Option>());
    }

    private void fixRingtoneAndNotificationSettings() {
        AudioManager audioManager = getAudioManager();

        if (AudioUtils.areRingerAndNotificationVolumeLinked(audioManager)) {
            LOG.info("Ringtone and Notifications are linked: hide notification settings view");
            ViewGroup.LayoutParams layoutParams = notificationVolume.getLayoutParams();
            layoutParams.height = 0;
            notificationVolume.setLayoutParams(layoutParams);

            LOG.info("Ringtone and Notifications are linked: remove notification divider");
            View divider = UiUtils.getView(getView(), R.id.notification_volume_devider);
            if (divider != null && divider.getParent() != null) {
                ((ViewGroup) divider.getParent()).removeView(divider);
            }

            LOG.info("Ringtone and Notifications are linked: modify label for ringtone settings view");
            String newLabel = getResources().getString(R.string.editprofile_ringtone_and_notification_volume);
            ringtoneVolume.setLabel(newLabel);
        }
    }

    private String getName() throws ValidationError {
        return name.getValue();
    }

    private VolumeSettings constructVolumeSettings() throws ValidationError {
        VolumeSettings volumeSettings = new VolumeSettings();
        volumeSettings.setAlarmVolume(alarmVolume.getValue());
        volumeSettings.setMediaVolume(mediaVolume.getValue());
        volumeSettings.setRingtoneVolume(ringtoneVolume.getValue());
        volumeSettings.setRingtoneMode((RingtoneMode) ringtoneMode.getValue().value);

        if (AudioUtils.areRingerAndNotificationVolumeLinked(getAudioManager())) {
            LOG.info("Ringtone and Notifications are linked: set ringtone volume as notification volume");
            volumeSettings.setNotificationVolume(ringtoneVolume.getValue());
        } else {
            volumeSettings.setNotificationVolume(notificationVolume.getValue());
        }
        return volumeSettings;
    }

    private AudioManager getAudioManager() {
        return (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
    }
}
