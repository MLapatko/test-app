package by.lovata.a2doc.screenRecordDoctor.screenYourInformation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourInformationFragment extends Fragment {

    public static String ID_DOCTOR_SELECTED = "ID_DOCTOR_SELECTED";
    public static String ID_FILTER_SELECTED = "ID_FILTER_SELECTED";
    public static String ID_ORGANIZATION_SELECTED = "ID_ORGANIZATION_SELECTED";
    public static String DAY_SELECTED = "DAY_SELECTED";
    public static String TIME_SELECTED = "TIME_SELECTED";
    public static String SAVE_INFORMATION_PARCELABLE = "SAVE_INFORMATION_PARCELABLE";

    public static String ID_DOCTOR_SELECTED_SAVE = "ID_DOCTOR_SELECTED_SAVE";
    public static String ID_FILTER_SELECTED_SAVE = "ID_FILTER_SELECTED_SAVE";
    public static String ID_ORGANIZATION_SELECTED_SAVE = "ID_ORGANIZATION_SELECTED_SAVE";
    public static String DAY_SELECTED_SAVE = "DAY_SELECTED_SAVE";
    public static String TIME_SELECTED_SAVE = "TIME_SELECTED_SAVE";
    public static String SAVE_INFORMATION_PARCELABLE_SAVE = "SAVE_INFORMATION_PARCELABLE_SAVE";

    SaveParameter saveParameter;

    int id_doctor;
    int id_filter;
    int id_organization;
    String day;
    String time;

    public YourInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view =  inflater.inflate(R.layout.fragment_your_information, container, false);

        if (savedInstanceState == null) {
            saveParameter = getArguments().getParcelable(SAVE_INFORMATION_PARCELABLE);

            id_doctor = getArguments().getInt(ID_DOCTOR_SELECTED, 0);
            id_filter = getArguments().getInt(ID_FILTER_SELECTED, 0);
            id_organization = getArguments().getInt(ID_ORGANIZATION_SELECTED, 0);

            day = getArguments().getString(DAY_SELECTED);
            time = getArguments().getString(TIME_SELECTED);
        } else {
            saveParameter = savedInstanceState.getParcelable(SAVE_INFORMATION_PARCELABLE_SAVE);

            id_doctor = savedInstanceState.getInt(ID_DOCTOR_SELECTED_SAVE);
            id_filter = savedInstanceState.getInt(ID_FILTER_SELECTED_SAVE);
            id_organization = savedInstanceState.getInt(ID_ORGANIZATION_SELECTED_SAVE);

            day = savedInstanceState.getString(DAY_SELECTED_SAVE);
            time = savedInstanceState.getString(TIME_SELECTED_SAVE);
        }

        String name = null;
        Integer price = null;
        String gps = saveParameter.organizations.get(id_organization).name;
        for (DoctorInfo doctorInfo: saveParameter.doctorsInfo) {
            if (doctorInfo.id == id_doctor) {
                name = doctorInfo.full_name;
                price = doctorInfo.service_list.get(id_filter);
            }
        }

        TextView doctor_name = (TextView) root_view.findViewById(R.id.doctor_name);
        doctor_name.setText(name);

        TextView doctor_gps = (TextView) root_view.findViewById(R.id.doctor_gps);
        doctor_gps.setText(gps);

        TextView doctor_date = (TextView) root_view.findViewById(R.id.doctor_date);
        doctor_date.setText(day + time);

        TextView doctor_price = (TextView) root_view.findViewById(R.id.doctor_price);
        doctor_price.setText(Integer.toString(price));


        return root_view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVE_INFORMATION_PARCELABLE_SAVE, saveParameter);

        outState.putInt(ID_DOCTOR_SELECTED_SAVE, id_doctor);
        outState.putInt(ID_FILTER_SELECTED_SAVE, id_filter);
        outState.putInt(ID_ORGANIZATION_SELECTED_SAVE, id_organization);

        outState.putString(DAY_SELECTED_SAVE, day);
        outState.putString(TIME_SELECTED_SAVE, time);
    }

}
