package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenRecordDoctor.RecordDoctorActivity;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;
import by.lovata.a2doc.screenViewDoctor.SelectDoctor;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts.SortDefault;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts.SortExperience;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts.SortPriceDown;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts.SortPriceUp;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDoctorFragment extends Fragment implements MenuFilterFragment.AccessFilter {

    public static final String SAVEPARAMETER_PARSALABEL = "SAVEPARAMETER_PARSALABEL";

    private static final String SAVEPARAMETER_PARSALABEL_SAVE = "SAVEPARAMETER_PARSALABEL_SAVE";

    SaveParameter saveParameter;
    RecyclerView recyclerView;
    private APIMethods apiMethods;
    Map<Integer, String> services;
    int idFilter;
    boolean metro;
    boolean baby;
    private  CheckBox checkBoxMetro;
    private  CheckBox checkBoxBaby;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    ImageView hideFilters;
    public ListDoctorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_list_doctor, container, false);

        initializeData();
        initializeFilter(root_view);
        initializeSort(root_view);
        initializeView(root_view);

        setHasOptionsMenu(true);

        return root_view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.view_settings,menu);
        MenuItem view_change = menu.findItem(R.id.view_change);
        view_change.setTitle(getResources().getString(R.string.view_change_list));
        view_change.setIcon(R.drawable.ic_map_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeView(View root_view) {
        SaveParameter newSaveParam=new SaveParameter(saveParameter.getId_city(),
                saveParameter.getId_filter(),saveParameter.getId_sort(),
                saveParameter.getIdSpeciality(),saveParameter.getDoctorsInfo(),
                saveParameter.getOrganizations(),saveParameter.getServices(),
                saveParameter.isMetro(),saveParameter.isBaby());
        apiMethods=new APIMethods(getActivity());
        DoctorsAdapter doctorAdapter = new DoctorsAdapter(getContext(),
                saveParameter.getServices(),
                saveParameter.getOrganizations(),newSaveParam);
        ArrayList<DoctorInfo> doctorInfos = createArrayWithFilter(saveParameter.getDoctorsInfo(),
                saveParameter.getId_filter(), saveParameter.isMetro(), saveParameter.isBaby());
        doctorAdapter.setArray_doctors(doctorInfos);
        doctorAdapter.setId_filter(saveParameter.getId_filter());
        doctorAdapter.setListener(new ClickOnCard());
        doctorAdapter.setApiMethods(apiMethods);
        doctorAdapter.setIdSpeciality(saveParameter.getIdSpeciality());
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerview_doctor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(doctorAdapter);
        setId_sort(saveParameter.getId_sort(), true);
        slidingUpPanelLayout=(SlidingUpPanelLayout)root_view.findViewById(R.id.filters);
        slidingUpPanelLayout.setPanelHeight(0);
        hideFilters=(ImageView)root_view.findViewById(R.id.hide);
        hideFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

            }
        });
    }

    public void setId_sort(int position, boolean status) {
        if (saveParameter.getId_sort() != position || status) {
            saveParameter.setId_sort(position);
            ArrayList<DoctorInfo> Array_doctors = ((DoctorsAdapter) recyclerView.getAdapter()).getArray_doctors();
            if (Array_doctors.size() > 1) {
                switch (position) {
                    case 0:
                        Collections.sort(Array_doctors, new SortDefault());
                        break;
                    case 1:
                        SortPriceUp sortPriceUp = new SortPriceUp(saveParameter.getId_filter());
                        Collections.sort(Array_doctors, sortPriceUp);
                        break;
                    case 2:
                        SortPriceDown sortPriceDown = new SortPriceDown(saveParameter.getId_filter());
                        Collections.sort(Array_doctors, sortPriceDown);
                        break;
                    case 3:
                        Collections.sort(Array_doctors, new SortExperience());
                        break;
                }
            }
            synchronizedDoctors();
        }
    }

    @Override
    public void setFilters(int id_filter, boolean metro, boolean baby) {
        saveParameter.setId_filter(id_filter);
        saveParameter.setMetro(metro);
        saveParameter.setBaby(baby);
        ((DoctorsAdapter) recyclerView.getAdapter()).setId_filter(id_filter);
        ((DoctorsAdapter) recyclerView.getAdapter()).
                setArray_doctors(createArrayWithFilter(saveParameter.getDoctorsInfo(), id_filter, metro, baby));
        setId_sort(saveParameter.getId_sort(), true);
    }

    @Override
    public Map<Integer, String> getServices() {
        return saveParameter.getServices();
    }

    public static ArrayList<DoctorInfo> createArrayWithFilter(ArrayList<DoctorInfo> doctorsInfo, int id_filter,
                                                              boolean is_metro, boolean is_baby) {
        ArrayList<DoctorInfo> arrayList = new ArrayList<>();
        for (DoctorInfo doctorInfo : doctorsInfo) {
            if (doctorInfo.getService_list().containsKey(id_filter)) {
                if (!is_metro) {
                    if (!is_baby) {
                        arrayList.add(doctorInfo);
                    } else {
                        if (doctorInfo.isBaby()) {
                            arrayList.add(doctorInfo);
                        }
                    }
                } else {
                    if (doctorInfo.isMerto()) {
                        arrayList.add(doctorInfo);
                    }
                }
            }
        }
        return arrayList;
    }

    private void synchronizedDoctors() {
        ((DoctorsAdapter) recyclerView.getAdapter()).synchronizedAdapter();
    }


    private void initializeData() {
        saveParameter = getArguments().getParcelable(SAVEPARAMETER_PARSALABEL);
    }
    private void initializeSort(View rootView){
        int position_item_selected = saveParameter.getId_sort();
       String[] menu_sort_items = getResources().getStringArray(R.array.menu_sort_items);
        ListView lst_sort = (ListView) rootView.findViewById(R.id.lst_sort);
        lst_sort.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_single_choice,
                menu_sort_items
        ));
        lst_sort.setItemChecked(position_item_selected, true);

        lst_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveParameter.setId_sort(position);
            }
        });
    }
    private void initializeFilter(View viewRoot){

        services = saveParameter.getServices();
        idFilter = saveParameter.getId_filter();
        metro = saveParameter.isMetro();
        baby = saveParameter.isBaby();
        checkBoxMetro = (CheckBox) viewRoot.findViewById(R.id.filterCheckBox2);
        checkBoxMetro.setChecked(metro);

        checkBoxBaby = (CheckBox) viewRoot.findViewById(R.id.filterCheckBox1);
        checkBoxBaby.setChecked(baby);

            Set<Integer> set_key_services = services.keySet();
            final Integer[] key_services = set_key_services.toArray(new Integer[set_key_services.size()]);

            final Spinner spinner = (Spinner) viewRoot.findViewById(R.id.services_spinner);
            spinner.setAdapter(new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    new ArrayList<>(services.values())));
            int position = getPosition(key_services, idFilter);
            spinner.setSelection(position);

            Button applyFilters = (Button) viewRoot.findViewById(R.id.filterApplyButton);
            applyFilters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idFilter = key_services[spinner.getSelectedItemPosition()];
                    metro = checkBoxMetro.isChecked();
                    baby = checkBoxBaby.isChecked();
                    setFilters(idFilter, metro, baby);
                    setId_sort(saveParameter.getId_sort(),false);
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
            });
        }
    private int getPosition(Integer[] key_services, int id_filter) {
        int position = 0;
        for (Integer key : key_services) {
            if (key == id_filter) {
                break;
            }
            position++;
        }
        return position;
    }
    private class ClickOnCard implements DoctorsAdapter.Listener {

        @Override
        public void onClickRecord(int id_doctor, int id_filter, int id_organization) {
            Intent intent = new Intent(getActivity(), RecordDoctorActivity.class);
            DoctorInfo doctorInfo = getDoctor(id_doctor);
            saveParameter.setSelectDoctor(new SelectDoctor(id_doctor, id_filter, id_organization, null, null, doctorInfo));
            intent.putExtra(RecordDoctorActivity.SAVEPARAMETER_PARSALABEL, saveParameter);

            getActivity().startActivity(intent);
        }

        @Override
        public void onClickDoctor(int id_doctor, int id_filter, int id_organization) {
            Intent intent = new Intent(getActivity(), DoctorActivity.class);
            DoctorInfo doctorInfo = getDoctor(id_doctor);

            saveParameter.setSelectDoctor(new SelectDoctor(id_doctor, id_filter, id_organization, null, null, doctorInfo));

            intent.putExtra(DoctorActivity.SAVEPARAMETER_PARSALABEL, saveParameter);

            getActivity().startActivity(intent);
        }

        private DoctorInfo getDoctor(int id_doctor) {
            for (DoctorInfo doctorInfo : saveParameter.getDoctorsInfo()) {
                if (doctorInfo.getId() == id_doctor) {
                    return doctorInfo;
                }
            }
            return null;
        }
    }

}
