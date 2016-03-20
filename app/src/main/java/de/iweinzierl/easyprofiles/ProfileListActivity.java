package de.iweinzierl.easyprofiles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.orm.SugarRecord;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;

import java.util.List;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.adapter.ModifiableProfileCardAdapter;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.util.ProfileActivator;

@EActivity
public class ProfileListActivity extends BaseActivity {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(ProfileListActivity.class.getName());

    @ViewById(R.id.profile_list)
    protected RecyclerView profileListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_profilelist);

        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.addProfileButton);
        actionButton.setOnClickListener(new OnAddProfileClickListener(this));
    }

    @AfterViews
    protected void setupProfileList() {
        profileListView.setHasFixedSize(false);
        profileListView.setLayoutManager(new LinearLayoutManager(this));

        final ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView profileListView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ModifiableProfileCardAdapter.ViewHolder holder = (ModifiableProfileCardAdapter.ViewHolder) viewHolder;
                Profile profile = holder.getProfile();

                removeProfile(profile);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(profileListView);
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

    @Background
    protected void removeProfile(Profile profile) {
        if (SugarRecord.delete(profile)) {
            showProfileRemovedHint(profile);
        }
    }

    @UiThread
    protected void showProfileRemovedHint(Profile profile) {
        Snackbar.make(
                profileListView,
                "Deleted profile: " + profile.toString(),
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Background
    protected void updateProfileList() {
        final List<Profile> profiles = SugarRecord.listAll(Profile.class);
        LOG.debug("Found {} profiles in database", profiles.size());

        setProfiles(profiles, true);
    }

    @UiThread
    protected void setProfiles(List<Profile> profiles, boolean modifiable) {
        ModifiableProfileCardAdapter profileCardAdapter = new ModifiableProfileCardAdapter(this, null);
        profileCardAdapter.setOnItemClickListener(new OnProfileClickListener());
        profileCardAdapter.setOnItemEditListener(new OnProfileEditListener());
        profileListView.setAdapter(profileCardAdapter);
        profileCardAdapter.setItems(profiles);
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

    private class OnProfileClickListener implements ModifiableProfileCardAdapter.OnItemClickListener {
        @Override
        public void onItemClick(Profile profile) {
            if (profile != null) {
                new ProfileActivator(ProfileListActivity.this).activate(profile);
                Snackbar.make(
                        profileListView,
                        "Activated profile: " + profile.getName(),
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private class OnProfileEditListener implements ModifiableProfileCardAdapter.OnItemEditListener {
        @Override
        public void onItemEdit(Profile profile) {
            Intent intent = new Intent(ProfileListActivity.this, EditProfileActivity.class);
            intent.putExtra(EditProfileActivity.EXTRA_PROFILE_ID, profile.getId());
            startActivity(intent);
        }
    }
}
