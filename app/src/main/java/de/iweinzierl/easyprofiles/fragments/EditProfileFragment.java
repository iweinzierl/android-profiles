package de.iweinzierl.easyprofiles.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
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

    private Callback callback;

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
                    callback.onSaveProfile(getProfile());
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

    public Profile getProfile() {
        VolumeSettings volumeSettings = new VolumeSettings();
        volumeSettings.setAlarmVolume(alarmVolume.getValue());
        volumeSettings.setMediaVolume(mediaVolume.getValue());
        volumeSettings.setRingtoneVolume(ringtoneVolume.getValue());
        volumeSettings.setNotificationVolume(notificationVolume.getValue());

        Profile profile = new Profile();
        profile.setName(name.getValue());
        profile.setVolumeSettings(volumeSettings);

        return profile;
    }
}
