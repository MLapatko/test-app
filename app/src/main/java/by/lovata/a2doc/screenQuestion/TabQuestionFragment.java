package by.lovata.a2doc.screenQuestion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.lovata.a2doc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabQuestionFragment extends Fragment {


    public TabQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_question, container, false);
    }

}
