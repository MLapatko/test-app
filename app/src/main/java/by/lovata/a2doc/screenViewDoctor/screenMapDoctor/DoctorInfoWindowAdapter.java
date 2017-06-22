package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;

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
        TextView fio = (TextView) infoWindows.findViewById(R.id.fio_infoWindow);
        TextView speciality = (TextView) infoWindows.findViewById(R.id.speciality_infoWindow);
        ImageView img = (ImageView) infoWindows.findViewById(R.id.img_infoWindow);
        TextView review = (TextView) infoWindows.findViewById(R.id.review_infoWindow);
        TextView price = (TextView) infoWindows.findViewById(R.id.price_of_consultation_infoWindow);
        Button btn = (Button) infoWindows.findViewById(R.id.btn_infoWindow);

        DoctorInfo doctorInfo = (DoctorInfo) marker.getTag();

        fio.setText(doctorInfo.full_name);
        speciality.setText(doctorInfo.speciality);
//        review.setText(doctorInfo.review);
//        price.setText(doctorInfo.price_of_consultation);
        btn.setText("Записаться");

        return infoWindows;
    }
}
