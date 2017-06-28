package by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.aboutDoctor.AboutDoctorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment implements View.OnKeyListener{

    public static final String REVIEWS_SAVE = "REVIEWS_SAVE";

    private Reviews[] reviewses;
    MaterialSheetFab<Fab> materialSheetFab;

    public ReviewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view_root = inflater.inflate(R.layout.fragment_review, container, false);

        if (savedInstanceState == null) {
            initializeData();
        } else {
            restoreData(savedInstanceState);
        }

        setAdapter(view_root);

        Fab fab = (Fab) view_root.findViewById(R.id.fab);
        View sheetView = view_root.findViewById(R.id.fab_sheet);
        View overlay = view_root.findViewById(R.id.overlay);
        int sheetColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        int fabColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
                sheetColor, fabColor);

        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Called when the material sheet's "show" animation starts.
            }

            @Override
            public void onSheetShown() {
                // Called when the material sheet's "show" animation ends.
            }

            @Override
            public void onHideSheet() {
                // Called when the material sheet's "hide" animation starts.
            }

            public void onSheetHidden() {
                // Called when the material sheet's "hide" animation ends.
            }
        });

        return view_root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArray(REVIEWS_SAVE, reviewses);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
            return true;
        }
        return false;
    }

    private void setAdapter(View view_root) {
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getContext(), reviewses);
        RecyclerView recyclerView = (RecyclerView) view_root.findViewById(R.id.recyclerview_review);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(reviewsAdapter);
    }

    private void restoreData(Bundle savedInstanceState) {
        reviewses = (Reviews[]) savedInstanceState.getParcelableArray(REVIEWS_SAVE);
    }

    private void initializeData() {
        int id_doctor = getArguments().getInt(AboutDoctorActivity.ID_SELECTED_DOCTOR);

        APIMethods apiMethods = new APIMethods(getContext());
        reviewses = apiMethods.getReviewsFromJSON(id_doctor);
    }


}
