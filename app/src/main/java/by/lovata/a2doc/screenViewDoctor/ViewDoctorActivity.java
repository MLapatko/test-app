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
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.MainActivity;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.ListDoctorFragment;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.MenuFilterFragment;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.MenuSortFragment;
import by.lovata.a2doc.screenViewDoctor.screenMapDoctor.MapDoctorFragment;

public class ViewDoctorActivity extends AppCompatActivity implements InformationInterface {

    public static final String ID_SPECIALITY_SELECTED = "ID_SPECIALITY_SELECTED";
    public static final String ID_CITY_SELECTED = "ID_CITY_SELECTED";
    public static final String PARCEL_DOCTORSINFO = "PARCEL_DOCTORSINFO";

    public static final String LIST_VIEW_FRAGMENT = "LIST_VIEW_FRAGMENT";
    public static final String MAP_VIEW_FRAGMENT = "MAP_VIEW_FRAGMENT";

    int id_spiciality;
    int id_city;

    DoctorInfo[] doctorsInfo;
    Map<Integer, String> sevices;
    Map<Integer, OrganizationInfo> organizations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor);

        if (savedInstanceState == null) {
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);

            id_spiciality = getIntent().getIntExtra(ID_SPECIALITY_SELECTED, 0);
            id_city = sharedPreferences.getInt(MainActivity.CITY_SELECT, 0);;

            APIMethods apiMethods = new APIMethods(this);
            doctorsInfo = apiMethods.getDoctorsInfoFromJSON(id_city, id_spiciality);
            organizations = apiMethods.getOrganizationsInfoFromJSON(id_city, id_spiciality);
            sevices = getServices(id_city, doctorsInfo);

            Fragment fragment = new ListDoctorFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ID_SPECIALITY_SELECTED, id_spiciality);
            fragment.setArguments(bundle);
            FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
            fragmentManager.replace(R.id.contain_view_doctor, fragment, LIST_VIEW_FRAGMENT);
            fragmentManager.commit();
        } else {
            id_spiciality = savedInstanceState.getInt(ID_SPECIALITY_SELECTED, 0);
            id_city = savedInstanceState.getInt(ID_CITY_SELECTED, 0);

            SaveParameter saveParameter = (SaveParameter) getLastCustomNonConfigurationInstance();
            doctorsInfo = saveParameter.doctorsInfo;
            sevices = saveParameter.sevices;
            organizations = saveParameter.organizations;
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

        outState.putInt(ID_SPECIALITY_SELECTED, id_spiciality);
        outState.putInt(ID_CITY_SELECTED, id_city);
        outState.putParcelableArray(PARCEL_DOCTORSINFO, doctorsInfo);
    }

    private void clickViewChange(MenuItem menuItem) {
        Fragment fragment = null;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (fm.findFragmentByTag(MAP_VIEW_FRAGMENT) != null) {
            fragment = new ListDoctorFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ID_SPECIALITY_SELECTED, id_spiciality);
            fragment.setArguments(bundle);
            ft.replace(R.id.contain_view_doctor, fragment, LIST_VIEW_FRAGMENT);
        } else if (fm.findFragmentByTag(LIST_VIEW_FRAGMENT) != null) {
            fragment = new MapDoctorFragment();
            ft.replace(R.id.contain_view_doctor, fragment, MAP_VIEW_FRAGMENT);
        }

        ft.commit();
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
    public Map<Integer, OrganizationInfo> getOrganizations() {
        return organizations;
    }
}
