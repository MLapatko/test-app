package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import by.lovata.a2doc.LogoActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.MainActivity;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.InterfaceDoctors;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapDoctorFragment extends Fragment implements OnMapReadyCallback {

    DoctorInfo[] doctorsInfo;
    InterfaceDoctors doctorsInterface;
    MapView mMapView;
    GoogleMap gMap;
    Geocoder geocoder;
    SharedPreferences sharedPreferences;

    public MapDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        doctorsInfo = doctorsInterface.getDoctors();

        View view = inflater.inflate(R.layout.fragment_map_doctor, container, false);
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
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.doctorsInterface = (InterfaceDoctors) activity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        geocoder = new Geocoder(getActivity(), Locale.US);
        try {
            List<Address> addressList = geocoder.getFromLocationName(LogoActivity.getCities()
                    .get(sharedPreferences.getInt(MainActivity.CITY_SELECT, 0)), 1);
            LatLng city = new LatLng(addressList.get(0).getLatitude(),
                    addressList.get(0).getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(city)      // Sets the center of the map to location user
                    .zoom(12)          // Sets the zoom
                    .bearing(0)       // Sets the orientation of the camera to east
                    .tilt(40)          // Sets the tilt of the camera to 30 degrees
                    .build();          // Creates a CameraPosition from the builder

            String doctorName;

            for(DoctorInfo doctorInfo : doctorsInfo) {
                doctorName = doctorInfo.full_name;
                gMap.addMarker(new MarkerOptions()
                        .position(new LatLng(doctorInfo.lat, doctorInfo.lng))
                        .title(doctorName));
            }
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            gMap.setInfoWindowAdapter(new DoctorInfoWindowAdapter(getActivity().getLayoutInflater()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
