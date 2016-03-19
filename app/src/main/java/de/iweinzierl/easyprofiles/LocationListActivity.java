package de.iweinzierl.easyprofiles;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orm.SugarRecord;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.iweinzierl.easyprofiles.adapter.LocationsCardAdapter;
import de.iweinzierl.easyprofiles.domain.LocationBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.Location;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

@EActivity
public class LocationListActivity extends BaseActivity {

    @ViewById(R.id.locations)
    protected RecyclerView locationsView;

    protected LocationsCardAdapter locationsAdapter = new LocationsCardAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @AfterViews
    protected void setupLocationsView() {
        locationsView.setHasFixedSize(false);
        locationsView.setLayoutManager(new LinearLayoutManager(this));
        locationsView.setAdapter(locationsAdapter);

        updateLocations();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location_list;
    }

    @Background
    protected void updateLocations() {
        // TODO fetch correct locations here!!
        List<PersistentTrigger> triggers = SugarRecord.find(
                PersistentTrigger.class,
                "type = ?",
                TriggerType.LOCATION_BASED.name());

        List<Location> locs = new ArrayList<>();

        for (PersistentTrigger trigger : triggers) {
            LocationBasedTrigger locTrigger = new LocationBasedTrigger();
            locTrigger.apply(trigger);

            locs.add(new Location("Fake", locTrigger.getLat(), locTrigger.getLon(), locTrigger.getRadius()));
        }

        setLocations(locs);
    }

    @UiThread
    protected void setLocations(List<Location> locations) {
        locationsAdapter.setItems(locations);
    }
}
