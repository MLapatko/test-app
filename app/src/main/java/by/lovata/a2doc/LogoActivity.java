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
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.screenStart.MainActivity;


public class LogoActivity extends AppCompatActivity {

    private static Map<Integer, String> cities;
    private static Map<Integer, String> specialities;
    private static String phone;

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

                APIMethods apiMethods = new APIMethods(LogoActivity.this);

                String s_phone = apiMethods.loadStandartFromJSON(R.raw.phone);
                phone = apiMethods.parsePhoneFromJSON(s_phone, "phone");

                String s_cities = apiMethods.loadStandartFromJSON(R.raw.cities);
                cities = apiMethods.parseCitiesFromJSON(s_cities, "cities");

                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);

                Integer[] id_cityArray;
                id_cityArray = cities.keySet().toArray(new Integer[cities.size()]);
                int id_city = sharedPreferences.getInt(MainActivity.CITY_SELECT, id_cityArray[0]);

                String s_specialities = apiMethods.loadSpecialitiesFromJSON(R.raw.specialities, id_city);
                specialities = apiMethods.parseSpecialitiesFromJSON(s_specialities, "specialities");

                startActivity(new Intent(LogoActivity.this, MainActivity.class));
                finish();
            }
        });
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

    public static String getPhone() {
        return phone;
    }

}
