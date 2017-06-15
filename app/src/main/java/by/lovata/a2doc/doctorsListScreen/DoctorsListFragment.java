package by.lovata.a2doc.doctorsListScreen;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import by.lovata.a2doc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsListFragment extends Fragment {


    public DoctorsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_doctors_list, container, false);

        ArrayList<DoctorInfo> arrayList = new ArrayList<>();
        arrayList.add(new DoctorInfo("1", "1", "1", "1", "1", "1"));
        arrayList.add(new DoctorInfo("2", "2", "2", "2", "2", "2"));
        arrayList.add(new DoctorInfo("3", "3", "3", "3", "3", "3"));
        arrayList.add(new DoctorInfo("4", "4", "4", "4", "4", "4"));
        arrayList.add(new DoctorInfo("5", "5", "5", "5", "5", "5"));

        DoctorsAdapter doctorsAdapter = new DoctorsAdapter(arrayList);

        RecyclerView recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerview_doctor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(doctorsAdapter);

        return root_view;
    }

}
