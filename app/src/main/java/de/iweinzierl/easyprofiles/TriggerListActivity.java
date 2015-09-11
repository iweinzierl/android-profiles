package de.iweinzierl.easyprofiles;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import de.iweinzierl.easyprofiles.fragments.TriggerListFragment;
import de.iweinzierl.easyprofiles.persistence.Trigger;

public class TriggerListActivity extends Activity {

    private TriggerListFragment triggerListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_list);

        triggerListFragment = new TriggerListFragment();

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

    private void updateTriggerList() {
        List<Trigger> triggers = Trigger.listAll(Trigger.class);
        Log.d("easyprofiles", "Found " + triggers.size() + " triggers");

        triggerListFragment.setTriggers(triggers);
    }
}
