package by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor;


import android.os.Bundle;
import android.app.Fragment;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableDoctorFragment extends Fragment {

    public static String ID_DOCTOR_SELECTED = "ID_DOCTOR_SELECTED";
    public static String ID_FILTER_SELECTED = "ID_FILTER_SELECTED";
    public static String ID_ORGANIZATION_SELECTED = "ID_ORGANIZATION_SELECTED";

    public static String TIMES_PARCELABLE = "TIMES_PARCELABLE";

    Times[] times;

    public TimetableDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            int id_doctor = getArguments().getInt(ID_DOCTOR_SELECTED, 0);
            int id_filter = getArguments().getInt(ID_FILTER_SELECTED, 0);
            int id_organization = getArguments().getInt(ID_ORGANIZATION_SELECTED, 0);

            APIMethods apiMethods = new APIMethods(getActivity());
            times = apiMethods.getTimesFromJSON(id_doctor, id_filter, id_organization, 0);
        } else {
            times = (Times[]) savedInstanceState.getParcelableArray(TIMES_PARCELABLE);
        }

        return inflater.inflate(R.layout.fragment_timetable_doctor, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArray(TIMES_PARCELABLE, times);
    }


}
