package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Location;

public class LocationsCardAdapter extends RecyclerView.Adapter<LocationsCardAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        protected MapView mapView;

        protected TextView nameView;
        protected TextView latView;
        protected TextView lonView;

        protected Location location;
        protected GoogleMap map;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mapView = (MapView) itemView.findViewById(R.id.map);
            this.nameView = (TextView) itemView.findViewById(R.id.name);
            this.latView = (TextView) itemView.findViewById(R.id.lat);
            this.lonView = (TextView) itemView.findViewById(R.id.lon);

            mapView.onCreate(null);
            mapView.getMapAsync(this);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;

            mapView.onResume();
            setLocation();
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;

            updateViews();
            setLocation();
        }

        private void updateViews() {
            nameView.setText(String.format(
                    "%s (%s%s)",
                    location.getName(),
                    location.getRadius(),
                    context.getResources().getString(R.string.activity_locationlist_radius_in_title)));
            latView.setText(String.valueOf(location.getLat()));
            lonView.setText(String.valueOf(location.getLon()));
        }

        private void setLocation() {
            if (location != null && map != null) {
                LatLng latLng = new LatLng(location.getLat(), location.getLon());

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);

                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(latLng);
                circleOptions.radius(location.getRadius());
                circleOptions.fillColor(R.color.ColorSecondary);

                map.moveCamera(update);
                map.addCircle(circleOptions);
            }
        }
    }

    protected final Context context;

    private List<Location> locations;

    @SuppressWarnings("unchecked")
    public LocationsCardAdapter(Context context) {
        this.context = context;
        this.locations = (List<Location>) Collections.EMPTY_LIST;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.card_item_location, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setLocation(locations.get(position));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void setItems(List<Location> items) {
        this.locations = items;
        notifyDataSetChanged();
    }
}
