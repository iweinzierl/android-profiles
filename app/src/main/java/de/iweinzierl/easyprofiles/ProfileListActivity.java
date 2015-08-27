package de.iweinzierl.easyprofiles;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import de.iweinzierl.easyprofiles.fragments.ProfileListFragment;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.util.AudioManagerHelper;

public class ProfileListActivity extends AppCompatActivity implements ProfileListFragment.Callback {

    private ProfileListFragment profileListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        profileListFragment = new ProfileListFragment();
        getFragmentManager().beginTransaction().replace(R.id.profile_list_fragment, profileListFragment).commit();
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
            new AudioManagerHelper(audioManager).adjustVolume(profile.getVolumeSettings());
            Toast.makeText(this, "Profile " + profile.getName() + " successfully set.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProfileModify(Profile profile) {
        // TODO
        Toast.makeText(this, "Profile Modification Currently Not Implemented!", Toast.LENGTH_LONG).show();
    }

    private void updateProfileList() {
        List<Profile> profiles = Profile.listAll(Profile.class);
        Log.d("easyprofiles", "Found " + profiles.size() + " profiles in database");

        profileListFragment.setProfiles(profiles);
    }
}
