package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
    private FragmentActivity myContext;

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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
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
        myContext = (FragmentActivity) activity;
        this.doctorsInterface = (InterfaceDoctors) activity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        gMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
