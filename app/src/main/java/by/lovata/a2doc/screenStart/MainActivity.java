package by.lovata.a2doc.screenStart;



import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import by.lovata.a2doc.BaseMenuActivity;
import by.lovata.a2doc.InternetReceiver;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.screenAbout.TabAboutFragment;
import by.lovata.a2doc.screenStart.screenCity.TabCityFragment;
import by.lovata.a2doc.screenStart.screenQuestion.TabQuestionFragment;
import by.lovata.a2doc.screenStart.screenSearch.TabSearchFragment;

import static by.lovata.a2doc.R.id.tabs;

public class MainActivity extends BaseMenuActivity {

    public static final String NAME_PREFERENCES = "TwoDocBY";
    public static final String CITY_SELECT = "CITY";
    public static final String PHONE = "PHONE";


    private InternetReceiver receiver;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String phone;
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

        initializeView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        receiver = new InternetReceiver();
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(receiver, new IntentFilter(filter));
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
    }

    private void initializeView() {
        phone = getIntent().getStringExtra(PHONE);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment fragment = null;

        fragment = new TabSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PHONE, phone);
        fragment.setArguments(bundle);

        adapter.addFragment(fragment, "ONE");
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
