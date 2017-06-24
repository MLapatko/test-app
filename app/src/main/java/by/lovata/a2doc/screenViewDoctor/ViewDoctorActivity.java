package by.lovata.a2doc.screenViewDoctor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.LogoActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.MainActivity;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.ListDoctorFragment;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.MenuFilterFragment;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.MenuSortFragment;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts.SortDefault;
import by.lovata.a2doc.screenViewDoctor.screenMapDoctor.MapDoctorFragment;

public class ViewDoctorActivity extends AppCompatActivity implements
                    ListDoctorFragment.InformationInterfaceList,
                    MapDoctorFragment.InformationInterfaceMap {

    public static final String ID_SPECIALITY_SELECTED_SAVE = "ID_SPECIALITY_SELECTED_SAVE";
    public static final String ID_CITY_SELECTED_SAVE = "ID_CITY_SELECTED_SAVE";
    public static final String ID_SORT_SELECTED_SAVE = "ID_SORT_SELECTED_SAVE";
    public static final String ID_FILTER_SELECTED_SAVE = "ID_FILTER_SELECTED_SAVE";
    public static final String IS_METRO_SAVE = "IS_METRO_SAVE";
    public static final String IS_BABY_SAVE = "IS_BABY_SAVE";

    public static final String ID_SPECIALITY_SELECTED = "ID_SPECIALITY_SELECTED";

    public static final String LIST_VIEW_FRAGMENT = "LIST_VIEW_FRAGMENT";
    public static final String MAP_VIEW_FRAGMENT = "MAP_VIEW_FRAGMENT";

    int id_spiciality;
    int id_city;

    int id_sort;

    int id_filter;
    boolean metro;
    boolean baby;

    DoctorInfo[] doctorsInfo;
    Map<Integer, String> sevices;
    Map<Integer, OrganizationInfo> organizations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor);

        if (savedInstanceState == null) {
            initializeData();

            if (doctorsInfo.length > 1) {
                Arrays.sort(doctorsInfo, new SortDefault());
            }
            setListDoctorsFragment();
        } else {
            restoreData(savedInstanceState);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return new SaveParameter(doctorsInfo, sevices, organizations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_change, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_change:
                clickViewChange(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ID_SPECIALITY_SELECTED_SAVE, id_spiciality);
        outState.putInt(ID_CITY_SELECTED_SAVE, id_city);
        outState.putInt(ID_SORT_SELECTED_SAVE, id_sort);
        outState.putInt(ID_FILTER_SELECTED_SAVE, id_filter);

        outState.putBoolean(ListDoctorFragment.IS_METRO, metro);
        outState.putBoolean(ListDoctorFragment.IS_BABY, baby);
    }

    private void clickViewChange(MenuItem menuItem) {
        FragmentManager fm = getSupportFragmentManager();

        if (fm.findFragmentByTag(MAP_VIEW_FRAGMENT) != null) {
            setListDoctorsFragment();
        } else if (fm.findFragmentByTag(LIST_VIEW_FRAGMENT) != null) {
            setMapDoctorsFragment();
        }
    }

    private Map<Integer, String> getServices(int id_city, DoctorInfo[] doctorsInfo) {
        Map<Integer, String> services = null;
        Set<Integer> services_list = new TreeSet<Integer>();
        APIMethods apiMethods = new APIMethods(this);

        for (DoctorInfo doctorInfo: doctorsInfo) {
            for (int id_service: doctorInfo.service_list.keySet()) {
                services_list.add(id_service);
            }
        }
        services = apiMethods.getServicesFromJSON(id_city, services_list);

        return services;
    }

    @Override
    public DoctorInfo[] getDoctors() {
        return doctorsInfo;
    }

    @Override
    public Map<Integer, String> getSevices() {
        return sevices;
    }

    @Override
    public Map<Integer, OrganizationInfo> getOrganization() {
        return organizations;
    }

    @Override
    public void setId_sort(int id_sort_selected) {
        id_sort = id_sort_selected;
    }

    @Override
    public void setFilters(int id_filter, boolean metro, boolean baby) {
        this.id_filter = id_filter;
        this.metro = metro;
        this.baby = baby;
    }

    private void restoreData(Bundle savedInstanceState) {
        id_spiciality = savedInstanceState.getInt(ID_SPECIALITY_SELECTED_SAVE, 0);
        id_city = savedInstanceState.getInt(ID_CITY_SELECTED_SAVE, 0);

        id_sort = savedInstanceState.getInt(ID_SORT_SELECTED_SAVE, 0);

        id_filter = savedInstanceState.getInt(ID_FILTER_SELECTED_SAVE, 0);
        metro = savedInstanceState.getBoolean(IS_METRO_SAVE);
        baby = savedInstanceState.getBoolean(IS_BABY_SAVE);

        SaveParameter saveParameter = (SaveParameter) getLastCustomNonConfigurationInstance();
        doctorsInfo = saveParameter.doctorsInfo;
        sevices = saveParameter.sevices;
        organizations = saveParameter.organizations;
    }

    private void initializeData() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);

        id_spiciality = getIntent().getIntExtra(ID_SPECIALITY_SELECTED, 0);
        id_city = sharedPreferences.getInt(MainActivity.CITY_SELECT, 0);
        id_sort = 0;
        metro = false;
        baby = false;

        APIMethods apiMethods = new APIMethods(this);
        doctorsInfo = apiMethods.getDoctorsInfoFromJSON(id_city, id_spiciality);
        organizations = apiMethods.getOrganizationsInfoFromJSON(id_city, id_spiciality);
        sevices = getServices(id_city, doctorsInfo);
        id_filter = initId_filter();
    }

    private int initId_filter() {
        Set<Integer> set_keys_services = doctorsInfo[0].service_list.keySet();
        Integer[] keys_services = set_keys_services.toArray(new Integer[set_keys_services.size()]);
        return keys_services[0];
    }

    private void setListDoctorsFragment() {
        Fragment fragment = new ListDoctorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ListDoctorFragment.ID_SORT_SELECTED, id_sort);
        bundle.putInt(ListDoctorFragment.ID_FILTER_SELECTED, id_filter);
        bundle.putBoolean(ListDoctorFragment.IS_METRO, metro);
        bundle.putBoolean(ListDoctorFragment.IS_BABY, baby);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contain_view_doctor, fragment, LIST_VIEW_FRAGMENT);
        ft.commit();
    }

    private void setMapDoctorsFragment() {
        Fragment fragment = new MapDoctorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MapDoctorFragment.ID_FILTER_SELECTED, id_filter);
        bundle.putBoolean(MapDoctorFragment.IS_METRO, metro);
        bundle.putBoolean(MapDoctorFragment.IS_BABY, baby);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contain_view_doctor, fragment, MAP_VIEW_FRAGMENT);
        ft.commit();
    }
}
