package de.iweinzierl.easyprofiles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import de.iweinzierl.easyprofiles.fragments.ProfileListFragment;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileListActivity extends AppCompatActivity {

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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateProfileList() {
        List<Profile> profiles = Profile.listAll(Profile.class);
        Log.d("easyprofiles", "Found " + profiles.size() + " profiles in database");

        profileListFragment.setProfiles(profiles);
    }
}
