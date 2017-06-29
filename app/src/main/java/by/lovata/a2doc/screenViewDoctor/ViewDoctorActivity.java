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

public class ViewDoctorActivity extends AppCompatActivity {

    public static final String ID_SPECIALITY_SELECTED = "ID_SPECIALITY_SELECTED";
    public static final String LIST_VIEW_FRAGMENT = "LIST_VIEW_FRAGMENT";
    public static final String MAP_VIEW_FRAGMENT = "MAP_VIEW_FRAGMENT";

    private static final String SAVEPARAMETER_PARSALABEL_SAVE = "SAVEPARAMETER_PARSALABEL_SAVE";

    SaveParameter saveParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor);

        if (savedInstanceState == null) {
            initializeData();

            setListDoctorsFragment();
        } else {
            restoreData(savedInstanceState);
        }

        setTitle(getString(R.string.list_doctors));
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
            case android.R.id.home:
                onBackPressed();
                return true;
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

        outState.putParcelable(SAVEPARAMETER_PARSALABEL_SAVE, saveParameter);
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
        Set<Integer> services_list = new TreeSet<>();
        APIMethods apiMethods = new APIMethods(this);

        for (DoctorInfo doctorInfo: doctorsInfo) {
            for (int id_service: doctorInfo.getService_list().keySet()) {
                services_list.add(id_service);
            }
        }
        services = apiMethods.getServicesFromJSON(id_city, services_list);

        return services;
    }

    private void restoreData(Bundle savedInstanceState) {
        saveParameter = savedInstanceState.getParcelable(SAVEPARAMETER_PARSALABEL_SAVE);
    }

    private void initializeData() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);

        int id_spiciality = getIntent().getIntExtra(ID_SPECIALITY_SELECTED, 0);
        int id_city = sharedPreferences.getInt(MainActivity.CITY_SELECT, 0);
        int id_sort = 0;
        boolean metro = false;
        boolean baby = false;

        APIMethods apiMethods = new APIMethods(this);
        DoctorInfo[] doctorsInfo = apiMethods.getDoctorsInfoFromJSON(id_city, id_spiciality);
        Map<Integer, OrganizationInfo> organizations = apiMethods.getOrganizationsInfoFromJSON(id_city, id_spiciality);
        Map<Integer, String> sevices = getServices(id_city, doctorsInfo);
        int id_filter = initId_filter(doctorsInfo);
        saveParameter = new SaveParameter(id_city, id_spiciality, id_filter, id_sort, doctorsInfo,
                                    organizations, sevices, metro, baby);
    }

    private int initId_filter(DoctorInfo[] doctorsInfo) {
        Set<Integer> set_keys_services = doctorsInfo[0].getService_list().keySet();
        Integer[] keys_services = set_keys_services.toArray(new Integer[set_keys_services.size()]);
        return keys_services[0];
    }

    private void setListDoctorsFragment() {
        Fragment fragment = new ListDoctorFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ListDoctorFragment.SAVEPARAMETER_PARSALABEL, saveParameter);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contain_view_doctor, fragment, LIST_VIEW_FRAGMENT);
        ft.commit();
    }

    private void setMapDoctorsFragment() {
        Fragment fragment = new MapDoctorFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(MapDoctorFragment.SAVEPARAMETER_PARSALABEL, saveParameter);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contain_view_doctor, fragment, MAP_VIEW_FRAGMENT);
        ft.commit();
    }
}
