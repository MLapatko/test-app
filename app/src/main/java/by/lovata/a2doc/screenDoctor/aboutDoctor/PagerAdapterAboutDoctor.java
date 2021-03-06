package by.lovata.a2doc.screenDoctor.aboutDoctor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapterAboutDoctor extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public PagerAdapterAboutDoctor(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}