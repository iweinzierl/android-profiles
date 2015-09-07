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
import android.widget.Toolbar;

import java.util.List;

import de.iweinzierl.easyprofiles.fragments.ProfileListFragment;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.util.AudioManagerHelper;
import de.iweinzierl.easyprofiles.util.NotificationHelper;

public class ProfileListActivity extends Activity implements ProfileListFragment.Callback {

    private ProfileListFragment profileListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        profileListFragment = new ProfileListFragment();
        getFragmentManager().beginTransaction().replace(R.id.profile_list_fragment, profileListFragment).commit();

        setTitle(R.string.activity_profilelist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProfileList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @Override
    public void onProfileClick(Profile profile) {
        if (profile != null) {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (new AudioManagerHelper(audioManager).adjustVolume(profile.getVolumeSettings())) {
                new NotificationHelper(this).publishProfileNotification(profile);
                Toast.makeText(this, "Profile '" + profile.getName() + "' successfully set.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Profile '" + profile.getName() + "' not set!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onProfileModify(Profile profile) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(EditProfileActivity.EXTRA_PROFILE_ID, profile.getId());
        startActivity(intent);
    }

    private void updateProfileList() {
        List<Profile> profiles = Profile.listAll(Profile.class);
        Log.d("easyprofiles", "Found " + profiles.size() + " profiles in database");

        profileListFragment.setProfiles(profiles);
    }
}
