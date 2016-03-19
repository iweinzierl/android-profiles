package de.iweinzierl.easyprofiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orm.SugarRecord;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import de.iweinzierl.easyprofiles.adapter.LocationsCardAdapter;
import de.iweinzierl.easyprofiles.persistence.Location;

@EActivity
public class LocationListActivity extends BaseActivity {

    @ViewById(R.id.locations)
    protected RecyclerView locationsView;

    protected LocationsCardAdapter locationsAdapter = new LocationsCardAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Click(R.id.addButton)
    protected void addNewLocation() {
        startActivityForResult(
                new Intent(this, PickLocationActivity_.class),
                PickLocationActivity.REQUEST_LOCATION_WITH_RADIUS);
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
        List<Location> locations = SugarRecord.listAll(Location.class);
        setLocations(locations);
    }

    @UiThread
    protected void setLocations(List<Location> locations) {
        locationsAdapter.setItems(locations);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PickLocationActivity.REQUEST_LOCATION_WITH_RADIUS && resultCode == RESULT_OK) {
            PickLocationActivity.Result result =
                    (PickLocationActivity.Result) data.getSerializableExtra(PickLocationActivity.EXTRA_LOCATION_RESULT);

            saveNewLocation(result);
        }
    }

    @Background
    protected void saveNewLocation(PickLocationActivity.Result result) {
        Location location = new Location(
                result.getName(),
                result.getLat(),
                result.getLon(),
                result.getRadius());

        SugarRecord.saveInTx(location);
        updateLocations();
    }
}
