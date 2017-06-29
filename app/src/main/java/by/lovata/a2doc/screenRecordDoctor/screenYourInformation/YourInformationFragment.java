package by.lovata.a2doc.screenRecordDoctor.screenYourInformation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews.ReviewWriteActivity;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourInformationFragment extends Fragment {

    public static final String SAVEPARAMETER_PARSALABEL = "SAVEPARAMETER_PARSALABEL";


    SaveParameter saveParameter;

    public YourInformationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_your_information, container, false);

        initializeData();

        initializeView(root_view);

        return root_view;
    }

    private void initializeData() {
        saveParameter = getArguments().getParcelable(SAVEPARAMETER_PARSALABEL);
    }

    private void initializeView(View root_view) {

        String name = getNameDoctor();
        TextView doctor_name = (TextView) root_view.findViewById(R.id.doctor_name);
        doctor_name.setText(name);

        String gps = getGPSDoctor();
        TextView doctor_gps = (TextView) root_view.findViewById(R.id.doctor_gps);
        doctor_gps.setText(gps);

        String service = getServiceDoctor();
        TextView doctor_service = (TextView) root_view.findViewById(R.id.doctor_service);
        doctor_service.setText(service);

        String date = getDateDoctor();
        TextView doctor_date = (TextView) root_view.findViewById(R.id.doctor_date);
        doctor_date.setText(date);

        String price = getPriceDoctor();
        TextView doctor_price = (TextView) root_view.findViewById(R.id.doctor_price);
        doctor_price.setText(price);

        Button button_send = (Button) root_view.findViewById(R.id.record_send_to_server);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messanger = getString(R.string.add_review_sent);
                Toast.makeText(getContext(), messanger, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
    }

    private String getNameDoctor() {
        return saveParameter.getSelectDoctor().getDoctorInfo().getFull_name();
    }

    private String getGPSDoctor() {
        return saveParameter.getOrganizations().get(
                saveParameter.getSelectDoctor().getId_organization()).getName();
    }

    private String getServiceDoctor() {
        String service_label = getString(R.string.your_information_service);
        String service = String.format("%s %s", service_label, saveParameter.getServices().get(
                saveParameter.getSelectDoctor().getId_filter()));
        return service;
    }

    private String getDateDoctor() {
        String date_label = getString(R.string.your_information_date);
        String date = String.format("%s %s, %s", date_label, saveParameter.getSelectDoctor().getDay(),
                saveParameter.getSelectDoctor().getTime());
        return date;
    }

    private String getPriceDoctor() {
        String price_label = getString(R.string.your_information_price);
        String price = String.format("%s %s р.", price_label, saveParameter.getSelectDoctor().getDoctorInfo()
                .getService_list().get(saveParameter.getSelectDoctor().getId_filter()));
        return price;
    }
}
