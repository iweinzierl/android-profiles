package de.iweinzierl.easyprofiles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import java.util.List;

import de.iweinzierl.easyprofiles.fragments.TriggerListFragment;
import de.iweinzierl.easyprofiles.persistence.Trigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;
import de.iweinzierl.easyprofiles.util.TriggerBuilder;

public class TriggerListActivity extends BaseActivity implements TriggerListFragment.Callback {

    private static final int REQUEST_PICK_TRIGGERTYPE = 100;
    private static final int REQUEST_PICK_WIFI = 200;
    private static final int REQUEST_PICK_PROFILE = 300;

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

    @Override
    public void onTriggerRemoved(Trigger trigger) {
        trigger.delete();
        updateTriggerList();
    }

    private void updateTriggerList() {
        List<Trigger> triggers = Trigger.listAll(Trigger.class);
        Log.d("easyprofiles", "Found " + triggers.size() + " triggers");

        triggerListFragment.setTriggers(triggers);
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
                Toast.makeText(this, "TIME BASED NOT IMPLEMENTED", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void onProfileSelected(Intent data) {
        long profileId = data.getLongExtra(ProfileSelectionListActivity.EXTRA_PROFILE_ID, 0);
        triggerBuilder.setOnActivateProfile(profileId);

        triggerBuilder.build().save();
        triggerBuilder = null;
        updateTriggerList();
    }

    private void onWifiSelected(Intent data) {
        String ssid = data.getStringExtra(WifiSelectionListActivity.EXTRA_WIFI_SSID);
        triggerBuilder.setData(ssid);

        startActivityForResult(new Intent(this, ProfileSelectionListActivity.class), REQUEST_PICK_PROFILE);
    }
}
