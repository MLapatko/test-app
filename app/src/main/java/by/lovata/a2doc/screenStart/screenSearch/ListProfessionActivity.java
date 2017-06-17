package by.lovata.a2doc.screenStart.screenSearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenViewDoctor.ViewDoctorActivity;

public class ListProfessionActivity extends AppCompatActivity {

    final String[] catNamesArray = new String[] { "Рыжик", "Барсик", "Мурзик",
            "Мурка", "Васька", "Томасина", "Бобик", "Кристина", "Пушок",
            "Дымка", "Кузя", "Китти", "Барбос", "Масяня", "Симба" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_profession);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this,
                R.layout.speciality_item,
                catNamesArray);

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
