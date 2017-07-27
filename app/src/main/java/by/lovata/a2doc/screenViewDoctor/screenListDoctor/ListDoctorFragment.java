package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

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
    public ListDoctorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_list_doctor, container, false);

        initializeData();

        initializeView(root_view);

        setHasOptionsMenu(true);

        return root_view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.view_filter, menu);
        inflater.inflate(R.menu.view_sort, menu);

        MenuItem view_change = menu.findItem(R.id.view_change);
        view_change.setTitle(getResources().getString(R.string.view_change_list));
        view_change.setIcon(R.drawable.ic_map_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showMenuFilter();
                return true;
            case R.id.menu_sort:
                showMenuSort();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeView(View root_view) {

        apiMethods=new APIMethods(getActivity());
        DoctorsAdapter doctorAdapter = new DoctorsAdapter(getContext(),
                saveParameter.getServices(),
                saveParameter.getOrganizations());
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

    private void showMenuFilter() {
        MenuFilterFragment dialog_filter = new MenuFilterFragment();

        Bundle bundle_filter = new Bundle();
        bundle_filter.putInt(MenuFilterFragment.ID_FILTER_SELECTED, saveParameter.getId_filter());
        bundle_filter.putBoolean(MenuFilterFragment.IS_METRO, saveParameter.isMetro());
        bundle_filter.putBoolean(MenuFilterFragment.IS_BABY, saveParameter.isBaby());

        dialog_filter.setArguments(bundle_filter);
        dialog_filter.show(getChildFragmentManager(), "filter");
    }

    private void showMenuSort() {
        MenuSortFragment dialog_sort = new MenuSortFragment();

        Bundle bundle_sort = new Bundle();
        bundle_sort.putInt(MenuSortFragment.ID_SORT_SELECTED, saveParameter.getId_sort());

        dialog_sort.setArguments(bundle_sort);
        dialog_sort.show(getChildFragmentManager(), "sort");
    }

    private void initializeData() {
        saveParameter = getArguments().getParcelable(SAVEPARAMETER_PARSALABEL);
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
