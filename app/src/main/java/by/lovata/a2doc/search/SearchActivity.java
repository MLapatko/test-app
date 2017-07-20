package by.lovata.a2doc.search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

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
    private ListView servicesList;
    //данные об услкугах и о специальностях, которые получаются по API
    private Map<Integer,String>services;
    private Map<Integer,String>specialities=LogoActivity.getSpecialities();
    //списки названий специальностей и услуг
    private ArrayList<String> specialitiesName=new ArrayList<>(specialities.values());
    private ArrayList<String> servicesValues;

    private ArrayList<DoctorInfo> doctorsInfo;

    //списки для результатов поиска
    private ArrayList<String> searchResultSpecialities;
    private ArrayList<String> searchResultServices;
    private ArrayList<DoctorInfo> searchResultNames;

    private APIMethods apiMethods = new APIMethods(this);
    private  SharedPreferences sharedPreferences;
    private int idCity;
    private SaveParameter saveParameter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Поиск");
        setContentView(R.layout.activity_search);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
        sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);
        idCity = sharedPreferences.getInt(MainActivity.CITY_SELECT, 0);

        editText=(EditText)findViewById(R.id.text_search);
        //ListView для отображения списка специальностей
        specList=(ListView)findViewById(R.id.search_result_list);
        //ListView для отображения списка докторов
        doctorsNamesList=(ListView)findViewById(R.id.search_result_names_list);
        //ListView для отображения списка услуг
        servicesList=(ListView)findViewById(R.id.search_result_services_list);

        doctorsInfo =apiMethods.getDoctorsInfoFromJSON(idCity,-1);
        services=apiMethods.getServicesFromJSON(idCity,-1);
        servicesValues=new ArrayList<>(services.values());
        //услугу "первичный прием" отображать в списке услуг не надо,
        // поэтому удаляем все услуги с таким именем
        for (int i = 0; i <servicesValues.size() ; i++) {
            if (servicesValues.get(i).toLowerCase().equals("первичный прием")) {
                servicesValues.remove(i);
            }
        }
        searchResultSpecialities=new ArrayList<>();
        searchResultServices=new ArrayList<>();
        searchResultNames=new ArrayList<>();
        //initializeLists(searchResultSpecialities,specList,specialitiesName);
        //initializeLists(searchResultNames,doctorsNamesList,doctorsInfo);
        //initializeLists(searchResultServices,servicesList,servicesValues);


      editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //если пользователь стер написанный ранее текст, очищаем списки
                if (s.toString().equals("")) {
                    clearArrayLists();
                    //initializeLists(searchResultSpecialities,specList,specialitiesName);
                    //initializeLists(searchResultNames,doctorsNamesList,doctorsInfo);
                    //initializeLists(searchResultServices,servicesList,servicesValues);
                    //установка адаптеров и высоты для каждого ListView
                    setAdapterForListView(searchResultSpecialities,specList);
                    setAdapterForListView(searchResultNames,doctorsNamesList);
                    setAdapterForListView(searchResultServices,servicesList);
                }
                else {
                    clearArrayLists();
                    makeSearch(s.toString(),specialitiesName,specList,searchResultSpecialities);
                    makeSearchDoctorInfo(s.toString(),doctorsInfo,doctorsNamesList,
                            searchResultNames);
                    makeSearch(s.toString(),servicesValues,servicesList,searchResultServices);

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
                //ищем id специальности в specialities
                int ind=findIdMap(specialities,searchResultSpecialities.get(position));
                //передаем id специальности на ViewDoctorActivity
                i.putExtra(ViewDoctorActivity.ID_SPECIALITY_SELECTED,ind);
                startActivity(i);
            }
        });
        doctorsNamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SearchActivity.this, DoctorActivity.class);
                int ind=findId(doctorsInfo,searchResultNames.get(position).getFull_name());
                DoctorInfo selectedDoctor = doctorsInfo.get(ind);
                ArrayList<DoctorInfo>doctorsInfo = apiMethods.getDoctorsInfoFromJSON(idCity,-1);
                int idFilter = ViewDoctorActivity.initIdFilter(doctorsInfo, ind);
                saveParameter = new SaveParameter(idCity, 15, idFilter, 0, doctorsInfo,
                        apiMethods.getOrganizationsInfoFromJSON(idCity, 15),
                        ViewDoctorActivity.getServices(idCity, doctorsInfo,apiMethods,-1), false, false);
                saveParameter.setSelectDoctor(new SelectDoctor(searchResultNames.get(position).getId(), idFilter,
                        selectedDoctor.getId_organization()[0], null, null, selectedDoctor));

                intent.putExtra(DoctorActivity.SAVEPARAMETER_PARSALABEL, saveParameter);
                startActivity(intent);
            }
        });
        servicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i =new Intent(SearchActivity.this,ViewDoctorActivity.class);
                //ищем id услуги в services
                int ind=findIdMap(services,searchResultServices.get(position));
                //apiMethods.findIdSpeciality(ind,idCity) находит id специальности по id услуги
                i.putExtra(ViewDoctorActivity.ID_SPECIALITY_SELECTED,apiMethods.findIdSpeciality(ind,idCity));
                i.putExtra(ViewDoctorActivity.ID_FILTER,ind);
                startActivity(i);
            }
        });
    }
    private int findIdMap(Map<Integer,String>map,String str){
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(str))
               return entry.getKey();
        }
        return -1;
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
    // функция поиска, если ArrayLst<DoctorInfo>
 public void makeSearchDoctorInfo(String textToSearch,ArrayList<DoctorInfo>arr,ListView list,
                                  ArrayList<DoctorInfo>searchResult){
     for (int i = 0; i <arr.size() ; i++) {
         if(arr.get(i).getFull_name().toLowerCase().contains(textToSearch.toLowerCase())){
             searchResult.add(arr.get(i));
         }
     }
     setAdapterForListView(searchResult,list);
 }
    // функция поиска, если ArrayLst<String>
   public  void makeSearch(String textToSearch,ArrayList<String>arr,ListView list,ArrayList<String>searchResult) {
       for (int i = 0; i <arr.size() ; i++) {
           //если элемент исходного списока содержит подстроку (без учета регистра),
           // которую ввел пользователь,то добавляем этот элемент в результат
           if(arr.get(i).toLowerCase().contains(textToSearch.toLowerCase())){
               searchResult.add(arr.get(i));
           }
       }
       //устанавливаем адаптер для результата поиска
       setAdapterForListView(searchResult,list);
    }
    private <T> void initializeLists(ArrayList<T> searchResult, ListView list,ArrayList<T> arr) {
        searchResult.addAll(arr);
        setAdapterForListView(searchResult,list);
    }
    //установка адаптера для ListView
    private<T> void setAdapterForListView(ArrayList<T> arr,ListView listView){
        listView.setAdapter(new ArrayAdapter<>(this,R.layout.speciality_item,arr));
        //установка высоты ListView
        Utility.setDynamicHeight(listView);
    }
    private void clearArrayLists(){
        searchResultServices.clear();
        searchResultNames.clear();
        searchResultSpecialities.clear();
    }
    //возвращение на предыдущее activity при нажатии пользователем на крестик в углу экрана
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
