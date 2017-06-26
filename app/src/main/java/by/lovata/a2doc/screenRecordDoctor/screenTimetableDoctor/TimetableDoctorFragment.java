package by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    ArrayList<Integer> list_weeks;
    TimeAdapter timeAdapter;

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
        timeAdapter = new TimeAdapter(getActivity(), times);
        timeAdapter.setListener(this);
        ListView timetable_lst = (ListView) root_view.findViewById(R.id.timetable_lst);
        timetable_lst.setAdapter(timeAdapter);

        list_weeks = new ArrayList<>();
        list_weeks.add(0);

        Button button_week_1 = (Button) root_view.findViewById(R.id.btn_week_1);
        button_week_1.setOnClickListener(onClickListener_btn_week);
        Button button_week_2 = (Button) root_view.findViewById(R.id.btn_week_2);
        button_week_2.setOnClickListener(onClickListener_btn_week);
        Button button_week_3 = (Button) root_view.findViewById(R.id.btn_week_3);
        button_week_3.setOnClickListener(onClickListener_btn_week);
        Button button_week_4 = (Button) root_view.findViewById(R.id.btn_week_4);
        button_week_4.setOnClickListener(onClickListener_btn_week);

    }

    View.OnClickListener onClickListener_btn_week = new View.OnClickListener() {
        int btn_week_old = 0;

        @Override
        public void onClick(View v) {
            int btn_week = 0;
            switch (v.getId()) {
                case R.id.btn_week_1:
                    btn_week = 0;
                    break;
                case R.id.btn_week_2:
                    btn_week = 1;
                    break;
                case R.id.btn_week_3:
                    btn_week = 2;
                    break;
                case R.id.btn_week_4:
                    btn_week = 3;
                    break;
            }

            getNewTime(btn_week);

        }

        private void getNewTime(int btn_week) {
            if (btn_week_old != btn_week) {
                btn_week_old = btn_week;
                if (!list_weeks.contains(btn_week)) {
                    list_weeks.add(btn_week);

                    times = concatArray(times, getTimes(btn_week));
                }

                int position = list_weeks.indexOf(btn_week);
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
    };
}
