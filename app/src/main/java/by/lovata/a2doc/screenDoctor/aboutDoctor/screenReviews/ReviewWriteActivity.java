package by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import by.lovata.a2doc.R;

public class ReviewWriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        initializeView();

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

    private void initializeView() {
        setTitle(getString(R.string.add_review));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);


        ImageView name = (ImageView) findViewById(R.id.recommend_send);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messanger = getString(R.string.add_review_sent);
                Toast.makeText(ReviewWriteActivity.this, messanger, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
