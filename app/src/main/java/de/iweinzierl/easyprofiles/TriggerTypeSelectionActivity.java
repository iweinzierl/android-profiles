package de.iweinzierl.easyprofiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import de.iweinzierl.easyprofiles.fragments.TriggerTypeListFragment;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class TriggerTypeSelectionActivity extends Activity implements TriggerTypeListFragment.Callback {

    public static final int REQUEST_CODE_TRIGGERTYPE = 100;

    public static final String EXTRA_TRIGGER_TYPE = "extra.triggertype";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_type_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        if (toolbar != null) {
            setActionBar(toolbar);
            toolbar.setTitle(R.string.activity_profile_selection);
            setTitle(R.string.activity_trigger_type_selection);
        } else {
            setTitle(R.string.activity_profile_selection);
        }

        TriggerTypeListFragment triggerTypeListFragment = new TriggerTypeListFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.trigger_type_fragment, triggerTypeListFragment)
                .commit();
    }

    @Override
    public void onTriggerTypeClicked(TriggerType triggerType) {
        Log.d("easyprofiles", "Clicked trigger type: " + triggerType);

        Intent data = new Intent();
        data.putExtra(EXTRA_TRIGGER_TYPE, triggerType.name());

        setResult(RESULT_OK, data);
        finish();
    }
}
