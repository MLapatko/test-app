package by.lovata.a2doc.screenStart.screenSearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.MainActivity;
import by.lovata.a2doc.screenViewDoctor.ViewDoctorActivity;

public class ListProfessionActivity extends AppCompatActivity {

    String[] specialities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_profession);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);

        Set<String> set_specialities = sharedPreferences.getStringSet(MainActivity.SPECIALITIES_ARRAY, new HashSet<String>());
        specialities = set_specialities.toArray(new String[set_specialities.size()]);
        Arrays.sort(specialities);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this,
                R.layout.speciality_item,
                specialities);

        ListView lst_profession = (ListView) findViewById(R.id.lst_profession);
        lst_profession.setAdapter(mAdapter);
        lst_profession.setOnItemClickListener(itemClickListener_lst_profession);
    }

    AdapterView.OnItemClickListener itemClickListener_lst_profession = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ListProfessionActivity.this, ViewDoctorActivity.class);
            startActivity(intent);
            finish();
        }
    };

}
