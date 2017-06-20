package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenRecordDoctor.RecordDoctorActivity;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.InterfaceDoctors;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDoctorFragment extends Fragment {

    DoctorInfo[] doctorsInfo;
    InterfaceDoctors doctorsInterface;

    public ListDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_list_doctor, container, false);

        doctorsInfo = doctorsInterface.getDoctors();
        DoctorsAdapter doctorAdapter = new DoctorsAdapter(doctorsInfo);
        doctorAdapter.setListener(new ClickOnCard());

        RecyclerView recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerview_doctor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(doctorAdapter);

        return root_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.doctorsInterface = (InterfaceDoctors) activity;
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
