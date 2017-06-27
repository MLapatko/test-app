package by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import by.lovata.a2doc.R;

public class ShowReviewActivity extends AppCompatActivity {

    public static final String DISCRIBTION = "DISCRIBTION";

    public static final String DISCRIBTION_SAVE = "DISCRIBTION_SAVE";

    String discribtion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_review);

        if (savedInstanceState == null) {
            initializeData();
        } else {
            restoreData(savedInstanceState);
        }

        initializeView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(DISCRIBTION_SAVE, discribtion);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeData() {
        discribtion = getIntent().getStringExtra(DISCRIBTION);
    }

    private void restoreData(Bundle savedInstanceState) {
        discribtion = savedInstanceState.getString(DISCRIBTION_SAVE);
    }

    private void initializeView() {
        TextView discribtion_text = (TextView) findViewById(R.id.show_review_text);
        discribtion_text.setText(discribtion);
    }
}
