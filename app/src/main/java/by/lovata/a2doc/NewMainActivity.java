package by.lovata.a2doc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.screenStart.MainActivity;
import by.lovata.a2doc.screenViewDoctor.ViewDoctorActivity;
import by.lovata.a2doc.search.SearchActivity;

public class NewMainActivity extends BaseMenuActivity {
    private Spinner citySpinner;
    private Spinner specialitySpinner;
    private APIMethods apiMethods=new APIMethods(this);;
    private Map<Integer,String> cities;
    private Map<Integer,String> specialities=new HashMap<>();
    private int idCity=0;
    private int idSpeciality;
    private ArrayList<String>citiesNames=new ArrayList<>();
    private ArrayList<String> specialitiesNames=new ArrayList<>();
    private String defaultMassegeCities="";
    private String defaultMassegeSpecialities="Выберите специализацию...";
    private Button findDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        initialToolBarMenu();
        citySpinner=(Spinner) findViewById(R.id.spinner_city);
        specialitySpinner=(Spinner) findViewById(R.id.spinner_speciality);
        initialiseDate();
    }

    private void initialiseDate() {
        findDoctor=(Button)findViewById(R.id.findDoctors);
        findDoctor.setEnabled(false);
        apiMethods=new APIMethods(this);
        cities=apiMethods.getCitiesFromJSON();
        initialiseListsForSpinners(citySpinner,citiesNames,cities,defaultMassegeCities);
        setCitySelectId(idCity);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idCity = SearchActivity.findIdMap(cities, citiesNames.get(position));
                    specialities=apiMethods.getSpecialitiesFromJSON(idCity);
                    initialiseListsForSpinners(specialitySpinner,specialitiesNames,
                            specialities,defaultMassegeSpecialities);
                setCitySelectId(idCity);
                   // idCity=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        initialiseListsForSpinners(specialitySpinner,specialitiesNames,specialities,defaultMassegeSpecialities);
       specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if (position!=0) {
                   idSpeciality = SearchActivity.findIdMap(specialities, specialitiesNames.get(position));
                   findDoctor.setEnabled(true);
               }
               else findDoctor.setEnabled(false);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
        findDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NewMainActivity.this, ViewDoctorActivity.class);
                i.putExtra(ViewDoctorActivity.ID_SPECIALITY_SELECTED, idSpeciality);
                startActivity(i);
            }
        });

    }
    private void initialiseListsForSpinners(Spinner spinner,ArrayList<String> array,Map<Integer,String>map,
                                            String defaultMassege){
        array.clear();
        if(!defaultMassege.equals(""))
        array.add(0,defaultMassege);
        if (map.size()!=0)
        array.addAll(map.values());
        spinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_activated_1,
                array));
        spinner.setSelection(0);
    }
    private boolean setCitySelectId(int idCity) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MainActivity.CITY_SELECT, idCity);
        return editor.commit();
    }
}
