package de.iweinzierl.easyprofiles;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.orm.SugarRecord;
import com.software.shell.fab.ActionButton;

import org.slf4j.Logger;

import java.util.List;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.TriggerBuilder;
import de.iweinzierl.easyprofiles.fragments.TriggerListFragment;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class TriggerListActivity extends BaseActivity implements TriggerListFragment.Callback {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(TriggerListActivity.class.getName());

    private static final int REQUEST_PICK_TRIGGERTYPE = 100;
    private static final int REQUEST_PICK_WIFI = 200;
    private static final int REQUEST_PICK_LOCATION = 300;
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
            onWifiTriggerCreated();
        } else if (requestCode == REQUEST_PICK_TIME_SETTINGS) {
            onTimeSettingsSelected();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trigger_list;
    }

    @Override
    public void onTriggerEnabled(PersistentTrigger persistentTrigger) {
        persistentTrigger.setEnabled(true);
        SugarRecord.save(persistentTrigger);
    }

    @Override
    public void onTriggerDisabled(PersistentTrigger persistentTrigger) {
        persistentTrigger.setEnabled(false);
        SugarRecord.save(persistentTrigger);
    }

    @Override
    public void onTriggerRemoved(PersistentTrigger persistentTrigger) {
        SugarRecord.delete(persistentTrigger);
        updateTriggerList();
    }

    private void updateTriggerList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final List<PersistentTrigger> persistentTriggers = SugarRecord.listAll(PersistentTrigger.class);
                LOG.debug("Found {} triggers", persistentTriggers.size());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        triggerListFragment.setTriggers(persistentTriggers);
                    }
                });

                return null;
            }
        }.execute();
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
                startActivityForResult(new Intent(this, WifiTriggerActivity.class), REQUEST_PICK_WIFI);
                break;
            case TIME_BASED:
                triggerBuilder.setTriggerType(TriggerType.TIME_BASED);
                startActivityForResult(new Intent(this, ProfileSchedulerActivity.class), REQUEST_PICK_TIME_SETTINGS);
                break;
            case LOCATION_BASED:
                triggerBuilder.setTriggerType(TriggerType.LOCATION_BASED);
                startActivityForResult(new Intent(this, LocationTriggerActivity.class), REQUEST_PICK_LOCATION);
        }
    }

    private void onWifiTriggerCreated() {
        updateTriggerList();
    }

    private void onTimeSettingsSelected() {
        triggerBuilder = null;
        updateTriggerList();
    }
}
