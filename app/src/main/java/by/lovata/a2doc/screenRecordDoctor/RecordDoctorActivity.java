package by.lovata.a2doc.screenRecordDoctor;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor.TimetableDoctorFragment;

public class RecordDoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_doctor);

        Fragment fragment = new TimetableDoctorFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_record, fragment);
        fragmentTransaction.commit();
    }
}
