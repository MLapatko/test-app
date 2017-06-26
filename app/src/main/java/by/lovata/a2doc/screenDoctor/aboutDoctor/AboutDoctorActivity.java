package by.lovata.a2doc.screenDoctor.aboutDoctor;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.screenAbout.TabAboutFragment;
import by.lovata.a2doc.screenStart.screenCity.TabCityFragment;
import by.lovata.a2doc.screenStart.screenQuestion.TabQuestionFragment;
import by.lovata.a2doc.screenStart.screenSearch.TabSearchFragment;

import static by.lovata.a2doc.R.id.tabs;

public class AboutDoctorActivity extends AppCompatActivity {

    public static final String ID_SELECTED_DOCTOR = "ID_SELECTED_DOCTOR";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    int id_doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_doctor);

        id_doctor = getIntent().getIntExtra(ID_SELECTED_DOCTOR, 0);

        viewPager = (ViewPager) findViewById(R.id.viewpager_doctor_about);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_doctor_about);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
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

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapterAboutDoctor adapter = new PagerAdapterAboutDoctor(getSupportFragmentManager());

        Fragment fragment_review = new ReviewFragment();
        Fragment fragment_qualification = new QualificationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID_SELECTED_DOCTOR, id_doctor);

        fragment_review.setArguments(bundle);
        fragment_qualification.setArguments(bundle);

        adapter.addFragment(fragment_review, "ONE");
        adapter.addFragment(fragment_qualification, "TWO");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText(R.string.about_doctor_review);
        tabLayout.getTabAt(1).setText(R.string.about_doctor_qualification);
    }
}
