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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import by.lovata.a2doc.LogoActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.MainActivity;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.InterfaceDoctors;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapDoctorFragment extends Fragment {

    DoctorInfo[] doctorsInfo;
    InterfaceDoctors doctorsInterface;

    public MapDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        doctorsInfo = doctorsInterface.getDoctors();

        View view = inflater.inflate(R.layout.fragment_map_doctor, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                MainActivity.NAME_PREFERENCES, Context.MODE_PRIVATE);

        final MapView mMapView = (MapView) view.findViewById(R.id.map);

        // Получаем MapController
        MapController mMapController = mMapView.getMapController();

        // Перемещаем карту на заданные координаты
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            Map<Integer, String> cities = LogoActivity.getCities();
            List<Address> addresses = geocoder.getFromLocationName(
                    cities.get(sharedPreferences.getInt(
                            MainActivity.CITY_SELECT, 0)), 1);
            GeoPoint point = new GeoPoint(addresses.get(0).getLatitude(), addresses.get(0)
                    .getLongitude());
            mMapController.setPositionAnimationTo(point);
            mMapController.setZoomCurrent(15);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.doctorsInterface = (InterfaceDoctors) activity;
    }


}
