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
import java.util.Map;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenRecordDoctor.RecordDoctorActivity;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.OrganizationInfo;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;
import by.lovata.a2doc.screenViewDoctor.ViewDoctorActivity;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts.SortDefault;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts.SortExperience;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts.SortPriceDown;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts.SortPriceUp;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDoctorFragment extends Fragment implements MenuFilterFragment.AccessFilter {

    public static interface InformationInterfaceList {
        public DoctorInfo[] getDoctors();
        public Map<Integer, String> getSevices();
        public Map<Integer, OrganizationInfo> getOrganization();
        public void setId_sort(int id_sort_selected);
        public void setFilters(int id_filter, boolean metro, boolean baby);
        public SaveParameter getSaveParameter();
    }

    public static final String ID_SORT_SELECTED = "ID_SORT_SELECTED";
    public static final String ID_FILTER_SELECTED = "ID_FILTER_SELECTED";
    public static final String IS_METRO = "IS_METRO";
    public static final String IS_BABY = "IS_BABY";

    public static final String ID_SORT_SELECTED_SAVE = "ID_SORT_SELECTED_SAVE";
    public static final String ID_FILTER_SELECTED_SAVE = "ID_FILTER_SELECTED_SAVE";
    public static final String IS_METRO_SAVE = "IS_METRO_SAVE";
    public static final String IS_BABY_SAVE = "IS_BABY_SAVE";

    RecyclerView recyclerView;
    DoctorInfo[] doctorsInfo;
    InformationInterfaceList informationInterface;

    int id_sort;
    int id_filter;

    boolean metro;
    boolean baby;

    public ListDoctorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_list_doctor, container, false);

        if (savedInstanceState == null) {
            initializeData();
        } else {
            restoreData(savedInstanceState);
        }

        doctorsInfo = informationInterface.getDoctors();
        DoctorsAdapter doctorAdapter = new DoctorsAdapter(getContext(),
                informationInterface.getSevices(),
                informationInterface.getOrganization());
        doctorAdapter.setArray_doctors(createArrayWithFilter(doctorsInfo, id_filter, metro, baby));
        doctorAdapter.setId_filter(id_filter);
        doctorAdapter.setListener(new ClickOnCard());

        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerview_doctor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(doctorAdapter);

        setHasOptionsMenu(true);

        return root_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.informationInterface = (InformationInterfaceList) activity;
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

    public void setId_sort(int position) {
        if (id_sort != position) {
            id_sort = position;
            DoctorInfo[] Array_doctors = ((DoctorsAdapter) recyclerView.getAdapter()).getArray_doctors();
            if (Array_doctors.length > 1) {
                switch (position) {
                    case 0:
                        Arrays.sort(doctorsInfo, new SortDefault());
                        break;
                    case 1:
                         SortPriceUp sortPriceUp = new SortPriceUp(id_filter);
                         Arrays.sort(Array_doctors, sortPriceUp);
                        break;
                    case 2:
                        SortPriceDown sortPriceDown = new SortPriceDown(id_filter);
                        Arrays.sort(Array_doctors, sortPriceDown);
                        break;
                    case 3:
                        Arrays.sort(doctorsInfo, new SortExperience());
                        break;
                }
                informationInterface.setId_sort(position);
                synchronizedDoctors();
            }
        }
    }

    @Override
    public void setFilters(int id_filter, boolean metro, boolean baby) {
        this.id_filter = id_filter;
        this.metro = metro;
        this.baby = baby;
        this.id_sort = 0;

        informationInterface.setFilters(id_filter, metro, baby);
        informationInterface.setId_sort(id_sort);

        if (doctorsInfo.length > 1) {
            Arrays.sort(doctorsInfo, new SortDefault());
        }
        ((DoctorsAdapter) recyclerView.getAdapter()).setId_filter(id_filter);
        ((DoctorsAdapter) recyclerView.getAdapter()).
                setArray_doctors(createArrayWithFilter(doctorsInfo, id_filter, metro, baby));

        synchronizedDoctors();
    }

    @Override
    public Map<Integer, String> getSevices() {
        return informationInterface.getSevices();
    }

    private DoctorInfo[] createArrayWithFilter(DoctorInfo[] doctorsInfo, int id_filter,
                                               boolean is_metro, boolean is_baby) {
        ArrayList<DoctorInfo> arrayList = new ArrayList<>();
        for (DoctorInfo doctorInfo: doctorsInfo) {
            if (doctorInfo.service_list.containsKey(id_filter)) {
                if (!is_metro) {
                    if (!is_baby) {
                        arrayList.add(doctorInfo);
                    } else {
                        if (doctorInfo.baby) {
                            arrayList.add(doctorInfo);
                        }
                    }
                } else {
                    if (doctorInfo.merto) {
                        arrayList.add(doctorInfo);
                    }
                }
            }
        }
        return arrayList.toArray(new DoctorInfo[arrayList.size()]);
    }

    private void synchronizedDoctors() {
        ((DoctorsAdapter) recyclerView.getAdapter()).synchronizedAdapter();
    }

    private void showMenuFilter() {
        MenuFilterFragment dialog_filter = new MenuFilterFragment();
        Bundle bundle_filter = new Bundle();
        bundle_filter.putInt(MenuFilterFragment.ID_FILTER_SELECTED, id_filter);
        bundle_filter.putBoolean(MenuFilterFragment.IS_METRO, metro);
        bundle_filter.putBoolean(MenuFilterFragment.IS_BABY, baby);
        dialog_filter.setArguments(bundle_filter);
        dialog_filter.show(getChildFragmentManager(), "filter");
    }

    private void showMenuSort() {
        MenuSortFragment dialog_sort = new MenuSortFragment();
        Bundle bundle_sort = new Bundle();
        bundle_sort.putInt(MenuSortFragment.ID_SORT_SELECTED, id_sort);
        dialog_sort.setArguments(bundle_sort);
        dialog_sort.show(getChildFragmentManager(), "sort");
    }

    private void initializeData() {
        id_sort = getArguments().getInt(ID_SORT_SELECTED);
        id_filter = getArguments().getInt(ID_FILTER_SELECTED);

        metro = getArguments().getBoolean(IS_METRO);
        baby = getArguments().getBoolean(IS_BABY);
    }

    private void restoreData(Bundle savedInstanceState) {
        id_sort = savedInstanceState.getInt(ID_SORT_SELECTED_SAVE);
        id_filter = savedInstanceState.getInt(ID_FILTER_SELECTED_SAVE);

        metro = savedInstanceState.getBoolean(IS_METRO_SAVE);
        baby = savedInstanceState.getBoolean(IS_BABY_SAVE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ID_SORT_SELECTED_SAVE, id_sort);
        outState.putInt(ID_FILTER_SELECTED_SAVE, id_filter);

        outState.putBoolean(IS_METRO_SAVE, metro);
        outState.putBoolean(IS_BABY_SAVE, baby);
    }

    private class ClickOnCard implements DoctorsAdapter.Listener {

        @Override
        public void onClickRecord(int id_doctor, int id_filter, int id_organization) {
            Intent intent = new Intent(getActivity(), RecordDoctorActivity.class);
            intent.putExtra(RecordDoctorActivity.ID_DOCTOR_SELECTED, id_doctor);
            intent.putExtra(RecordDoctorActivity.ID_FILTER_SELECTED, id_filter);
            intent.putExtra(RecordDoctorActivity.ID_ORGANIZATION_SELECTED, id_organization);
            intent.putExtra(RecordDoctorActivity.SAVE_INFORMATION_PARCELABLE, informationInterface.getSaveParameter());
            Log.w("MYLOG", informationInterface.getSaveParameter().doctorsInfo[0].full_name);

            getActivity().startActivity(intent);
        }

        @Override
        public void onClickDoctor(int position) {
            Intent intent = new Intent(getActivity(), DoctorActivity.class);
            getActivity().startActivity(intent);
        }
    }

}
