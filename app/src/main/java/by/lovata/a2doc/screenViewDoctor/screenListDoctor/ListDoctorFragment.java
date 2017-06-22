package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.app.Activity;
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

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenRecordDoctor.RecordDoctorActivity;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.InformationInterface;
import by.lovata.a2doc.screenViewDoctor.ViewDoctorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDoctorFragment extends Fragment {

    DoctorInfo[] doctorsInfo;
    InformationInterface informationInterface;

    public static final String ID_SORT_SELECTED = "ID_SORT_SELECTED";

    RecyclerView recyclerView;
    int id_sort;
    int id_spiciality;


    public ListDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_list_doctor, container, false);

        id_spiciality = getArguments().getInt(ViewDoctorActivity.ID_SPECIALITY_SELECTED);

        doctorsInfo = informationInterface.getDoctors();
        DoctorsAdapter doctorAdapter = new DoctorsAdapter(doctorsInfo, getContext());
        doctorAdapter.setListener(new ClickOnCard());

        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerview_doctor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(doctorAdapter);

        if (savedInstanceState != null) {
            id_sort = savedInstanceState.getInt(ID_SORT_SELECTED, 0);
        } else {
            id_sort = 0;
        }

        setHasOptionsMenu(true);

        return root_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.informationInterface = (InformationInterface) activity;
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
                MenuFilterFragment dialog_filter = new MenuFilterFragment();
                dialog_filter.show(getChildFragmentManager(), "filter");
                return true;
            case R.id.menu_sort:
                MenuSortFragment dialog_sort = new MenuSortFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(ID_SORT_SELECTED, id_sort);
                dialog_sort.setArguments(bundle);
                dialog_sort.show(getChildFragmentManager(), "sort");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setId_sort(int position) {
        id_sort = position;
        synchronizedDoctors();
    }

    private void synchronizedDoctors() {
//        APIMethods apiMethods = new APIMethods(getContext());
//        String s_doctorsInfo = apiMethods.loadDoctorsInfoFromJSON(R.raw.doctors, id_spiciality, 0);
//        doctorsInfo = apiMethods.parseDoctorsInfoFromJSON(s_doctorsInfo, "doctors");
//        ((DoctorsAdapter) recyclerView.getAdapter()).synchronizedAdapter(doctorsInfo);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ID_SORT_SELECTED, id_sort);
    }

    class ClickOnCard implements DoctorsAdapter.Listener {

        @Override
        public void onClickRecord(int position) {
            Intent intent = new Intent(getActivity(), RecordDoctorActivity.class);
            getActivity().startActivity(intent);
        }

        @Override
        public void onClickDoctor(int position) {
            Intent intent = new Intent(getActivity(), DoctorActivity.class);
            getActivity().startActivity(intent);
        }
    }

}
