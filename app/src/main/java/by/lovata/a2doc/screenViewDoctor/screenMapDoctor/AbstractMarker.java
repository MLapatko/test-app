package by.lovata.a2doc.screenViewDoctor.screenMapDoctor;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import by.lovata.a2doc.screenViewDoctor.DoctorInfo;

/**
 * Created by kroos on 6/25/17.
 */

public class AbstractMarker implements ClusterItem {

    private final LatLng mPosition;
    private DoctorInfo doctorInfo;
    private String title;
    private int org_id;

    public AbstractMarker(double latitude, double longitude, String title, DoctorInfo doctorInfo,
                          int org_id) {
        mPosition = new LatLng(latitude, longitude);
        this.doctorInfo = doctorInfo;
        this.title = title;
        this.org_id = org_id;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public DoctorInfo getDoctorInfo() {
        return doctorInfo;
    }

    public int getOrg_id() {
        return org_id;
    }
}
