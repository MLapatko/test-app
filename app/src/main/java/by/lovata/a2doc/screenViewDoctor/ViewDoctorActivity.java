package by.lovata.a2doc.screenViewDoctor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.BaseMenuActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.MainActivity;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.ListDoctorFragment;
import by.lovata.a2doc.screenViewDoctor.screenMapDoctor.MapDoctorFragment;

public class ViewDoctorActivity extends BaseMenuActivity {

    public static final String ID_SPECIALITY_SELECTED = "ID_SPECIALITY_SELECTED";
    public static final String LIST_VIEW_FRAGMENT = "LIST_VIEW_FRAGMENT";
    public static final String MAP_VIEW_FRAGMENT = "MAP_VIEW_FRAGMENT";
    public static final String ID_FILTER="ID_FILTER";
    public static final String NAME_SPECIALITY="NAME_SPECIALITY";

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


        setTitle(getIntent().getStringExtra("NAME_SPECIALITY"));
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



    private void restoreData(Bundle savedInstanceState) {
        saveParameter = savedInstanceState.getParcelable(SAVEPARAMETER_PARSALABEL_SAVE);
    }

    private void initializeData() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);

        int id_spiciality = getIntent().getIntExtra(ID_SPECIALITY_SELECTED, 0);
        int id_filter = getIntent().getIntExtra(ID_FILTER, -1);
        int id_city = sharedPreferences.getInt(MainActivity.CITY_SELECT, 0);
        int id_sort = 0;
        boolean metro = false;
        boolean baby = false;

        APIMethods apiMethods = new APIMethods(this);
        ArrayList<DoctorInfo> doctorsInfo = apiMethods.getDoctorsInfoFromJSON(id_city, id_spiciality);
        Map<Integer, OrganizationInfo> organizations = apiMethods.getOrganizationsInfoFromJSON(id_city, id_spiciality);
        Map<Integer, String> sevices = apiMethods.getServicesFromJSON(id_city, id_spiciality);
        if (id_filter==-1)
        id_filter =  initIdFilter(doctorsInfo,0);
        saveParameter = new SaveParameter(id_city, id_filter, id_sort,id_spiciality, doctorsInfo,
                organizations, sevices, metro, baby);
    }

    public static int initIdFilter(ArrayList<DoctorInfo> doctorsInfo,int position) {
        //получаем ключи услуг, выбранного доктора
        Set<Integer> setKeysServices = doctorsInfo.get(position).getService_list().keySet();
        //возвращаем 1-ый элемент setKeysServices
        return setKeysServices.iterator().next();
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
        bundle.putInt(MapDoctorFragment.POSITION,-1);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contain_view_doctor, fragment, MAP_VIEW_FRAGMENT);
        ft.commit();
    }
}
