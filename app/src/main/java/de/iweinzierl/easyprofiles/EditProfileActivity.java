package de.iweinzierl.easyprofiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import de.iweinzierl.easyprofiles.fragments.EditProfileFragment;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.Trigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;
import de.iweinzierl.easyprofiles.persistence.VolumeSettings;
import de.iweinzierl.easyprofiles.util.AudioManagerHelper;
import de.iweinzierl.easyprofiles.widget.validation.ValidationError;

public class EditProfileActivity extends Activity {

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

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        toolbarTop.inflateMenu(R.menu.menu_edit_profile);
        toolbarTop.setNavigationIcon(R.drawable.ic_arrow_back_black_36dp);
        setActionBar(toolbarTop);

        Intent caller = getIntent();
        if (caller != null) {
            long profileId = caller.getLongExtra(EXTRA_PROFILE_ID, 0);
            initProfile = Profile.findById(Profile.class, profileId);

            if (initProfile != null) {
                Log.d("easyprofiles", "Init activity with profile: " + initProfile);
                setTitle(initProfile.getName());
                toolbarBottom.setVisibility(View.GONE);
            } else {
                toolbarBottom.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                toolbarBottom.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            onSaveProfile(editProfileFragment.getProfile());
                        } catch (ValidationError e) {
                            Log.w("easyprofiles", "Error while validating profile", e);
                        }
                    }
                });
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
    protected void onPause() {
        super.onPause();
        if (initProfile != null) {
            onSaveProfile(editProfileFragment.getProfile());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);

        if (initProfile == null) {
            MenuItem item = menu.findItem(R.id.delete);
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteProfile();
                return true;
            case R.id.trigger:
                // TODO REMOVE TRIGGER SELECTION AND REPLACE WITH TRIGGER SELECTION IN FRAGMENT AS SETTING
                startActivityForResult(
                        new Intent(this, WifiSelectionListActivity.class),
                        WifiSelectionListActivity.REQUEST_WIFI_SSID);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WifiSelectionListActivity.REQUEST_WIFI_SSID && resultCode == RESULT_OK) {
            String ssid = data.getStringExtra(WifiSelectionListActivity.EXTRA_WIFI_SSID);
            Log.d("easyprofiles", "Received selected wifi ssid: " + ssid);

            if (initProfile != null) {
                Trigger trigger = Trigger.of(TriggerType.WIFI, ssid, initProfile.getId());
                trigger.save();

                Toast.makeText(this, "Added WIFI trigger to profile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

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
