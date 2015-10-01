package de.iweinzierl.easyprofiles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.software.shell.fab.ActionButton;

import java.util.List;

import de.iweinzierl.easyprofiles.domain.TriggerBuilder;
import de.iweinzierl.easyprofiles.fragments.TriggerListFragment;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class TriggerListActivity extends BaseActivity implements TriggerListFragment.Callback {

    private static final int REQUEST_PICK_TRIGGERTYPE = 100;
    private static final int REQUEST_PICK_WIFI = 200;
    private static final int REQUEST_PICK_PROFILE = 300;
    private static final int REQUEST_PICK_TIME_SETTINGS = 400;

    private TriggerListFragment triggerListFragment;
    private TriggerBuilder triggerBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_triggerlist);

        triggerListFragment = new TriggerListFragment();

        ActionButton actionButton = (ActionButton) findViewById(R.id.addTriggerButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddTriggerClicked();
            }
        });

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.trigger_list_fragment, triggerListFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTriggerList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            triggerBuilder = null;
        } else if (requestCode == REQUEST_PICK_TRIGGERTYPE) {
            onTriggerTypeSelected(data);
        } else if (requestCode == REQUEST_PICK_WIFI) {
            onWifiSelected(data);
        } else if (requestCode == REQUEST_PICK_PROFILE) {
            onProfileSelected(data);
        } else if (requestCode == REQUEST_PICK_TIME_SETTINGS) {
            onTimeSettingsSelected(data);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trigger_list;
    }

    @Override
    public void onTriggerEnabled(PersistentTrigger persistentTrigger) {
        persistentTrigger.setEnabled(true);
        persistentTrigger.save();
    }

    @Override
    public void onTriggerDisabled(PersistentTrigger persistentTrigger) {
        persistentTrigger.setEnabled(false);
        persistentTrigger.save();
    }

    @Override
    public void onTriggerRemoved(PersistentTrigger persistentTrigger) {
        persistentTrigger.delete();
        updateTriggerList();
    }

    private void updateTriggerList() {
        List<PersistentTrigger> persistentTriggers = PersistentTrigger.listAll(PersistentTrigger.class);
        Log.d("easyprofiles", "Found " + persistentTriggers.size() + " triggers");

        triggerListFragment.setTriggers(persistentTriggers);
    }

    private void onAddTriggerClicked() {
        triggerBuilder = new TriggerBuilder();
        startActivityForResult(new Intent(this, TriggerTypeSelectionActivity.class), REQUEST_PICK_TRIGGERTYPE);
    }

    private void onTriggerTypeSelected(Intent data) {
        String triggerTypeName = data.getStringExtra(TriggerTypeSelectionActivity.EXTRA_TRIGGER_TYPE);
        TriggerType triggerType = TriggerType.valueOf(triggerTypeName);

        switch (triggerType) {
            case WIFI:
                triggerBuilder.setTriggerType(TriggerType.WIFI);
                startActivityForResult(new Intent(this, WifiSelectionListActivity.class), REQUEST_PICK_WIFI);
                break;
            case TIME_BASED:
                triggerBuilder.setTriggerType(TriggerType.TIME_BASED);
                startActivityForResult(new Intent(this, TimeTriggerSettingsActivity.class), REQUEST_PICK_TIME_SETTINGS);
                break;
        }
    }

    private void onProfileSelected(Intent data) {
        Profile profileId = Profile.findById(Profile.class, data.getLongExtra(ProfileSelectionListActivity.EXTRA_PROFILE_ID, 0));
        triggerBuilder.setOnActivateProfile(profileId);

        triggerBuilder.build().export().save();
        triggerBuilder = null;
        updateTriggerList();
    }

    private void onWifiSelected(Intent data) {
        String ssid = data.getStringExtra(WifiSelectionListActivity.EXTRA_WIFI_SSID);
        triggerBuilder.setData(ssid);

        startActivityForResult(new Intent(this, ProfileSelectionListActivity.class), REQUEST_PICK_PROFILE);
    }

    private void onTimeSettingsSelected(Intent data) {
        String timeTriggerData = data.getStringExtra(TimeTriggerSettingsActivity.EXTRA_TIME_TRIGGER_DATA);
        triggerBuilder.setData(timeTriggerData);

        startActivityForResult(new Intent(this, ProfileSelectionListActivity.class), REQUEST_PICK_PROFILE);
    }
}
