package de.iweinzierl.easyprofiles;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import de.iweinzierl.easyprofiles.fragments.TriggerListFragment;
import de.iweinzierl.easyprofiles.persistence.Trigger;

public class TriggerListActivity extends BaseActivity {

    private TriggerListFragment triggerListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_triggerlist);

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trigger_list;
    }

    private void updateTriggerList() {
        List<Trigger> triggers = Trigger.listAll(Trigger.class);
        Log.d("easyprofiles", "Found " + triggers.size() + " triggers");

        triggerListFragment.setTriggers(triggers);
    }
}
