package by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.lovata.a2doc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableDoctorFragment extends Fragment {


    public TimetableDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timetable_doctor, container, false);
    }

}
