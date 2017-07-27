package by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;
import by.lovata.a2doc.search.SearchActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableDoctorFragment extends Fragment implements
        TimeAdapter.RecordTime {

    public static interface RecordDoctor {
        public void setYourInformationFragment(String day_selected, String time_selected);
    }

    public static final String TIMES_PARCELABLE = "TIMES_PARCELABLE";
    public static final String SAVE_PARAMETER = "SAVE_PARAMETER";

    private static final String SELECT_DOCTOR_SAVE = "SELECT_DOCTOR_SAVE";
    private int idSpeciality;
    ArrayList<Times> currentTimetable;
    SaveParameter saveParpmeter;
    RecordDoctor recordDoctor;
    TimeAdapter timeAdapter;
    private ArrayList<Times>timetable;
    private int idDoctor;
    private int position=7;
    private ImageButton btnPrevious;
    private ImageButton btnNext;
    private Spinner spinnerServices;
    public TimetableDoctorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timetable_doctor, container, false);

        if (savedInstanceState == null) {
            initializeData();
        } else {
            restoreData(savedInstanceState);
        }

        initializeView(rootView);

        return rootView;
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

        outState.putParcelableArrayList(TIMES_PARCELABLE, currentTimetable);
        outState.putParcelable(SELECT_DOCTOR_SAVE, saveParpmeter);
    }

    private void initializeData() {
        saveParpmeter = getArguments().getParcelable(SAVE_PARAMETER);
        idDoctor = saveParpmeter.getSelectDoctor().getId_doctor();
        currentTimetable=new ArrayList<>();
        idSpeciality=saveParpmeter.getIdSpeciality();
         APIMethods apiMethods = new APIMethods(getActivity());
        timetable=apiMethods.getTimesFromJSON(idDoctor, idSpeciality);
        currentTimetable.addAll(timetable.subList(0,7));
    }


    private void restoreData(Bundle savedInstanceState) {
        saveParpmeter = savedInstanceState.getParcelable(SELECT_DOCTOR_SAVE);
        currentTimetable = savedInstanceState.getParcelableArrayList(TIMES_PARCELABLE);
    }

    private void initializeView(View rootView) {
        timeAdapter = new TimeAdapter(getActivity(), currentTimetable);
        timeAdapter.setListener(this);
        ListView timetableList = (ListView) rootView.findViewById(R.id.timetable_lst);
        timetableList.setAdapter(timeAdapter);
        btnPrevious = (ImageButton) rootView.findViewById(R.id.btn_previous);
        btnPrevious.setEnabled(false);
        btnPrevious.setOnClickListener(onClickListenerBtnListner);
        btnNext = (ImageButton) rootView.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(onClickListenerBtnListner);
        spinnerServices=(Spinner)rootView.findViewById(R.id.spinner_services);
        final ArrayList<String> services= getServicesNames(saveParpmeter.getServices(),
                saveParpmeter.getSelectDoctor().getDoctorInfo().getService_list().keySet());
        spinnerServices.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_activated_1,
                services));

        spinnerServices.setSelection(DoctorActivity.getPositionService(saveParpmeter));
        spinnerServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveParpmeter.getSelectDoctor().setId_filter(SearchActivity.findIdMap(saveParpmeter.getServices(),
                        services.get(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        String[] organizations_name = DoctorActivity.getOrganizationName(saveParpmeter);
        int posotion_organization =DoctorActivity.getPositionOrganization(saveParpmeter);
        Spinner organizations_profile = (Spinner) rootView.findViewById(R.id.organizations_profile);
        organizations_profile.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                organizations_name));
        organizations_profile.setSelection(posotion_organization);
        organizations_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveParpmeter.getSelectDoctor().setId_organization(saveParpmeter.getSelectDoctor().
                        getDoctorInfo().getId_organization()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayList<String> getServicesNames(Map<Integer, String> services, Set<Integer> serviceList) {
    ArrayList<String> servicesNames=new ArrayList<>();
        for (Map.Entry<Integer, String> entry : services.entrySet()) {
            if (serviceList.contains(entry.getKey()))
                servicesNames.add(entry.getValue());
        }
        return servicesNames;
    }

    View.OnClickListener onClickListenerBtnListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_previous:
                    btnNext.setEnabled(true);
                    OnButtonClick (btnPrevious,position-14,position-7,7);
                    break;
                case R.id.btn_next:
                    btnPrevious.setEnabled(true);
                    OnButtonClick (btnNext,position,position+7,timetable.size());
                    break;
            }
        }

        public void OnButtonClick (View button,int start,int end,int extremePosition){
            currentTimetable=new ArrayList<>();
            currentTimetable.addAll(timetable.subList(start,end));
            if (button.equals(btnPrevious))position-=7;
            else position+=7;
            if (position==extremePosition)
                button.setEnabled(false);
            timeAdapter.setTimes(currentTimetable);
        }
    };
}
