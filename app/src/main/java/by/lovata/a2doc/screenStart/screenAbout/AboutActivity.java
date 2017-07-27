package by.lovata.a2doc.screenStart.screenAbout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import by.lovata.a2doc.BaseMenuActivity;
import by.lovata.a2doc.R;

public class AboutActivity extends BaseMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initialToolBarMenu();
    }
}
