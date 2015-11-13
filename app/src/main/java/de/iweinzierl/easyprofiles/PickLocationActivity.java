package de.iweinzierl.easyprofiles;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.util.AndroidUtils;

public class PickLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(PickLocationActivity.class.getName());

    private static final int REQUEST_LOCATION_PERMISSION = 100;

    private GoogleMap mMap;
    private Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SeekBar seekBar = (SeekBar) findViewById(R.id.radius_bar);
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (circle != null) {
                    int newValue = seekBar.getProgress();
                    circle.setRadius(newValue);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (permissions.length == 0) {
                LOG.warn("Permission request was interrupted. Request again.");
                requestLocationPermission();
            }

            if (didUserGrantLocationPermission(permissions, grantResults)) {
                LOG.info("User granted location permissions");
                init();
            }
        }
    }

    private void init() {
        int buildVersion = AndroidUtils.getBuildVersion();
        LOG.debug("Operating on device with build version: {}", buildVersion);

        if (buildVersion >= Build.VERSION_CODES.M) {
            LOG.debug("Operating on device with API level >= Android M");

            if (!isLocationPermissionGranted()) {
                LOG.debug("Request for location permission necessary");
                requestLocationPermission();
                return;
            } else {
                LOG.debug("Location permission has alreaddy been granted");
            }
        }

        mMap.setMyLocationEnabled(true);

        initializeCurrentLocation();
        initializeCircle();
    }

    private void initializeCurrentLocation() {
        LatLng currentPos = getCurrentLocation();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                circle.setCenter(latLng);
            }
        });
    }

    private void initializeCircle() {
        CircleOptions opts = new CircleOptions();
        opts.center(getCurrentLocation());
        opts.strokeColor(R.color.ColorPrimaryDark);
        opts.fillColor(R.color.ColorPrimary);
        opts.radius(50);

        circle = mMap.addCircle(opts);
    }

    private LatLng getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return new LatLng(location.getAltitude(), location.getLongitude());
    }

    private boolean didUserGrantLocationPermission(String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        LOG.info("Request for user permission: ACCESS_FINE_LOCATION");
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isLocationPermissionGranted() {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
