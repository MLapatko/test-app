package by.lovata.a2doc.screenRecordDoctor.screenYourInformation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourInformationFragment extends Fragment {

    public static String SAVEPARAMETER_PARSALABEL = "SAVEPARAMETER_PARSALABEL";
    public static String SAVEPARAMETER_PARSALABEL_SAVE = "SAVEPARAMETER_PARSALABEL_SAVE";

    SaveParameter saveParameter;

    public YourInformationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view =  inflater.inflate(R.layout.fragment_your_information, container, false);

        if (savedInstanceState == null) {
            initializeData();
        } else {
            restoreData(savedInstanceState);
        }

        Map<String, String> informations = getInformations();

       initializeView(root_view, informations);

        return root_view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVEPARAMETER_PARSALABEL_SAVE, saveParameter);
    }

    private void initializeData() {
        saveParameter = getArguments().getParcelable(SAVEPARAMETER_PARSALABEL);
    }

    private void restoreData(Bundle savedInstanceState) {
        saveParameter = savedInstanceState.getParcelable(SAVEPARAMETER_PARSALABEL_SAVE);
    }

    private Map<String, String> getInformations() {
        Map<String , String> map = new HashMap<>();

        map.put("gps", saveParameter.getOrganizations().get(
                saveParameter.getSelectDoctor().getId_organization()).getName());

        for (DoctorInfo doctorInfo: saveParameter.getDoctorsInfo()) {
            if (doctorInfo.getId() == saveParameter.getSelectDoctor().getId_doctor()) {
                map.put("name", doctorInfo.getFull_name());
                map.put("service", saveParameter.getServices().get(
                        saveParameter.getSelectDoctor().getId_filter()));
                map.put("price", String.format("Цена: %s", doctorInfo.getService_list().get(
                        saveParameter.getSelectDoctor().getId_filter())));
            }
        }
        return map;
    }

    private void initializeView(View root_view, Map<String, String> informations) {

        TextView doctor_name = (TextView) root_view.findViewById(R.id.doctor_name);
        doctor_name.setText(informations.get("name"));

        TextView doctor_gps = (TextView) root_view.findViewById(R.id.doctor_gps);
        doctor_gps.setText(informations.get("gps"));

        TextView doctor_service = (TextView) root_view.findViewById(R.id.doctor_service);
        doctor_service.setText(informations.get("service"));

        TextView doctor_date = (TextView) root_view.findViewById(R.id.doctor_date);
        doctor_date.setText(String.format("Дата %s: %s",saveParameter.getSelectDoctor().getDay(),
                saveParameter.getSelectDoctor().getTime()));

        TextView doctor_price = (TextView) root_view.findViewById(R.id.doctor_price);
        doctor_price.setText(informations.get("price"));
    }

}
