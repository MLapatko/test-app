package by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;
import by.lovata.a2doc.search.SearchActivity;
import by.lovata.a2doc.search.Utility;


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
    //расписание отображается по неделям, поэтому position=7
    private int position=7;
    private Button btnPrevious;
    private Button btnNext;
    private Spinner spinnerServices;
    private RecyclerView timetableList;
    private TextView textView;
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
        //в currentTimetable заносятся первые 7 дней
        currentTimetable.addAll(timetable.subList(0,7));

    }


    private void restoreData(Bundle savedInstanceState) {
        saveParpmeter = savedInstanceState.getParcelable(SELECT_DOCTOR_SAVE);
        currentTimetable = savedInstanceState.getParcelableArrayList(TIMES_PARCELABLE);
    }

    private void initializeView(View rootView) {

        timeAdapter = new TimeAdapter(getActivity(), currentTimetable);
        timeAdapter.setListener(this);
        timetableList = (RecyclerView) rootView.findViewById(R.id.timetable_lst);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        timetableList.setLayoutManager(mLayoutManager);

        timetableList.setHasFixedSize(true);
       // setHeader(timetableList,saveParpmeter);
        timetableList.setAdapter(timeAdapter);

        btnPrevious = (Button) rootView.findViewById(R.id.btn_previous);
        btnPrevious.setEnabled(false);
        btnPrevious.setOnClickListener(onClickListenerBtnListner);
        btnNext = (Button) rootView.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(onClickListenerBtnListner);



    }
    private void setHeader(ListView listView,SaveParameter saveParameter){
        Bundle b=new Bundle();
        View header=getLayoutInflater(b).inflate(R.layout.header_timetable,null);


        textView=(TextView)header.findViewById(R.id.text);
        textView.setText(saveParpmeter.getSelectDoctor().getDoctorInfo().getFull_name());
        String[] organizations_name = DoctorActivity.getOrganizationName(saveParpmeter);
        int positionOrganization =DoctorActivity.getPositionOrganization(saveParpmeter)+1;
        ListView organizationsProfile = (ListView) header.findViewById(R.id.organizations_profile);
        organizationsProfile.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        organizationsProfile.setItemChecked(positionOrganization,true);
        organizationsProfile.setAdapter(new ArrayAdapter<>(
                getActivity(),
                R.layout.organization_item,
                organizations_name));

        // organizationsProfile.setSelection(posotion_organization);
        //при выборе организации Id_organization становится равным id выбранной организации
      /*  organizationsProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveParpmeter.getSelectDoctor().setId_organization(saveParpmeter.getSelectDoctor().
                        getDoctorInfo().getId_organization()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        organizationsProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveParpmeter.getSelectDoctor().setId_organization(saveParpmeter.getSelectDoctor().
                        getDoctorInfo().getId_organization()[position]);
            }
        });
        spinnerServices=(Spinner)header.findViewById(R.id.spinner_services);
        final ArrayList<String> services= getServicesNames(saveParpmeter.getServices(),
                saveParpmeter.getSelectDoctor().getDoctorInfo().getService_list().keySet());
        spinnerServices.setAdapter(new ArrayAdapter<>(getActivity()
                ,android.R.layout.simple_list_item_activated_1,
                services));
        //по умолчанию выделяется элемент, который был выбран пользователем ранее
        spinnerServices.setSelection(DoctorActivity.getPositionService(saveParpmeter));
        //при выборе услуги id_filter становится равным id услуги
        spinnerServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveParpmeter.getSelectDoctor().setId_filter(SearchActivity.findIdMap(saveParpmeter.getServices(),
                        services.get(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        listView.addHeaderView(header);
    }
    // возвращает список названий услуг для данного врача
    // получает Map всех услуг специальности и Set id услуг доктора
    private ArrayList<String> getServicesNames(Map<Integer, String> services, Set<Integer> serviceList) {
    ArrayList<String> servicesNames=new ArrayList<>();
        for (Map.Entry<Integer, String> entry : services.entrySet()) {
            if (serviceList.contains(entry.getKey()))
                servicesNames.add(entry.getValue());
        }
        return servicesNames;
    }
    //Обработка нажатий на кнопки вперед, назад
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
        //после нажатия на кнопку (вперед или назад) формирует соответствующий ArrayList
        //для отображения в listView
        public void OnButtonClick (View button,int start,int end,int extremePosition){
            currentTimetable=new ArrayList<>();
            currentTimetable.addAll(timetable.subList(start,end));
            if (button.equals(btnPrevious))position-=7;
            else position+=7;
            if (position==extremePosition) {
                button.setEnabled(false);
            }
            timeAdapter.setTimes(currentTimetable);
        }
    };
}
