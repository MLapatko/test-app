package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.lovata.a2doc.R;
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

        final MapView mMapView = (MapView) view.findViewById(R.id.map);

        // Получаем MapController
        MapController mMapController = mMapView.getMapController();

        // Перемещаем карту на заданные координаты
        mMapController.setPositionAnimationTo(new GeoPoint(53.9, 27.57));

        mMapController.setZoomCurrent(15);
        return view;
    }

}
