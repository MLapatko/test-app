package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.LogoActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenStart.MainActivity;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.OrganizationInfo;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;
import by.lovata.a2doc.screenViewDoctor.SelectDoctor;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.ListDoctorFragment;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.MenuFilterFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapDoctorFragment extends Fragment implements OnMapReadyCallback,
        MenuFilterFragment.AccessFilter {

    public static final String SAVEPARAMETER_PARSALABEL = "SAVEPARAMETER_PARSALABEL";
    public static final String POSITION="POSITION";
    static final float COORDINATE_OFFSET = 0.00002f;
    private int positionOrg;
    private AbstractMarker clickedClusterItem = null;
    ArrayList<DoctorInfo> doctorsInfo;
    Map<Integer, OrganizationInfo> organizations;
    SaveParameter saveParameter;

    MapView mMapView;
    GoogleMap gMap;
    ClusterManager<AbstractMarker> clusterManager;
    Geocoder geocoder;
    SharedPreferences sharedPreferences;
    Set<LatLng> markerLocations;    // HashMap of marker identifier and its location as a string

    public MapDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_map_doctor, container, false);

        initializeData();

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        sharedPreferences = getActivity().getSharedPreferences(
                MainActivity.NAME_PREFERENCES, Context.MODE_PRIVATE);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        setHasOptionsMenu(true);

        return view;
    }

    private void initializeData() {
        positionOrg=getArguments().getInt(POSITION);
        Log.e("mylog","positionOrg"+positionOrg);
        saveParameter = (SaveParameter) getArguments().get(SAVEPARAMETER_PARSALABEL);
        int id_filter = saveParameter.getId_filter();
        boolean metro = saveParameter.isMetro();
        boolean baby = saveParameter.isBaby();
        doctorsInfo = ListDoctorFragment.createArrayWithFilter(saveParameter.getDoctorsInfo(), id_filter, metro, baby);
        organizations = saveParameter.getOrganizations();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMaxZoomPreference(19f);
        geocoder = new Geocoder(getActivity(), Locale.US);
        markerLocations = new HashSet<>();
        clusterManager = new ClusterManager<>(getActivity(), gMap);

        clusterManager.setOnClusterClickListener(new ClusterManager.
                OnClusterClickListener<AbstractMarker>() {
            @Override
            public boolean onClusterClick(final Cluster<AbstractMarker> cluster) {

                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (ClusterItem item : cluster.getItems()) {
                    builder.include(item.getPosition());
                }
                final LatLngBounds bounds = builder.build();
                gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                return true;
            }
        });

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<AbstractMarker>() {
            @Override
            public boolean onClusterItemClick(AbstractMarker item) {
                clickedClusterItem = item;
                return false;
            }
        });

        gMap.setOnCameraIdleListener(clusterManager);
        gMap.setOnMarkerClickListener(clusterManager);
        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (clickedClusterItem != null) {
                    Intent intent = new Intent(getActivity(), DoctorActivity.class);
                    DoctorInfo doctorInfo = clickedClusterItem.getDoctorInfo();
                    saveParameter.setSelectDoctor(new SelectDoctor(doctorInfo.getId(), saveParameter.getId_filter(),
                            clickedClusterItem.getOrg_id(), null, null, doctorInfo));
                    intent.putExtra(DoctorActivity.SAVEPARAMETER_PARSALABEL, saveParameter);
                    getActivity().startActivity(intent);
                }
            }
        });

        //Move camera to initial position (Belarus)
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(54, 27), 5f));

        try {
            List<Address> addressList = geocoder.getFromLocationName(LogoActivity.getCities()
                    .get(sharedPreferences.getInt(MainActivity.CITY_SELECT, 0)), 1);
            LatLng city = new LatLng(addressList.get(0).getLatitude(),
                    addressList.get(0).getLongitude());


            final CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(city)      // Sets the center of the map to location user
                    .zoom(10)          // Sets the zoom
                    .bearing(0)        // Sets the orientation of the camera to east
                    .tilt(10)          // Sets the tilt of the camera to 10 degrees
                    .build();          // Creates a CameraPosition from the builder

            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            String doctorName;
            int[] organizationIDs;
            int start=0;
            int end;
            for (DoctorInfo doctorInfo : doctorsInfo) {
                doctorName = doctorInfo.getFull_name();
                organizationIDs = doctorInfo.getId_organization();
                if (positionOrg!=-1){
                    start=positionOrg;
                    end=positionOrg+1;
                }
                else {
                    start = 0;
                    end = organizationIDs.length;
                }
                for (int i = start; i <end ; i++) {
                    LatLng coords = coordinateForMarker(new LatLng(organizations.get(organizationIDs[i]).getLat(),
                            organizations.get(organizationIDs[i]).getLng()));
                    AbstractMarker offsetItem = new AbstractMarker(coords.latitude,
                            coords.longitude, doctorName, doctorInfo, organizationIDs[i],
                            organizations.get(organizationIDs[i]).getName());
                    clusterManager.addItem(offsetItem);
                }
            }

            gMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
            clusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                    new DoctorInfoWindowAdapter(getActivity().getLayoutInflater()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        CustomClusterRenderer<AbstractMarker> clusterRenderer = new CustomClusterRenderer<>(
                getActivity(), gMap, clusterManager, gMap.getCameraPosition().zoom, 19f);
        clusterManager.setRenderer(clusterRenderer);
        gMap.setOnCameraMoveListener(clusterRenderer);

    }

    // Check if any marker is displayed on given coordinate. If yes then decide
    // another appropriate coordinate to display this marker.
    private LatLng coordinateForMarker(LatLng location) {

        if (mapAlreadyHasMarkerForLocation(location)) {
            location = coordinateForMarker(new LatLng(location.latitude + COORDINATE_OFFSET,
                    location.longitude + COORDINATE_OFFSET));
        } else {
            markerLocations.add(location);
        }
        return location;
    }

    // Return whether marker with same location is already on map
    private boolean mapAlreadyHasMarkerForLocation(LatLng location) {
        return (markerLocations.contains(location));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem view_change = menu.findItem(R.id.view_change);
        view_change.setTitle(getResources().getString(R.string.view_change_map));
        view_change.setIcon(R.drawable.ic_format_align_justify_24dp);
    }



    @Override
    public void setFilters(int id_filter, boolean metro, boolean baby) {
        saveParameter.setId_filter(id_filter);
        saveParameter.setMetro(metro);
        saveParameter.setBaby(baby);

        doctorsInfo = ListDoctorFragment.createArrayWithFilter(saveParameter.getDoctorsInfo(), id_filter, metro, baby);

        gMap.clear();
        mMapView.getMapAsync(this);
    }

    @Override
    public Map<Integer, String> getServices() {
        return saveParameter.getServices();
    }



    private class DoctorInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private LayoutInflater inflater = null;

        DoctorInfoWindowAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public View getInfoWindow(Marker marker) {

            View infoWindows = inflater.inflate(R.layout.doctor_info_window, null);
            TextView fio = (TextView) infoWindows.findViewById(R.id.fio_infoWindow);
            TextView speciality = (TextView) infoWindows.findViewById(R.id.speciality_infoWindow);
            ImageView image = (ImageView) infoWindows.findViewById(R.id.img_infoWindow);
            TextView review = (TextView) infoWindows.findViewById(R.id.review_infoWindow);
            TextView price = (TextView) infoWindows.findViewById(R.id.price_infoWindow);
            TextView position = (TextView) infoWindows.findViewById(R.id.position_infoWindow);

            if (clickedClusterItem != null) {
                DoctorInfo doctorInfo = clickedClusterItem.getDoctorInfo();
                fio.setText(doctorInfo.getFull_name());
                speciality.setText(doctorInfo.getSpeciality());
                position.setText("Адрес: ".concat(clickedClusterItem.getOrgName()));
                Picasso.with(getActivity())
                        .load(doctorInfo.getUrl_img())
                        .placeholder(R.drawable.ic_file_download_24dp)
                        .error(R.drawable.ic_error_24dp)
                        .into(image);
                review.setText(String.valueOf(doctorInfo.getCount_reviews()).concat(" отз."));
                int minPrice, maxPrice;
                minPrice =  Collections.min(doctorInfo.getService_list().values());
                maxPrice = Collections.max(doctorInfo.getService_list().values());
                if (minPrice == maxPrice) {
                    price.setText("Цена: ".concat(String.valueOf(minPrice)).concat(" руб."));
                } else {
                    price.setText("Цена: от ".concat(String.valueOf(minPrice)).concat(" до ")
                            .concat(String.valueOf(maxPrice)).concat(" руб."));
                }
            }

            return infoWindows;

        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

}
