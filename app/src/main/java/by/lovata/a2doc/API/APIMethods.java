package by.lovata.a2doc.API;


import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.OrganizationInfo;

public class APIMethods {

    private Context context;

    public APIMethods(Context context) {
        this.context = context;
    }

    public Map<Integer, String> getCitiesFromJSON() {
        Map<Integer, String> cities_map = new TreeMap<>();
        JSONObject dataJsonObj = null;
        String cities_JSON = loadCitiesFromAPI(R.raw.cities);

        try {
            dataJsonObj = new JSONObject(cities_JSON);
            JSONArray items = dataJsonObj.getJSONArray("cities");

            String city_name;
            int city_id;
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                city_name = item.getString("name");
                city_id = item.getInt("id");
                cities_map.put(city_id, city_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cities_map;
    }
    private String loadCitiesFromAPI(int choice_id){

        Resources r = context.getResources();
        InputStream is = r.openRawResource(choice_id);
        String cities_JSON = null;
        try {
            cities_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  cities_JSON;
    }

    public String getPhoneFromJSON() {
        String phone = null;
        String phone_JSON = loadPhoneFromAPI(R.raw.phone);
        JSONObject dataJsonObj;

        try {
            dataJsonObj = new JSONObject(phone_JSON);
            phone = dataJsonObj.getString("phone");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return phone;
    }
    private String loadPhoneFromAPI(int choice_id){

        Resources r = context.getResources();
        InputStream is = r.openRawResource(choice_id);
        String phone_JSON = null;
        try {
            phone_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  phone_JSON;
    }

    public Map<Integer, String> getSpecialitiesFromJSON(int id_city) {
        Map<Integer, String> specialities_map = new TreeMap<>();
        JSONObject dataJsonObj = null;
        String specialities_JSON = loadSpecialitiesFromAPI(id_city);

        try {
            dataJsonObj = new JSONObject(specialities_JSON);
            JSONArray items = dataJsonObj.getJSONArray("specialities");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                String speciality_name = item.getString("name");
                int speciality_id = item.getInt("id");
                specialities_map.put(speciality_id, speciality_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return specialities_map;
    }
    private String loadSpecialitiesFromAPI(int id_city){

        Resources r = context.getResources();
        InputStream is = null;
        switch (id_city) {
            case 1:
                is = r.openRawResource(R.raw.specialities_cityid_1);
                break;
            case 2:
                is = r.openRawResource(R.raw.specialities_cityid_2);
                break;
            case 3:
                is = r.openRawResource(R.raw.specialities_cityid_3);
                break;
        }

        String specialities_JSON = null;
        try {
            specialities_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  specialities_JSON;
    }

    public DoctorInfo[] getDoctorsInfoFromJSON(int id_city, int id_speciality) {
        DoctorInfo[] item_set = null;
        JSONObject dataJsonObj = null;
        String doctorsInfo_JSON = loadDoctorsInfoFromAPI(id_city, id_speciality);

        try {
            dataJsonObj = new JSONObject(doctorsInfo_JSON);
            JSONArray items = dataJsonObj.getJSONArray("doctors");
            item_set = new DoctorInfo[items.length()];
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                int id = item.getInt("id");

                JSONArray id_organizationArray = item.getJSONArray("id_organization");
                int[] id_organization = new int[id_organizationArray.length()];
                for (int j = 0; j < id_organizationArray.length(); j++) {
                    id_organization[j] = id_organizationArray.getInt(j);
                }

                String img = item.getString("img");
                String fullname = item.getString("fullname");
                String speciality = item.getString("speciality");

                JSONArray service_listArray = item.getJSONArray("service_list");
                Map<Integer, Integer> service_list = new TreeMap<>();
                for (int j = 0; j < service_listArray.length(); j++) {
                    JSONObject item_service_list = service_listArray.getJSONObject(j);
                    int key_service = item_service_list.getInt("id");
                    int price_service = item_service_list.getInt("price");
                    service_list.put(key_service, price_service);
                }

                int count_reviews = item.getInt("count_reviews");
                int experience = item.getInt("experience");
                boolean merto = item.getBoolean("merto");
                boolean baby = item.getBoolean("baby");
                item_set[i] = new DoctorInfo(id, id_organization, img, fullname, speciality,
                        service_list, count_reviews, experience, merto, baby);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return item_set;
    }
    private String loadDoctorsInfoFromAPI(int id_city, int id_speciality){

        Resources r = context.getResources();
        InputStream is = null;
        switch (id_city) {
            case 1:
                is = r.openRawResource(R.raw.doctors_cityid_1_specialityid);
                break;
            case 2:
                is = r.openRawResource(R.raw.doctors_cityid_2_specialityid);
                break;
            case 3:
                is = r.openRawResource(R.raw.doctors_cityid_3_specialityid);
                break;
        }

        String doctorsInfo_JSON = null;
        try {
            doctorsInfo_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  doctorsInfo_JSON;
    }

    public Map<Integer, OrganizationInfo> getOrganizationsInfoFromJSON(int id_city, int id_speciality) {
        Map<Integer, OrganizationInfo> organizationInfo_map = new TreeMap<>();
        JSONObject dataJsonObj = null;
        String organizationInfo_JSON = loadOrganizationsInfoFromAPI(id_city, id_speciality);

        try {
            dataJsonObj = new JSONObject(organizationInfo_JSON);
            JSONArray items = dataJsonObj.getJSONArray("organizations");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                int id = item.getInt("id");
                String name = item.getString("name");
                int lat = item.getInt("lat");
                int lng = item.getInt("lng");
                organizationInfo_map.put(id, new OrganizationInfo(id, name, lat, lng));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return organizationInfo_map;
    }
    private String loadOrganizationsInfoFromAPI(int id_city, int id_speciality){

        Resources r = context.getResources();
        InputStream is = null;
        switch (id_city) {
            case 1:
                is = r.openRawResource(R.raw.organizations_cityid_1_specialityid);
                break;
            case 2:
                is = r.openRawResource(R.raw.organizations_cityid_2_specialityid);
                break;
            case 3:
                is = r.openRawResource(R.raw.organizations_cityid_3_specialityid);
                break;
        }

        String doctorsInfo_JSON = null;
        try {
            doctorsInfo_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  doctorsInfo_JSON;
    }

    public Map<Integer, String> getServicesFromJSON(int id_city, Set<Integer> services_list) {
        Map<Integer, String> services_map = new TreeMap<>();
        JSONObject dataJsonObj = null;
        String services_JSON = loadServicesFromAPI(id_city, services_list);

        try {
            dataJsonObj = new JSONObject(services_JSON);
            JSONArray items = dataJsonObj.getJSONArray("services");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                String service_name = item.getString("name");
                int service_id = item.getInt("id");
                services_map.put(service_id, service_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return services_map;
    }
    private String loadServicesFromAPI(int id_city, Set<Integer> services_list){

        Resources r = context.getResources();
        InputStream is = null;
        switch (id_city) {
            case 1:
                is = r.openRawResource(R.raw.service_cityid_1);
                break;
            case 2:
                is = r.openRawResource(R.raw.service_cityid_2);
                break;
            case 3:
                is = r.openRawResource(R.raw.service_cityid_3);
                break;
        }

        String specialities_JSON = null;
        try {
            specialities_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  specialities_JSON;
    }

    private String convertStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = is.read();
        while( i != -1)
        {
            baos.write(i);
            i = is.read();
        }
        return  baos.toString();
    }
}
