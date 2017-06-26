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
import by.lovata.a2doc.screenViewDoctor.SelectDoctor;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableDoctorFragment extends Fragment implements
                        TimeAdapter.RecordTime{

    public static interface RecordDoctor {
        public void setYourInformationFragment(String day_selected, String time_selected);
    }

    public static String TIMES_PARCELABLE = "TIMES_PARCELABLE";
    public static String SELECT_DOCTOR = "SELECT_DOCTOR";

    public static String SELECT_DOCTOR_SAVE = "SELECT_DOCTOR_SAVE";

    Times[] times;
    SelectDoctor selectDoctor;
    RecordDoctor recordDoctor;

    public TimetableDoctorFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_timetable_doctor, container, false);

        if (savedInstanceState == null) {
            initializeData();
        } else {
            restoreData(savedInstanceState);
        }

        initializeView(root_view);

        return root_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        recordDoctor = ((RecordDoctor) activity);
    }

    @Override
    public void record(String day_selected, String time_selected) {
        recordDoctor.setYourInformationFragment(day_selected, time_selected);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArray(TIMES_PARCELABLE, times);
        outState.putParcelable(SELECT_DOCTOR_SAVE, selectDoctor);
    }

    private void initializeData() {
        selectDoctor = getArguments().getParcelable(SELECT_DOCTOR);

        int week = 0;
        times = getTimes(week);
    }

    private Times[] getTimes(int week) {
        APIMethods apiMethods = new APIMethods(getActivity());

        int id_doctor = selectDoctor.getId_doctor();
        int id_filter = selectDoctor.getId_filter();
        int id_organization = selectDoctor.getId_organization();

        return apiMethods.getTimesFromJSON(id_doctor, id_filter, id_organization, week);
    }

    private void restoreData(Bundle savedInstanceState) {
        selectDoctor = savedInstanceState.getParcelable(SELECT_DOCTOR_SAVE);
        times = (Times[]) savedInstanceState.getParcelableArray(TIMES_PARCELABLE);
    }

    private void initializeView(View root_view) {
        final TimeAdapter timeAdapter = new TimeAdapter(getActivity(), times);
        timeAdapter.setListener(this);
        ListView timetable_lst = (ListView) root_view.findViewById(R.id.timetable_lst);
        timetable_lst.setAdapter(timeAdapter);

        final ArrayList<Integer> list_weeks = new ArrayList<>();
        list_weeks.add(0);

        SeekBar seekBar = (SeekBar) root_view.findViewById(R.id.seekbar_week);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int start_seekbar;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                start_seekbar = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int end_seekbar = seekBar.getProgress();
                if (start_seekbar != end_seekbar) {
                    if (!list_weeks.contains(end_seekbar)) {
                        list_weeks.add(end_seekbar);

                        times = concatArray(times, getTimes(end_seekbar));
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

    }
}
