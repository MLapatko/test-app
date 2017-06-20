package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;


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

import by.lovata.a2doc.LogoActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.MainActivity;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapDoctorFragment extends Fragment {


    public MapDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_doctor, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                MainActivity.NAME_PREFERENCES, Context.MODE_PRIVATE);

        final MapView mMapView = (MapView) view.findViewById(R.id.map);

        // Получаем MapController
        MapController mMapController = mMapView.getMapController();

        // Перемещаем карту на заданные координаты
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addresses = geocoder.getFromLocationName(
                    LogoActivity.cities.get(sharedPreferences.getInt(
                            MainActivity.CITY_SELECT, 0) + 1), 1);
            GeoPoint point = new GeoPoint(addresses.get(0).getLatitude(), addresses.get(0)
                    .getLongitude());
            mMapController.setPositionAnimationTo(point);
            mMapController.setZoomCurrent(15);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

}
