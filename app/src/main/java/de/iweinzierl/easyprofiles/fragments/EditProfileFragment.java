package de.iweinzierl.easyprofiles.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.VolumeSettings;
import de.iweinzierl.easyprofiles.widget.AbstractSettingsView;
import de.iweinzierl.easyprofiles.widget.SettingsViewEditText;

public class EditProfileFragment extends Fragment {

    public interface Callback {
        void onSaveProfile(Profile profile);
    }

    private static class InvalidInputException extends Exception {
        public InvalidInputException(String detailMessage) {
            super(detailMessage);
        }
    }

    private Callback callback;

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

        Button saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    try {
                        callback.onSaveProfile(getProfile());

                    } catch (InvalidInputException e) {
                        Log.w("easyprofiles", "Profile is invalid", e);
                    }
                }
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        name = (SettingsViewEditText) view.findViewById(R.id.profile_name);
        alarmVolume = (AbstractSettingsView<Integer>) view.findViewById(R.id.alarm_volume);
        mediaVolume = (AbstractSettingsView<Integer>) view.findViewById(R.id.media_volume);
        ringtoneVolume = (AbstractSettingsView<Integer>) view.findViewById(R.id.ringtone_volume);
        notificationVolume = (AbstractSettingsView<Integer>) view.findViewById(R.id.notification_volume);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof Callback) {
            callback = (Callback) activity;
        }
    }

    public Profile getProfile() throws InvalidInputException {
        Profile profile = new Profile();
        profile.setName(name.getValue());
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

    private void validateVolumeSettings() throws InvalidInputException {
        if (alarmVolume.getValue() == null) {
            throw new InvalidInputException("Alarm volume not set!");
        }

        if (mediaVolume.getValue() == null) {
            throw new InvalidInputException("Media volume not set!");
        }

        if (ringtoneVolume.getValue() == null) {
            throw new InvalidInputException("Ringtone volume not set!");
        }

        if (notificationVolume.getValue() == null) {
            throw new InvalidInputException("Notification volume not set!");
        }
    }

    private VolumeSettings getVolumeSettings() throws InvalidInputException {
        validateVolumeSettings();

        VolumeSettings volumeSettings = new VolumeSettings();
        volumeSettings.setAlarmVolume(alarmVolume.getValue());
        volumeSettings.setMediaVolume(mediaVolume.getValue());
        volumeSettings.setRingtoneVolume(ringtoneVolume.getValue());
        volumeSettings.setNotificationVolume(notificationVolume.getValue());
        return volumeSettings;
    }
}
