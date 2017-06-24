package by.lovata.a2doc.screenRecordDoctor;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor.TimetableDoctorFragment;

public class RecordDoctorActivity extends AppCompatActivity {

    public static String ID_DOCTOR_SELECTED = "ID_DOCTOR_SELECTED";
    public static String ID_FILTER_SELECTED = "ID_FILTER_SELECTED";
    public static String ID_ORGANIZATION_SELECTED = "ID_ORGANIZATION_SELECTED";

    public static String ID_DOCTOR_SELECTED_SAVE = "ID_DOCTOR_SELECTED_SAVE";
    public static String ID_FILTER_SELECTED_SAVE = "ID_FILTER_SELECTED_SAVE";
    public static String ID_ORGANIZATION_SELECTED_SAVE = "ID_ORGANIZATION_SELECTED_SAVE";

    int id_doctor;
    int id_filter;
    int id_organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_doctor);

        if (savedInstanceState == null) {
            id_doctor = getIntent().getIntExtra(ID_DOCTOR_SELECTED, 0);
            id_filter = getIntent().getIntExtra(ID_FILTER_SELECTED, 0);
            id_organization = getIntent().getIntExtra(ID_ORGANIZATION_SELECTED, 0);
        } else {
            id_doctor = savedInstanceState.getInt(ID_DOCTOR_SELECTED_SAVE);
            id_filter = savedInstanceState.getInt(ID_FILTER_SELECTED_SAVE);
            id_organization = savedInstanceState.getInt(ID_ORGANIZATION_SELECTED_SAVE);
        }

        Fragment fragment = new TimetableDoctorFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_record, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt(ID_DOCTOR_SELECTED_SAVE, id_doctor);
        outState.putInt(ID_FILTER_SELECTED_SAVE, id_filter);
        outState.putInt(ID_ORGANIZATION_SELECTED_SAVE, id_organization);
    }

}
