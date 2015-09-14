package de.iweinzierl.easyprofiles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.software.shell.fab.ActionButton;

import java.util.List;

import de.iweinzierl.easyprofiles.fragments.TriggerListFragment;
import de.iweinzierl.easyprofiles.persistence.Trigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class TriggerListActivity extends BaseActivity implements TriggerListFragment.Callback {

    private static final int REQUEST_PICK_WIFI = 100;
    private static final int REQUEST_PICK_PROFILE = 200;

    private TriggerListFragment triggerListFragment;
    private Trigger newTriggerSetup;

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
            newTriggerSetup = null;
        } else if (requestCode == REQUEST_PICK_WIFI) {
            String ssid = data.getStringExtra(WifiSelectionListActivity.EXTRA_WIFI_SSID);
            newTriggerSetup.setType(TriggerType.WIFI);
            newTriggerSetup.setData(ssid);

            startActivityForResult(new Intent(this, ProfileSelectionListActivity.class), REQUEST_PICK_PROFILE);
        } else if (requestCode == REQUEST_PICK_PROFILE) {
            long profileId = data.getLongExtra(ProfileSelectionListActivity.EXTRA_PROFILE_ID, 0);
            newTriggerSetup.setProfileId(profileId);

            newTriggerSetup.save();
            newTriggerSetup = null;
            updateTriggerList();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trigger_list;
    }

    @Override
    public void onTriggerEnabled(Trigger trigger) {
        trigger.setEnabled(true);
        trigger.save();
    }

    @Override
    public void onTriggerDisabled(Trigger trigger) {
        trigger.setEnabled(false);
        trigger.save();
    }

    private void updateTriggerList() {
        List<Trigger> triggers = Trigger.listAll(Trigger.class);
        Log.d("easyprofiles", "Found " + triggers.size() + " triggers");

        triggerListFragment.setTriggers(triggers);
    }

    private void onAddTriggerClicked() {
        newTriggerSetup = new Trigger();

        // TODO bring up dialog to choose between trigger type
        startActivityForResult(new Intent(this, WifiSelectionListActivity.class), REQUEST_PICK_WIFI);
    }
}
