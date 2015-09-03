package de.iweinzierl.easyprofiles.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.VolumeSettings;
import de.iweinzierl.easyprofiles.widget.AbstractSettingsView;
import de.iweinzierl.easyprofiles.widget.SettingsViewEditText;
import de.iweinzierl.easyprofiles.widget.validation.NotEmptyStringValidator;
import de.iweinzierl.easyprofiles.widget.validation.NotNullValidator;
import de.iweinzierl.easyprofiles.widget.validation.ValidationError;

public class EditProfileFragment extends Fragment {

    private Profile editProfile;

    private AbstractSettingsView<String> name;
    private AbstractSettingsView<Integer> alarmVolume;
    private AbstractSettingsView<Integer> mediaVolume;
    private AbstractSettingsView<Integer> ringtoneVolume;
    private AbstractSettingsView<Integer> notificationVolume;

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
        alarmVolume = (AbstractSettingsView<Integer>) view.findViewById(R.id.alarm_volume);
        mediaVolume = (AbstractSettingsView<Integer>) view.findViewById(R.id.media_volume);
        ringtoneVolume = (AbstractSettingsView<Integer>) view.findViewById(R.id.ringtone_volume);
        notificationVolume = (AbstractSettingsView<Integer>) view.findViewById(R.id.notification_volume);

        initValidators();
    }

    public Profile getProfile() throws ValidationError {
        Profile profile = new Profile();
        profile.setName(getName());
        profile.setVolumeSettings(getVolumeSettings());

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
        }
    }

    private void initValidators() {
        name.addValidator(new NotNullValidator<String>());
        name.addValidator(new NotEmptyStringValidator());
        alarmVolume.addValidator(new NotNullValidator<Integer>());
        mediaVolume.addValidator(new NotNullValidator<Integer>());
        ringtoneVolume.addValidator(new NotNullValidator<Integer>());
        notificationVolume.addValidator(new NotNullValidator<Integer>());
    }

    private String getName() throws ValidationError {
        return name.getValue();
    }

    private VolumeSettings getVolumeSettings() throws ValidationError {
        VolumeSettings volumeSettings = new VolumeSettings();
        volumeSettings.setAlarmVolume(alarmVolume.getValue());
        volumeSettings.setMediaVolume(mediaVolume.getValue());
        volumeSettings.setRingtoneVolume(ringtoneVolume.getValue());
        volumeSettings.setNotificationVolume(notificationVolume.getValue());
        return volumeSettings;
    }
}
