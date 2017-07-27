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
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.screenStart.MainActivity;


public class LogoActivity extends AppCompatActivity {

    private static Map<Integer, String> cities;
    private static Map<Integer, String> specialities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                APIMethods apiMethods = new APIMethods(LogoActivity.this);

                String phone = apiMethods.getPhoneFromJSON();

                cities = apiMethods.getCitiesFromJSON();

                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);
                List<Integer> cities_id = new ArrayList<>(cities.keySet());
                int id_city = sharedPreferences.getInt(MainActivity.CITY_SELECT, cities_id.get(0));
                setCitySelectId(id_city);

                specialities = apiMethods.getSpecialitiesFromJSON(id_city);
                Log.e("spec","specialities"+specialities);
                Intent intent = new Intent(LogoActivity.this, NewMainActivity.class);
                intent.putExtra(MainActivity.PHONE, phone);
                startActivity(intent);

                finish();
            }
        }, 1000);
    }

    public static void setSpecialities(Map<Integer, String> specialities) {
        LogoActivity.specialities = specialities;
    }

    public static Map<Integer, String> getCities() {
        return cities;
    }

    public static Map<Integer, String> getSpecialities() {
        return specialities;
    }

    private boolean setCitySelectId(int id_city_select) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MainActivity.CITY_SELECT, id_city_select);
        return editor.commit();
    }

}
