package by.lovata.a2doc.search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.LogoActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenStart.MainActivity;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;
import by.lovata.a2doc.screenViewDoctor.SelectDoctor;
import by.lovata.a2doc.screenViewDoctor.ViewDoctorActivity;

/**
 * Created by user on 07.07.2017.
 */

public class SearchActivity extends AppCompatActivity {
    private EditText editText;
    private ListView specList;
    private ListView doctorsNamesList;
    private ArrayList<String> specialities=new ArrayList<>(LogoActivity.getSpecialities().values());
    private ArrayList<Integer> key_specialities=new ArrayList<>(LogoActivity.getSpecialities().keySet());
    private ArrayList<String> searchResultSpecialitiesValues;
    private ArrayList<Integer> searchResultSpecialitiesKeys;
    private APIMethods apiMethods = new APIMethods(this);
    private  SharedPreferences sharedPreferences;
    private int idCity;
    private ArrayList<DoctorInfo> doctorsInfo;
    private ArrayList<DoctorInfo> searchResultNames;
    private SaveParameter saveParameter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);
        idCity = sharedPreferences.getInt(MainActivity.CITY_SELECT, 0);

        editText=(EditText)findViewById(R.id.text_search);

        specList=(ListView)findViewById(R.id.search_result_list);

        doctorsNamesList=(ListView)findViewById(R.id.search_result_names_list);

        doctorsInfo =new ArrayList<>(Arrays.asList(apiMethods.getDoctorsInfoFromJSON(idCity,0)));

        initializeLists();

      editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    initializeLists();
                }
                else {

                        makeSearch(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       specList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i =new Intent(SearchActivity.this,ViewDoctorActivity.class);
                i.putExtra(ViewDoctorActivity.ID_SPECIALITY_SELECTED,searchResultSpecialitiesKeys.get(position));
                startActivity(i);
            }
        });
        doctorsNamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SearchActivity.this, DoctorActivity.class);
                int ind=findId(doctorsInfo,searchResultNames.get(position).getFull_name());
                DoctorInfo selectedDoctor = doctorsInfo.get(ind);
                DoctorInfo[] doctorsInfo = apiMethods.getDoctorsInfoFromJSON(idCity,-1);

                int id_filter = ViewDoctorActivity.initId_filter(doctorsInfo);
                saveParameter = new SaveParameter(idCity, 15, id_filter, 0, doctorsInfo,
                        apiMethods.getOrganizationsInfoFromJSON(idCity, 15),
                        ViewDoctorActivity.getServices(idCity, doctorsInfo,apiMethods), false, false);
                saveParameter.setSelectDoctor(new SelectDoctor(0, id_filter,
                        selectedDoctor.getId_organization()[0], null, null, selectedDoctor));
                intent.putExtra(DoctorActivity.SAVEPARAMETER_PARSALABEL, saveParameter);

                startActivity(intent);
            }
        });
    }

    private int findId(ArrayList<DoctorInfo> arr,String fullName) {
        int id = 0;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getFull_name().equals(fullName)) {
                id = i;
                break;
            }
        }
        return id;
    }
   public void makeSearch(String textToSearch) {
        searchResultSpecialitiesValues=new ArrayList<>();
        searchResultSpecialitiesKeys =new ArrayList<>();
        searchResultNames=new ArrayList<>();
        for (int i = 0; i <specialities.size(); i++) {
            if(specialities.get(i).toLowerCase().contains(textToSearch.toLowerCase())){
                searchResultSpecialitiesValues.add(specialities.get(i));
                searchResultSpecialitiesKeys.add(key_specialities.get(i));
            }

        }
       for (int i = 0; i <doctorsInfo.size() ; i++) {
           if(doctorsInfo.get(i).getFull_name().toLowerCase().contains(textToSearch.toLowerCase())){
               searchResultNames.add(doctorsInfo.get(i));
           }
       }
       specList.setAdapter(new ArrayAdapter<>(this,R.layout.speciality_item,
               searchResultSpecialitiesValues));
       Utility.setDynamicHeight(specList);
       doctorsNamesList.setAdapter(new ArrayAdapter<>(this,R.layout.speciality_item,
               searchResultNames));
       Utility.setDynamicHeight(doctorsNamesList);

    }
    private void initializeLists() {

        searchResultSpecialitiesValues=new ArrayList<>();
        searchResultSpecialitiesValues.addAll(specialities);
        searchResultSpecialitiesKeys=new ArrayList<>();
        searchResultSpecialitiesKeys.addAll(key_specialities);
        specList.setAdapter(new ArrayAdapter<>(this,R.layout.speciality_item,
                searchResultSpecialitiesValues));
        Utility.setDynamicHeight(specList);
        searchResultNames=new ArrayList<>();
        searchResultNames.addAll(doctorsInfo);
        doctorsNamesList.setAdapter(new ArrayAdapter<>(this,R.layout.speciality_item,
                searchResultNames));
        Utility.setDynamicHeight(doctorsNamesList);
    }
}
