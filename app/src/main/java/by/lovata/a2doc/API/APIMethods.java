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

import by.lovata.a2doc.screenViewDoctor.DoctorInfo;

public class APIMethods {

    Context context;

    public APIMethods(Context context) {
        this.context = context;
    }

    public Map<Integer, String> parseCitiesFromJSON(String s_JSON, String type) {
        Map<Integer, String> item_map = new TreeMap<>();
        JSONObject dataJsonObj = null;

        try {
            dataJsonObj = new JSONObject(s_JSON);
            JSONArray items = dataJsonObj.getJSONArray(type);

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                String city_name = item.getString("name");
                int city_id = item.getInt("id");
                item_map.put(city_id, city_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return item_map;
    }

    public String parsePhoneFromJSON(String s_JSON, String type) {
        String phone = null;
        JSONObject dataJsonObj;

        try {
            dataJsonObj = new JSONObject(s_JSON);
            phone = dataJsonObj.getString(type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return phone;
    }

    public DoctorInfo[] parseDoctorsInfoFromJSON(String s_JSON, String type) {
        DoctorInfo[] item_set = null;
        JSONObject dataJsonObj = null;

        try {
            dataJsonObj = new JSONObject(s_JSON);
            JSONArray items = dataJsonObj.getJSONArray(type);
            item_set = new DoctorInfo[items.length()];
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                int id = item.getInt("id");
                String img = item.getString("img");
                String fullname = item.getString("fullname");
                String speciality = item.getString("speciality");
                String price = item.getString("price");
                String count_services = item.getString("count_services");
                String gps = item.getString("gps");
                String count_reviews = item.getString("count_reviews");
                double lat = item.getDouble("lat");
                double lng = item.getDouble("lng");
                item_set[i] = new DoctorInfo(id, img, fullname, speciality,
                        price, count_services, gps, count_reviews, lat, lng);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return item_set;
    }

    public Map<Integer, String> parseSpecialitiesFromJSON(String s_JSON, String type) {
        Map<Integer, String> item_set = new TreeMap<>();
        JSONObject dataJsonObj = null;

        try {
            dataJsonObj = new JSONObject(s_JSON);
            JSONArray items = dataJsonObj.getJSONArray(type);

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                String city_name = item.getString("name");
                int city_id = item.getInt("id");
                item_set.put(city_id, city_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return item_set;
    }

    public String loadStandartFromJSON(int choice_id){

        Resources r = context.getResources();
        InputStream is = r.openRawResource(choice_id);
        String myText = null;
        try {
            myText = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  myText;
    }

    public String loadDoctorsInfoFromJSON(int choice_id, int id_speciality){

        Resources r = context.getResources();
        InputStream is = r.openRawResource(choice_id);
        String myText = null;
        try {
            myText = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  myText;
    }

    public String loadSpecialitiesFromJSON(int choice_id, int id_city){

        Resources r = context.getResources();
        InputStream is = r.openRawResource(choice_id);
        String myText = null;
        try {
            myText = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  myText;
    }

    private String  convertStreamToString(InputStream is) throws IOException {
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
