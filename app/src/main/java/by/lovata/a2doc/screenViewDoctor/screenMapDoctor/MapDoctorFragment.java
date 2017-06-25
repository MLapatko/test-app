package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

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
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.ListDoctorFragment;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.MenuFilterFragment;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts.SortDefault;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapDoctorFragment extends Fragment implements OnMapReadyCallback,
                        MenuFilterFragment.AccessFilter{

    public static interface InformationInterfaceMap {
        public void setId_sort(int id_sort_selected);
        public void setFilters(int id_filter, boolean metro, boolean baby);
    }

    public static final String SAVEPARAMETER_PARSALABEL = "SAVEPARAMETER_PARSALABEL";

    public static final String ID_FILTER_SELECTED = "ID_FILTER_SELECTED";
    public static final String IS_METRO = "IS_METRO";
    public static final String IS_BABY = "IS_BABY";

    public static final String ID_FILTER_SELECTED_SAVE = "ID_FILTER_SELECTED_SAVE";
    public static final String IS_METRO_SAVE = "IS_METRO_SAVE";
    public static final String IS_BABY_SAVE = "IS_BABY_SAVE";

    DoctorInfo[] doctorsInfo;
    InformationInterfaceMap informationInterface;

    int id_filter;
    boolean metro;
    boolean baby;

    MapView mMapView;
    GoogleMap gMap;
    Geocoder geocoder;
    SharedPreferences sharedPreferences;
    Set<LatLng> markerLocation;    // HashMap of marker identifier and its location as a string

    public MapDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //doctorsInfo = informationInterface.getDoctors();

        if (savedInstanceState == null) {
            initializeData();
        }

        View view = inflater.inflate(R.layout.fragment_map_doctor, container, false);
        /*mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        sharedPreferences = getActivity().getSharedPreferences(
                MainActivity.NAME_PREFERENCES, Context.MODE_PRIVATE);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);*/

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
        geocoder = new Geocoder(getActivity(), Locale.US);
        markerLocation = new HashSet<>();
        try {
            List<Address> addressList = geocoder.getFromLocationName(LogoActivity.getCities()
                    .get(sharedPreferences.getInt(MainActivity.CITY_SELECT, 0)), 1);
            LatLng city = new LatLng(addressList.get(0).getLatitude(),
                    addressList.get(0).getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(city)      // Sets the center of the map to location user
                    .zoom(10)          // Sets the zoom
                    .bearing(0)        // Sets the orientation of the camera to east
                    .tilt(0)           // Sets the tilt of the camera to 40 degrees
                    .build();          // Creates a CameraPosition from the builder

            String doctorName;

            for(DoctorInfo doctorInfo : doctorsInfo) {
                doctorName = doctorInfo.getFull_name();
//                gMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(doctorInfo.lat, doctorInfo.lng))
//                        .title(doctorName)).setTag(doctorInfo);
            }

            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            gMap.setInfoWindowAdapter(new DoctorInfoWindowAdapter(getActivity().getLayoutInflater()));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
