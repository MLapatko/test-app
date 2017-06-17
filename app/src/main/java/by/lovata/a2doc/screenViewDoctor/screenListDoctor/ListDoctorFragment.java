package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenRecordDoctor.RecordDoctorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDoctorFragment extends Fragment {


    public ListDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_list_doctor, container, false);

        ArrayList<DoctorInfo> arrayList = new ArrayList<>();
        arrayList.add(new DoctorInfo(R.mipmap.ic_launcher, "Красовский Дмитрий Михайлович",
                "Окулист", "Консультация окулиста\nот 20.00 до 30.00",
                "Оказывает еще 5 услуг", "Частная практика, ул. Одинцова 97, пом 76",
                "5 отз."));
        arrayList.add(new DoctorInfo(R.mipmap.ic_launcher, "Красовский Дмитрий Михайлович",
                "Окулист", "Консультация окулиста\nот 20.00 до 30.00",
                "Оказывает еще 5 услуг", "Частная практика, ул. Одинцова 97, пом 76",
                "5 отз."));
        arrayList.add(new DoctorInfo(R.mipmap.ic_launcher, "Красовский Дмитрий Михайлович",
                "Окулист", "Консультация окулиста\nот 20.00 до 30.00",
                "Оказывает еще 5 услуг", "Частная практика, ул. Одинцова 97, пом 76",
                "5 отз."));
        arrayList.add(new DoctorInfo(R.mipmap.ic_launcher, "Красовский Дмитрий Михайлович",
                "Окулист", "Консультация окулиста\nот 20.00 до 30.00",
                "Оказывает еще 5 услуг", "Частная практика, ул. Одинцова 97, пом 76",
                "5 отз."));
        arrayList.add(new DoctorInfo(R.mipmap.ic_launcher, "Красовский Дмитрий Михайлович",
                "Окулист", "Консультация окулиста\nот 20.00 до 30.00",
                "Оказывает еще 5 услуг", "Частная практика, ул. Одинцова 97, пом 76",
                "5 отз."));

        DoctorsAdapter doctorAdapter = new DoctorsAdapter(arrayList);
        doctorAdapter.setListener(new ClickOnCard());

        RecyclerView recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerview_doctor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(doctorAdapter);

        return root_view;
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
