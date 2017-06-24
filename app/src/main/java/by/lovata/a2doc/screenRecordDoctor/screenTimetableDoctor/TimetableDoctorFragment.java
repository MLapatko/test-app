package by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableDoctorFragment extends Fragment implements
                        TimeAdapter.RecordTime{

    public static interface RecordDoctor {
        public void showYourInformation(String day_selected, String time_selected);
    }

    public static String ID_DOCTOR_SELECTED = "ID_DOCTOR_SELECTED";
    public static String ID_FILTER_SELECTED = "ID_FILTER_SELECTED";
    public static String ID_ORGANIZATION_SELECTED = "ID_ORGANIZATION_SELECTED";

    public static String TIMES_PARCELABLE = "TIMES_PARCELABLE";

    Times[] times;
    RecordDoctor recordDoctor;

    int id_doctor;
    int id_filter;
    int id_organization;

    public TimetableDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_timetable_doctor, container, false);

        if (savedInstanceState == null) {
            id_doctor = getArguments().getInt(ID_DOCTOR_SELECTED, 0);
            id_filter = getArguments().getInt(ID_FILTER_SELECTED, 0);
            id_organization = getArguments().getInt(ID_ORGANIZATION_SELECTED, 0);

            APIMethods apiMethods = new APIMethods(getActivity());
            times = apiMethods.getTimesFromJSON(id_doctor, id_filter, id_organization, 0);
        } else {
            times = (Times[]) savedInstanceState.getParcelableArray(TIMES_PARCELABLE);
        }

        final TimeAdapter timeAdapter = new TimeAdapter(getActivity(), times);
        timeAdapter.setListener(this);
        ListView timetable_lst = (ListView) root_view.findViewById(R.id.timetable_lst);
        timetable_lst.setAdapter(timeAdapter);

        final ArrayList<Integer> list_weeks = new ArrayList<>();
        list_weeks.add(0);
        SeekBar seekBar = (SeekBar) root_view.findViewById(R.id.seekbar_week);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progress = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int end_seekbar = seekBar.getProgress();
                if (progress != end_seekbar) {
                    if (!list_weeks.contains(end_seekbar)) {
                        list_weeks.add(end_seekbar);

                        APIMethods apiMethods = new APIMethods(getActivity());
                        times = concatArray(times, apiMethods.getTimesFromJSON(
                                id_doctor, id_filter, id_organization, end_seekbar));
                    }
                    int position = list_weeks.indexOf(end_seekbar);
                    Times[] times_current = new Times[7];
                    System.arraycopy(times, position * 7, times_current, 0, 7);
                    timeAdapter.setTimes(times_current);
                }
            }

            private Times[] concatArray(Times[] a, Times[] b) {
                if (a == null)
                    return b;
                if (b == null)
                    return a;
                Times[] r = new Times[a.length + b.length];
                System.arraycopy(a, 0, r, 0, a.length);
                System.arraycopy(b, 0, r, a.length, b.length);
                return r;
            }
        });

        return root_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        recordDoctor = ((RecordDoctor) activity);
    }


    @Override
    public void record(String day_selected, String time_selected) {
        recordDoctor.showYourInformation(day_selected, time_selected);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArray(TIMES_PARCELABLE, times);
    }


}
