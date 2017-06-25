package by.lovata.a2doc.screenDoctor;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenRecordDoctor.RecordDoctorActivity;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;
import by.lovata.a2doc.screenViewDoctor.SelectDoctor;

public class DoctorActivity extends AppCompatActivity {

    public static final String SAVEPARAMETER_PARSALABEL = "SAVEPARAMETER_PARSALABEL";

    public static final String SAVEPARAMETER_PARSALABEL_SAVE = "SAVEPARAMETER_PARSALABEL_SAVE";
    public static final String ID_ORGANIZATION_SAVE = "ID_ORGANIZATION_SAVE";
    public static final String ID_FILTER_SAVE = "ID_FILTER_SAVE";

    SaveParameter saveParameter;
    int id_organization;
    int id_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        if (savedInstanceState == null) {
            saveParameter = getIntent().getParcelableExtra(SAVEPARAMETER_PARSALABEL);
            id_organization = saveParameter.getSelectDoctor().getId_organization();
            id_filter = saveParameter.getSelectDoctor().getId_filter();
        } else {
            saveParameter = savedInstanceState.getParcelable(SAVEPARAMETER_PARSALABEL_SAVE);
            id_organization = savedInstanceState.getInt(ID_ORGANIZATION_SAVE);
            id_filter = savedInstanceState.getInt(ID_FILTER_SAVE);

        }

        initialView();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVEPARAMETER_PARSALABEL_SAVE, saveParameter);
        outState.putInt(ID_ORGANIZATION_SAVE, id_organization);
        outState.putInt(ID_FILTER_SAVE, id_filter);
    }

    private void initialView() {

        final Map<String, String> informations = getInformations();

        ImageView img_profile = (ImageView) findViewById(R.id.img_profile);
        Picasso.with(this)
                .load(informations.get("img"))
                .placeholder(R.drawable.ic_file_download_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .into(img_profile);

        TextView fio_profile = (TextView) findViewById(R.id.fio_profile);
        fio_profile.setText(informations.get("name"));

        TextView speciality_profile = (TextView) findViewById(R.id.speciality_profile);
        speciality_profile.setText(informations.get("speciality"));

        TextView expirience_profile = (TextView) findViewById(R.id.expirience_profile);
        expirience_profile.setText(informations.get("expirience"));

        String[] organizations_name = getOrganizationName(Integer.valueOf(informations.get("position")));
        Spinner organizations_profile = (Spinner) findViewById(R.id.organizations_profile);
        organizations_profile.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_activated_1,
                organizations_name));
        int posotion_organization = getPositionOrganization(Integer.valueOf(informations.get("position")),
                saveParameter.getSelectDoctor().getId_organization());
        organizations_profile.setSelection(posotion_organization);
        organizations_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_organization = saveParameter.getDoctorsInfo()
                        [Integer.valueOf(informations.get("position"))].getId_organization()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final TextView price_profile = (TextView) findViewById(R.id.price_profile);
        price_profile.setText(String.valueOf(saveParameter.getDoctorsInfo()
                [Integer.valueOf(informations.get("position"))].getService_list().
                get(id_filter)));

        String[] services_name = getServicesName(Integer.valueOf(informations.get("position")));
        Spinner services_profile = (Spinner) findViewById(R.id.services_profile);
        services_profile.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_activated_1,
                services_name));
        int posotion_service = getPositionService(Integer.valueOf(informations.get("position")),
                saveParameter.getSelectDoctor().getId_filter());
        services_profile.setSelection(posotion_service);
        services_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Set<Integer> set_id_filter = saveParameter.getDoctorsInfo()
                        [Integer.valueOf(informations.get("position"))].
                        getService_list().keySet();
                id_filter = set_id_filter.toArray(new Integer[set_id_filter.size()])[position];
                price_profile.setText(String.valueOf(saveParameter.getDoctorsInfo()
                        [Integer.valueOf(informations.get("position"))].getService_list().
                        get(id_filter)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btn_record = (Button) findViewById(R.id.btn_record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorActivity.this, RecordDoctorActivity.class);
                SelectDoctor selectDoctor = saveParameter.getSelectDoctor();
                selectDoctor.setId_organization(id_organization);
                selectDoctor.setId_filter(id_filter);
                saveParameter.setSelectDoctor(selectDoctor);
                intent.putExtra(RecordDoctorActivity.SAVEPARAMETER_PARSALABEL, saveParameter);

                startActivity(intent);
            }
        });
    }

    private Map<String, String> getInformations() {
        Map<String , String> map = new HashMap<>();
        int position = 0;

        for (DoctorInfo doctorInfo: saveParameter.getDoctorsInfo()) {
            if (doctorInfo.getId() == saveParameter.getSelectDoctor().getId_doctor()) {
                map.put("name", doctorInfo.getFull_name());
                map.put("speciality", doctorInfo.getSpeciality());
                map.put("img", doctorInfo.getUrl_img());
                map.put("expirience", Integer.toString(doctorInfo.getExperience()));
                map.put("position", Integer.toString(position));
                break;
            }
            position++;
        }
        return map;
    }

    private String[] getOrganizationName(int position) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (int id: saveParameter.getDoctorsInfo()[position].getId_organization()) {
            arrayList.add(saveParameter.getOrganizations().get(id).getName());
        }

        return arrayList.toArray(new String[arrayList.size()]);
    }

    private int getPositionOrganization(int position, int id_organization) {
        int position_organization = 0;

        for (int key: saveParameter.getDoctorsInfo()[position].getId_organization()) {
            if (key == id_organization) {
                break;
            }
            position_organization++;
        }

        return position_organization;
    }

    private String[] getServicesName(int position) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (int id: saveParameter.getDoctorsInfo()[position].getService_list().keySet()) {
            arrayList.add(saveParameter.getServices().get(id));
        }

        return arrayList.toArray(new String[arrayList.size()]);
    }

    private int getPositionService(int position, int id_filter) {
        int position_service = 0;

        for (int key: saveParameter.getDoctorsInfo()[position].getService_list().keySet()) {
            if (key == id_filter) {
                break;
            }
            position_service++;
        }

        return position_service;
    }
}
