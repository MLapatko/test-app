package by.lovata.a2doc.API;


import android.content.Context;
import android.content.res.Resources;

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

    public Set<String> parseFromJSON(String s_JSON, String type) {
        Set<String> item_set = new HashSet<>();
        JSONObject dataJsonObj = null;

        try {
            dataJsonObj = new JSONObject(s_JSON);
            JSONArray items = dataJsonObj.getJSONArray(type);

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                String city_name = item.getString("name");
                item_set.add(city_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return item_set;
    }

    public String loadCitiesFromJSON(int choice_id){

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

    public String  convertStreamToString(InputStream is) throws IOException {
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
