package de.iweinzierl.easyprofiles.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import de.inselhome.android.utils.UiUtils;
import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.util.AndroidUtils;

public class PickLocationFragment extends Fragment implements OnMapReadyCallback {

    public interface Callback {
        void onLocationSelected(double lat, double lon, int radius);
    }

    private static final int REQUEST_LOCATION_PERMISSION = 100;

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(PickLocationFragment.class.getName());

    private Callback callback;

    private GoogleMap mMap;
    private Circle circle;
    private SeekBar radiusBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_location, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = new SupportMapFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.location_map, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        radiusBar = UiUtils.getGeneric(SeekBar.class, view, R.id.radius_bar);
        radiusBar.setProgress(50);
        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (circle != null) {
                    int newRadius = seekBar.getProgress();
                    circle.setRadius(newRadius);

                    if (callback != null) {
                        callback.onLocationSelected(
                                circle.getCenter().latitude, circle.getCenter().longitude, newRadius);
                    }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        init();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public double getLat() {
        return circle.getCenter().latitude;
    }

    public double getLon() {
        return circle.getCenter().longitude;
    }

    public double getRadius() {
        return circle.getRadius();
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

                if (callback != null) {
                    callback.onLocationSelected(
                            circle.getCenter().latitude, circle.getCenter().longitude, (int) circle.getRadius());
                }
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
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
        return getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
