package de.iweinzierl.easyprofiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.iweinzierl.easyprofiles.fragments.EditProfileFragment;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.VolumeSettings;
import de.iweinzierl.easyprofiles.util.AudioManagerHelper;

public class EditProfileActivity extends Activity implements EditProfileFragment.Callback {

    public static final String EXTRA_PROFILE_ID = "extra.profile.id";

    private EditProfileFragment editProfileFragment;
    private Profile initProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editProfileFragment = new EditProfileFragment();
        getFragmentManager().beginTransaction().replace(R.id.edit_profile_fragment, editProfileFragment).commit();

        if (getActionBar() != null) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent caller = getIntent();
        if (caller != null) {
            long profileId = caller.getLongExtra(EXTRA_PROFILE_ID, 0);
            initProfile = Profile.findById(Profile.class, profileId);

            if (initProfile != null) {
                Log.d("easyprofiles", "Init activity with profile: " + initProfile);
                setTitle(initProfile.getName());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (initProfile != null) {
            editProfileFragment.setProfile(initProfile);
        } else {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            VolumeSettings volumeSettings = new AudioManagerHelper(audioManager).getCurrentVolumeSettings();

            Profile currentProfile = new Profile();
            currentProfile.setVolumeSettings(volumeSettings);

            editProfileFragment.setProfile(currentProfile);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteProfile();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onSaveProfile(Profile profile) {
        profile.save();
        if (profile.getId() > 0) {
            Log.d("easyprofile", "Successfully persisted profile: " + profile);
            finish();
        } else {
            Log.w("easyprofiles", "Persistence of profile failed!");
        }
    }

    private void deleteProfile() {
        if (initProfile != null) {
            Profile.deleteAll(Profile.class, "id = ?", String.valueOf(initProfile.getId()));
            Toast.makeText(this, R.string.editprofile_info_delete_successful, Toast.LENGTH_LONG).show();
        }

        finish();
    }
}
