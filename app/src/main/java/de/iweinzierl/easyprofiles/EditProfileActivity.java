package de.iweinzierl.easyprofiles;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import de.iweinzierl.easyprofiles.fragments.EditProfileFragment;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class EditProfileActivity extends AppCompatActivity implements EditProfileFragment.Callback {

    private Fragment editProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editProfileFragment = new EditProfileFragment();
        getFragmentManager().beginTransaction().replace(R.id.edit_profile_fragment, editProfileFragment).commit();
    }

    @Override
    public void onSaveProfile(Profile profile) {
        profile.save();
        if (profile.getId() > 0) {
            Log.d("easyprofile", "Successfully persisted profile: " + profile);
            finish();
        }
        else {
            Log.w("easyprofiles", "Persistence of profile failed!");
        }
    }
}
