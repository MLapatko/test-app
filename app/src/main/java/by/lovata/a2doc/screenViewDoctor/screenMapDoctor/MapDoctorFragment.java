package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.LogoActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.MainActivity;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.OrganizationInfo;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.MenuFilterFragment;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts.SortDefault;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapDoctorFragment extends Fragment implements OnMapReadyCallback,
        MenuFilterFragment.AccessFilter {


    public interface InformationInterfaceMap {
        void setId_sort(int id_sort_selected);

        void setFilters(int id_filter, boolean metro, boolean baby);
    }

    public static final String SAVEPARAMETER_PARSALABEL = "SAVEPARAMETER_PARSALABEL";

    public static final String ID_FILTER_SELECTED = "ID_FILTER_SELECTED";
    public static final String IS_METRO = "IS_METRO";
    public static final String IS_BABY = "IS_BABY";

    public static final String ID_FILTER_SELECTED_SAVE = "ID_FILTER_SELECTED_SAVE";
    public static final String IS_METRO_SAVE = "IS_METRO_SAVE";
    public static final String IS_BABY_SAVE = "IS_BABY_SAVE";

    static final float COORDINATE_OFFSET = 0.002f;

    private AbstractMarker clickedClusterItem = null;
    DoctorInfo[] doctorsInfo;
    Map<Integer, OrganizationInfo> organizations;
    InformationInterfaceMap informationInterface;
    SaveParameter saveParameter;

    int id_filter;
    boolean metro;
    boolean baby;

    MapView mMapView;
    GoogleMap gMap;
    ClusterManager<AbstractMarker> clusterManager;
    Geocoder geocoder;
    SharedPreferences sharedPreferences;
    Set<LatLng> markerLocation;    // HashMap of marker identifier and its location as a string

    public MapDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            initializeData();
        }

        View view = inflater.inflate(R.layout.fragment_map_doctor, container, false);
        saveParameter = (SaveParameter) getArguments().get(SAVEPARAMETER_PARSALABEL);
        doctorsInfo = saveParameter.getDoctorsInfo();
        organizations = saveParameter.getOrganizations();

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.informationInterface = (InformationInterfaceMap) activity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        // The My Location button will be visible on the top right of the map.
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gMap.setMyLocationEnabled(true);
        geocoder = new Geocoder(getActivity(), Locale.US);
        markerLocation = new HashSet<>();
        clusterManager = new ClusterManager<>(getActivity(), gMap);

        gMap.setOnMarkerClickListener(clusterManager);
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<AbstractMarker>() {
            @Override
            public boolean onClusterItemClick(AbstractMarker item) {
                clickedClusterItem = item;
                return false;
            }
        });
        gMap.setOnCameraIdleListener(clusterManager);
        gMap.setOnMarkerClickListener(clusterManager);
        gMap.setOnInfoWindowClickListener(clusterManager);
        gMap.setOnInfoWindowClickListener(clusterManager);
        try {
            List<Address> addressList = geocoder.getFromLocationName(LogoActivity.getCities()
                    .get(sharedPreferences.getInt(MainActivity.CITY_SELECT, 0)), 1);
            LatLng city = new LatLng(addressList.get(0).getLatitude(),
                    addressList.get(0).getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(city)      // Sets the center of the map to location user
                    .zoom(10)          // Sets the zoom
                    .bearing(0)        // Sets the orientation of the camera to east
                    .tilt(10)          // Sets the tilt of the camera to 40 degrees
                    .build();          // Creates a CameraPosition from the builder

            String doctorName;
            int[] organizationIDs;

            for(DoctorInfo doctorInfo : doctorsInfo) {
                doctorName = doctorInfo.getFull_name();
                organizationIDs = doctorInfo.getId_organization();
                for(int id : organizationIDs) {
//                    gMap.addMarker(new MarkerOptions()
//                            .position(coordinateForMarker(new LatLng(organizations.get(id).getLat(),
//                                    organizations.get(id).getLng())))
//                            .title(doctorName)).setTag(doctorInfo);
                    LatLng coords = coordinateForMarker(new LatLng(organizations.get(id).getLat(),
                            organizations.get(id).getLng()));
                    AbstractMarker offsetItem = new AbstractMarker(coords.latitude,
                            coords.longitude, doctorName, doctorInfo);
                    clusterManager.addItem(offsetItem);
                }
            }
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            gMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
            clusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                    new DoctorInfoWindowAdapter(getActivity().getLayoutInflater()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Check if any marker is displayed on given coordinate. If yes then decide
    // another appropriate coordinate to display this marker.
    private LatLng coordinateForMarker(LatLng location) {

        if (mapAlreadyHasMarkerForLocation(location)) {
            location = coordinateForMarker(new LatLng(location.latitude + COORDINATE_OFFSET,
                    location.longitude + COORDINATE_OFFSET));
        } else {
            markerLocation.add(location);
        }
        return location;
    }

    // Return whether marker with same location is already on map
    private boolean mapAlreadyHasMarkerForLocation(LatLng location) {
        return (markerLocation.contains(location));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.view_filter, menu);

        MenuItem view_change = menu.findItem(R.id.view_change);
        view_change.setTitle(getResources().getString(R.string.view_change_map));
        view_change.setIcon(R.drawable.ic_format_align_justify_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showMenuFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setFilters(int id_filter, boolean metro, boolean baby) {
        this.id_filter = id_filter;
        this.metro = metro;
        this.baby = baby;

        informationInterface.setFilters(id_filter, metro, baby);
        informationInterface.setId_sort(0);

        if (doctorsInfo.length > 1) {
            Arrays.sort(doctorsInfo, new SortDefault());
        }
        /*((DoctorsAdapter) recyclerView.getAdapter()).
                setArray_doctors(createArrayWithFilter(doctorsInfo, id_filter, metro, baby));

        synchronizedDoctors();*/
    }

    @Override
    public Map<Integer, String> getSevices() {
        return null;
        //return informationInterface.getServices();
    }

    private void showMenuFilter() {
        MenuFilterFragment dialog_filter = new MenuFilterFragment();
        Bundle bundle_filter = new Bundle();
        bundle_filter.putInt(MenuFilterFragment.ID_FILTER_SELECTED, id_filter);
        bundle_filter.putBoolean(MenuFilterFragment.IS_METRO, metro);
        bundle_filter.putBoolean(MenuFilterFragment.IS_BABY, baby);
        dialog_filter.setArguments(bundle_filter);
        dialog_filter.show(getChildFragmentManager(), "filter");
    }

    private void initializeData() {
        //id_filter = getArguments().getInt(ID_FILTER_SELECTED);
        //metro = getArguments().getBoolean(IS_METRO);
        //baby = getArguments().getBoolean(IS_BABY);
    }

    private class DoctorInfoWindowAdapter implements GoogleMap.InfoWindowAdapter,
            GoogleMap.OnInfoWindowClickListener {

        private LayoutInflater inflater = null;

        DoctorInfoWindowAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public View getInfoWindow(Marker marker) {

            View infoWindows = inflater.inflate(R.layout.doctor_info_window, null);
            TextView fio = (TextView) infoWindows.findViewById(R.id.fio_infoWindow);
            TextView speciality = (TextView) infoWindows.findViewById(R.id.speciality_infoWindow);
            ImageView img = (ImageView) infoWindows.findViewById(R.id.img_infoWindow);
            TextView review = (TextView) infoWindows.findViewById(R.id.review_infoWindow);
            TextView price = (TextView) infoWindows.findViewById(R.id.price_of_consultation_infoWindow);

            if (clickedClusterItem != null) {
                DoctorInfo doctorInfo = clickedClusterItem.getDoctorInfo();

                fio.setText(doctorInfo.getFull_name());
                speciality.setText(doctorInfo.getSpeciality());
//            review.setText(doctorInfo.review);
//            price.setText(doctorInfo.price_of_consultation);
            }

            return infoWindows;

        }

        @Override
        public View getInfoContents(Marker marker) { return null; }

        @Override
        public void onInfoWindowClick(Marker marker) {
            if (clickedClusterItem != null) {

            }
        }
    }

}
