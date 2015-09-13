package de.iweinzierl.easyprofiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toolbar;

import java.util.List;

import de.iweinzierl.easyprofiles.fragments.ProfileListFragment;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileSelectionListActivity extends Activity implements ProfileListFragment.Callback {

    public static final String EXTRA_PROFILE_ID = "extra.profile.id";
    public static final String EXTRA_PROFILE_NAME = "extra.profile.name";

    private ProfileListFragment profileListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        if (toolbar != null) {
            setActionBar(toolbar);
            toolbar.setTitle(R.string.activity_profile_selection);
            setTitle(R.string.activity_profile_selection);
        } else {
            setTitle(R.string.activity_profile_selection);
        }

        profileListFragment = new ProfileListFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.profile_list_fragment, profileListFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<Profile> profiles = Profile.listAll(Profile.class);
        profileListFragment.setProfiles(profiles, false);
    }

    @Override
    public void onProfileClick(Profile profile) {
        Intent data = new Intent();
        data.putExtra(EXTRA_PROFILE_NAME, profile.getName());
        data.putExtra(EXTRA_PROFILE_ID, profile.getId());

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onProfileModify(Profile profile) {
    }
}
