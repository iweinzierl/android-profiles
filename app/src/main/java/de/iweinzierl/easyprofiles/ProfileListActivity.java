package de.iweinzierl.easyprofiles;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orm.SugarRecord;
import com.software.shell.fab.ActionButton;

import org.slf4j.Logger;

import java.util.List;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.animation.NotificationAnimation;
import de.iweinzierl.easyprofiles.fragments.ProfileListFragment;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.util.ProfileActivator;

public class ProfileListActivity extends BaseActivity implements ProfileListFragment.Callback {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(ProfileListActivity.class.getName());

    private ProfileListFragment profileListFragment;

    private TextView notificationBar;

    private NotificationAnimation notificationAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_profilelist);

        notificationBar = (TextView) findViewById(R.id.notification_bar);
        notificationAnimation = new NotificationAnimation(notificationBar, ((RelativeLayout.LayoutParams) notificationBar.getLayoutParams()).bottomMargin);

        ActionButton actionButton = (ActionButton) findViewById(R.id.addProfileButton);
        actionButton.setOnClickListener(new OnAddProfileClickListener(this));

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

        switch (id) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile_list;
    }

    @Override
    public void onProfileClick(Profile profile) {
        if (profile != null) {
            new ProfileActivator(this).activate(profile);

            // TODO i18n
            showNotification("Profile '" + profile.getName() + "' set");
        }
    }

    @Override
    public void onProfileModify(Profile profile) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(EditProfileActivity.EXTRA_PROFILE_ID, profile.getId());
        startActivity(intent);
    }

    private void updateProfileList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Profile> profiles = SugarRecord.listAll(Profile.class);
                LOG.debug("Found {} profiles in database", profiles.size());

                profileListFragment.setProfiles(profiles, true);

                return null;
            }
        }.execute();
    }

    private void showNotification(String notificationText) {
        notificationBar.setText(notificationText);
        notificationAnimation.start();
    }

    private static class OnAddProfileClickListener implements View.OnClickListener {

        private Context context;

        public OnAddProfileClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            LOG.debug("Clicked to add new profile");
            context.startActivity(new Intent(context, EditProfileActivity.class));
        }
    }
}
