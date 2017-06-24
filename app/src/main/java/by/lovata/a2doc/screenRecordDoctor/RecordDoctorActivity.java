package by.lovata.a2doc.screenRecordDoctor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor.TimetableDoctorFragment;
import by.lovata.a2doc.screenRecordDoctor.screenYourInformation.YourInformationFragment;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;

public class RecordDoctorActivity extends AppCompatActivity implements
        TimetableDoctorFragment.RecordDoctor {

    public static String ID_DOCTOR_SELECTED = "ID_DOCTOR_SELECTED";
    public static String ID_FILTER_SELECTED = "ID_FILTER_SELECTED";
    public static String ID_ORGANIZATION_SELECTED = "ID_ORGANIZATION_SELECTED";
    public static String SAVE_INFORMATION_PARCELABLE = "SAVE_INFORMATION_PARCELABLE";

    public static String ID_DOCTOR_SELECTED_SAVE = "ID_DOCTOR_SELECTED_SAVE";
    public static String ID_FILTER_SELECTED_SAVE = "ID_FILTER_SELECTED_SAVE";
    public static String ID_ORGANIZATION_SELECTED_SAVE = "ID_ORGANIZATION_SELECTED_SAVE";
    public static String SAVE_INFORMATION_PARCELABLE_SAVE = "SAVE_INFORMATION_PARCELABLE_SAVE";


    int id_doctor;
    int id_filter;
    int id_organization;

    SaveParameter saveParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_doctor);

        if (savedInstanceState == null) {
            saveParameter = getIntent().getParcelableExtra(SAVE_INFORMATION_PARCELABLE);
            Log.w("MYLOG", saveParameter.doctorsInfo[0].full_name);

            id_doctor = getIntent().getIntExtra(ID_DOCTOR_SELECTED, 0);
            id_filter = getIntent().getIntExtra(ID_FILTER_SELECTED, 0);
            id_organization = getIntent().getIntExtra(ID_ORGANIZATION_SELECTED, 0);
        } else {
            saveParameter = savedInstanceState.getParcelable(SAVE_INFORMATION_PARCELABLE_SAVE);

            id_doctor = savedInstanceState.getInt(ID_DOCTOR_SELECTED_SAVE);
            id_filter = savedInstanceState.getInt(ID_FILTER_SELECTED_SAVE);
            id_organization = savedInstanceState.getInt(ID_ORGANIZATION_SELECTED_SAVE);
        }

        Fragment fragment = new TimetableDoctorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TimetableDoctorFragment.ID_DOCTOR_SELECTED, id_doctor);
        bundle.putInt(TimetableDoctorFragment.ID_FILTER_SELECTED, id_filter);
        bundle.putInt(TimetableDoctorFragment.ID_ORGANIZATION_SELECTED, id_organization);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_record, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVE_INFORMATION_PARCELABLE_SAVE, saveParameter);

        outState.putInt(ID_DOCTOR_SELECTED_SAVE, id_doctor);
        outState.putInt(ID_FILTER_SELECTED_SAVE, id_filter);
        outState.putInt(ID_ORGANIZATION_SELECTED_SAVE, id_organization);
    }


    @Override
    public void showYourInformation(String day_selected, String time_selected) {
        Fragment fragment = new YourInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(YourInformationFragment.ID_DOCTOR_SELECTED, id_doctor);
        bundle.putInt(YourInformationFragment.ID_FILTER_SELECTED, id_filter);
        bundle.putInt(YourInformationFragment.ID_ORGANIZATION_SELECTED, id_organization);
        bundle.putString(YourInformationFragment.DAY_SELECTED, day_selected);
        bundle.putString(YourInformationFragment.TIME_SELECTED, time_selected);
        bundle.putParcelable(YourInformationFragment.SAVE_INFORMATION_PARCELABLE, saveParameter);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_record, fragment);
        fragmentTransaction.commit();
    }
}
