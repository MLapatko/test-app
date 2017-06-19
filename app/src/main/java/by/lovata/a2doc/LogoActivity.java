package by.lovata.a2doc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import by.lovata.a2doc.screenStart.MainActivity;


public class LogoActivity extends AppCompatActivity {

    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String s_cities = loadFromJSON(R.raw.cities);
                Set<String> cities = parseFromJSON(s_cities, "cities");
                editor.putStringSet(MainActivity.CITY_ARRAY, cities);

                String s_specialities = loadFromJSON(R.raw.specialities);
                Set<String> specialities = parseFromJSON(s_specialities, "specialities");
                editor.putStringSet(MainActivity.SPECIALITIES_ARRAY, specialities);

                editor.apply();

                startActivity(new Intent(LogoActivity.this, MainActivity.class));
                finish();
            }
        });
    }


    private Set<String> parseFromJSON(String s_JSON, String type) {
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

    private String loadFromJSON(int choice_id){

        Resources r = getResources();
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
