package by.lovata.a2doc.screenDoctor.aboutDoctor;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import by.lovata.a2doc.BaseMenuActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenDoctor.aboutDoctor.screenQualification.QualificationFragment;
import by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews.ReviewFragment;
import by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews.Reviews;

public class AboutDoctorActivity extends BaseMenuActivity {

    public static final String ID_SELECTED_DOCTOR = "ID_SELECTED_DOCTOR";

    private TabLayout tabLayout;
    ViewPager viewPager;

    private int id_doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_doctor);

        initializeData();
        initializeView();

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

    private void initializeData() {
        id_doctor = getIntent().getIntExtra(ID_SELECTED_DOCTOR, 0);
    }

    private void initializeView() {
        setTitle(getString(R.string.review));

        viewPager = (ViewPager) findViewById(R.id.viewpager_doctor_about);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_doctor_about);
        tabLayout.setupWithViewPager(viewPager);
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
