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
import by.lovata.a2doc.screenViewDoctor.SelectDoctor;

public class RecordDoctorActivity extends AppCompatActivity implements
        TimetableDoctorFragment.RecordDoctor {

    public static String SAVEPARAMETER_PARSALABEL = "SAVEPARAMETER_PARSALABEL";
    public static String SAVEPARAMETER_PARSALABEL_SAVE = "SAVEPARAMETER_PARSALABEL_SAVE";

    SaveParameter saveParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_doctor);

        if (savedInstanceState == null) {
            initializeData();
        } else {
            restoreData(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVEPARAMETER_PARSALABEL_SAVE, saveParameter);
    }

    @Override
    public void setYourInformationFragment(String day_selected, String time_selected) {
        Fragment fragment = new YourInformationFragment();
        Bundle bundle = new Bundle();

        setTime(day_selected, time_selected);

        bundle.putParcelable(YourInformationFragment.SAVEPARAMETER_PARSALABEL, saveParameter);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_record, fragment);
        fragmentTransaction.commit();
    }

    private void setTime(String day_selected, String time_selected) {
        SelectDoctor selectDoctor = saveParameter.getSelectDoctor();
        selectDoctor.setDay(day_selected);
        selectDoctor.setTime(time_selected);
        saveParameter.setSelectDoctor(selectDoctor);
    }

    private void setTimetableDoctorFragment() {
        Fragment fragment = new TimetableDoctorFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TimetableDoctorFragment.SELECT_DOCTOR, saveParameter.getSelectDoctor());
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_record, fragment);
        ft.commit();
    }

    private void initializeData() {
        saveParameter = getIntent().getParcelableExtra(SAVEPARAMETER_PARSALABEL);
        setTimetableDoctorFragment();
    }

    private void restoreData(Bundle savedInstanceState) {
        saveParameter = savedInstanceState.getParcelable(SAVEPARAMETER_PARSALABEL_SAVE);
    }

}
