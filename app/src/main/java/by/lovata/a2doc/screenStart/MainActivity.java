package by.lovata.a2doc.screenStart;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.screenAbout.TabAboutFragment;
import by.lovata.a2doc.screenStart.screenCity.TabCityFragment;
import by.lovata.a2doc.screenStart.screenQuestion.TabQuestionFragment;
import by.lovata.a2doc.screenStart.screenSearch.TabSearchFragment;

import static by.lovata.a2doc.R.id.tabs;

public class MainActivity extends AppCompatActivity {

    public static final String NAME_PREFERENCES = "TwoDocBY";
    public static final String CITY_SELECT = "CITY";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_search_24dp,
            R.drawable.ic_info_24dp,
            R.drawable.ic_question_answer_24dp,
            R.drawable.ic_location_city_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabSearchFragment(), "ONE");
        adapter.addFragment(new TabAboutFragment(), "TWO");
        adapter.addFragment(new TabQuestionFragment(), "THREE");
        adapter.addFragment(new TabCityFragment(), "FOUR");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

}
