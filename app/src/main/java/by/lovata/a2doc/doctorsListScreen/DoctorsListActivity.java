package by.lovata.a2doc.doctorsListScreen;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import by.lovata.a2doc.R;
import by.lovata.a2doc.filterScreen.FilterActivity;

public class DoctorsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_btn_filter);
        floatingActionButton.setOnClickListener(onClickListener);

        ArrayList<DoctorInfo> arrayList = new ArrayList<>();
        arrayList.add(new DoctorInfo("1", "1", "1", "1", "1", "1"));
        arrayList.add(new DoctorInfo("2", "2", "2", "2", "2", "2"));
        arrayList.add(new DoctorInfo("3", "3", "3", "3", "3", "3"));
        arrayList.add(new DoctorInfo("4", "4", "4", "4", "4", "4"));
        arrayList.add(new DoctorInfo("5", "5", "5", "5", "5", "5"));

        DoctorsAdapter doctorsAdapter = new DoctorsAdapter(arrayList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_doctor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DoctorsListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(doctorsAdapter);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DoctorsListActivity.this, FilterActivity.class);
            startActivity(intent);
        }
    };
}
