package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import by.lovata.a2doc.R;

/**
 * Created by kroos on 6/20/17.
 */

public class DoctorInfoWindowAdapter implements InfoWindowAdapter {

    private LayoutInflater inflater = null;

    DoctorInfoWindowAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoWindows = inflater.inflate(R.layout.doctor_info_window, null);
        TextView title = (TextView) infoWindows.findViewById(R.id.info_window_title);
        TextView description = (TextView) infoWindows.findViewById(R.id.info_window_description);
        return infoWindows;
    }
}
