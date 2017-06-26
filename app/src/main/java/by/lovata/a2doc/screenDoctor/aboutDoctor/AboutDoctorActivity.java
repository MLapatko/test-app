package by.lovata.a2doc.screenDoctor.aboutDoctor;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import by.lovata.a2doc.R;

public class AboutDoctorActivity extends AppCompatActivity {

    public static final String ID_SELECTED_DOCTOR = "ID_SELECTED_DOCTOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_doctor);

        int id_doctor = getIntent().getIntExtra(ID_SELECTED_DOCTOR, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
