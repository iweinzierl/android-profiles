package de.iweinzierl.easyprofiles;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.io.Serializable;

@EActivity
public class PickLocationActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    public static class Result implements Serializable {
        protected String name;
        protected double lat;
        protected double lon;
        protected int radius;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }
    }

    public static final int REQUEST_LOCATION_WITH_RADIUS = 100;

    public static final String EXTRA_LOCATION_RESULT = "PickLocationActivity.EXTRA_LOCATION_RESULT";

    public static final int PERMISSION_REQUEST_LOCATION = 100;
    public static final int PERMISSION_REQUEST_ZOOM_TO_CURRENT_LOCATION = 200;

    public static final int INITIAL_RADIUS = 100;

    @App
    protected EasyProfilesApp application;

    @ViewById(R.id.map)
    protected MapView map;

    @ViewById(R.id.radius)
    protected SeekBar radius;

    @ViewById(R.id.name)
    protected EditText nameView;

    @ViewById(R.id.radius_value)
    protected TextView radiusView;

    @StringRes(R.string.activity_pick_location_label_radius)
    protected String radiusLabel;

    private GoogleMap googleMap;
    private Circle circle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pick_location;
    }

    @AfterViews
    protected void setupMap() {
        map.onCreate(null);
        map.getMapAsync(this);
    }

    @AfterViews
    protected void setupRadius() {
        radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateRadiusValueText(progress);
                updateCircleRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        radius.setProgress(INITIAL_RADIUS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapClickListener(this);

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.fillColor(R.color.ColorSecondary);
        circleOptions.radius(INITIAL_RADIUS);
        circleOptions.center(new LatLng(90, 90));

        this.circle = this.googleMap.addCircle(circleOptions);

        enableMyLocation();
        zoomToCurrentLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION:
                enableMyLocation();
                break;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        circle.setCenter(latLng);
    }

    @Click(R.id.finish)
    protected void finishLocation() {
        String name = this.nameView.getText().toString();
        double lat = this.circle.getCenter().latitude;
        double lon = this.circle.getCenter().longitude;
        int radius = this.radius.getProgress();

        Result result = new Result();
        result.setName(name);
        result.setLat(lat);
        result.setLon(lon);
        result.setRadius(radius);

        Intent data = new Intent();
        data.putExtra(EXTRA_LOCATION_RESULT, result);

        setResult(RESULT_OK, data);
        finish();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkLocationPermission(int requestCode) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    requestCode);
            return false;
        }

        return true;
    }

    private void enableMyLocation() {
        if (checkLocationPermission(PERMISSION_REQUEST_LOCATION)) {
            this.googleMap.setMyLocationEnabled(true);
        }
    }

    private void zoomToCurrentLocation() {
        if (checkLocationPermission(PERMISSION_REQUEST_ZOOM_TO_CURRENT_LOCATION)) {
            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(EasyProfilesApp.getGoogleApiClient());

            if (lastLocation != null) {
                LatLng latlon = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, 16));
            }
        }
    }

    private void updateRadiusValueText(int radius) {
        radiusView.setText(String.format(radiusLabel, String.valueOf(radius)));
    }

    private void updateCircleRadius(int radius) {
        if (circle != null) {
            circle.setRadius(radius);
        }
    }
}
