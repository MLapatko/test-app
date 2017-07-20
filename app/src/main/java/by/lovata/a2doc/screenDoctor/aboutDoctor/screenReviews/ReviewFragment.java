package by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenDoctor.aboutDoctor.AboutDoctorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    private static final String REVIEWS_SAVE = "REVIEWS_SAVE";

    private ArrayList<Reviews> reviews;
    private AboutDoctorActivity listener;
    private TextView textView;
    public ReviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_root = inflater.inflate(R.layout.fragment_review, container, false);
        textView= (TextView) view_root.findViewById(R.id.notFound);
        if (savedInstanceState == null) {
            initializeData();
        } else {
            restoreData(savedInstanceState);
        }

        initializeView(view_root);
        setAdapter(view_root);

        return view_root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(REVIEWS_SAVE, reviews);
    }

    private void initializeView(View view_root) {
        FloatingActionButton fab = (FloatingActionButton) view_root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReviewWriteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setAdapter(View view_root) {
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getContext(), reviews);
        RecyclerView recyclerView = (RecyclerView) view_root.findViewById(R.id.recyclerview_review);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(reviewsAdapter);
        if(reviews.size()==0)
            textView.setText("Нет отзывов");

    }

    private void restoreData(Bundle savedInstanceState) {
        reviews = savedInstanceState.getParcelableArrayList(REVIEWS_SAVE);
    }

    private void initializeData() {
        int id_doctor = getArguments().getInt(AboutDoctorActivity.ID_SELECTED_DOCTOR);
        APIMethods apiMethods = new APIMethods(getContext());
        reviews = apiMethods.getReviewsFromJSON(id_doctor);
    }

}
