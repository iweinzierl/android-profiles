package de.iweinzierl.easyprofiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.orm.SugarRecord;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.adapter.TriggerCardAdapter;
import de.iweinzierl.easyprofiles.domain.LocationBasedTrigger;
import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;
import de.iweinzierl.easyprofiles.domain.Trigger;
import de.iweinzierl.easyprofiles.domain.TriggerBuilder;
import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;
import de.iweinzierl.easyprofiles.widget.recyclerview.CardItemRemoveListener;

@EActivity
public class TriggerListActivity extends BaseActivity {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(TriggerListActivity.class.getName());

    private static final int REQUEST_PICK_TRIGGERTYPE = 100;
    private static final int REQUEST_PICK_WIFI = 200;
    private static final int REQUEST_PICK_LOCATION = 300;
    private static final int REQUEST_PICK_TIME_SETTINGS = 400;

    @ColorRes(R.color.CardViewDeleteBackground)
    protected int cardViewDeleteBackgroundColor;

    @ColorRes(R.color.CardViewDeleteText)
    protected int cardViewDeleteTextColor;

    @StringRes(R.string.CardViewDelete)
    protected String cardViewDeleteMessage;

    @ViewById(R.id.trigger_list)
    protected RecyclerView triggerListView;

    private TriggerBuilder triggerBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_triggerlist);
    }

    @AfterViews
    protected void setupTriggerList() {
        triggerListView.setLayoutManager(new LinearLayoutManager(this));
        triggerListView.setHasFixedSize(false);

        final ItemTouchHelper.SimpleCallback callback = new CardItemRemoveListener(
                triggerListView,
                cardViewDeleteBackgroundColor,
                cardViewDeleteTextColor,
                cardViewDeleteMessage);

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(triggerListView);
    }

    @Click(R.id.addTriggerButton)
    protected void addTriggerClicked() {
        triggerBuilder = new TriggerBuilder();
        startActivityForResult(new Intent(this, TriggerTypeSelectionActivity.class), REQUEST_PICK_TRIGGERTYPE);
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

    @UiThread
    protected void showMessage(String message) {
        Snackbar.make(
                triggerListView,
                message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Background
    protected void enableTrigger(final PersistentTrigger persistentTrigger) {
        persistentTrigger.setEnabled(true);
        SugarRecord.save(persistentTrigger);

        showMessage("Trigger enabled: " + persistentTrigger.getType().name());
    }

    @Background
    protected void disableTrigger(final PersistentTrigger persistentTrigger) {
        persistentTrigger.setEnabled(false);
        SugarRecord.save(persistentTrigger);

        showMessage("Trigger disabled: " + persistentTrigger.getType().name());
    }

    @Background
    protected void removeTrigger(final PersistentTrigger persistentTrigger) {
        SugarRecord.delete(persistentTrigger);
        updateTriggerList();

        showMessage("Removed trigger: " + persistentTrigger.getType().name());
    }

    @Background
    protected void updateTriggerList() {
        final List<PersistentTrigger> persistentTriggers = SugarRecord.listAll(PersistentTrigger.class);
        LOG.debug("Found {} triggers", persistentTriggers.size());

        setTriggers(persistentTriggers);
    }

    @UiThread
    protected void setTriggers(List<PersistentTrigger> triggers) {
        final TriggerCardAdapter adapter = new TriggerCardAdapter(
                this,
                new ArrayList<>(Collections2.transform(triggers, new Function<PersistentTrigger, Trigger>() {
                            @Override
                            public Trigger apply(@Nullable PersistentTrigger input) {
                                if (input == null) {
                                    return null;
                                }

                                switch (input.getType()) {
                                    case LOCATION_BASED:
                                        LocationBasedTrigger lt = new LocationBasedTrigger();
                                        lt.apply(input);
                                        return lt;
                                    case TIME_BASED:
                                        TimeBasedTrigger tt = new TimeBasedTrigger();
                                        tt.apply(input);
                                        return tt;
                                    case WIFI:
                                        WifiBasedTrigger wt = new WifiBasedTrigger();
                                        wt.apply(input);
                                        return wt;
                                }

                                return null;
                            }
                        }
                )));

        adapter.setOnItemClickListener(new TriggerCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Trigger trigger) {
                if (trigger.isEnabled()) {
                    disableTrigger(trigger.export());
                } else {
                    enableTrigger(trigger.export());
                }
            }
        });

        adapter.setOnItemRemoveListener(new TriggerCardAdapter.OnItemRemoveListener() {
            @Override
            public void onItemRemove(Trigger trigger) {
                removeTrigger(trigger.export());
            }
        });

        triggerListView.setAdapter(adapter);
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
