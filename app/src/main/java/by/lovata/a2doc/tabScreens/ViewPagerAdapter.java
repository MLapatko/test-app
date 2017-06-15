package by.lovata.a2doc.tabScreens;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import by.lovata.a2doc.aboutScreen.TabAboutFragment;
import by.lovata.a2doc.screenCities.TabCitiesFragment;
import by.lovata.a2doc.screenQuestion.TabQuestionFragment;
import by.lovata.a2doc.screenSearch.TabSearchFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        if(position == 0) {
            fragment = new TabSearchFragment();
        } else if(position == 1) {
            fragment = new TabAboutFragment();
        } else if(position == 2) {
            fragment = new TabQuestionFragment();
        } else if(position == 3) {
            fragment = new TabCitiesFragment();
        }

            return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}