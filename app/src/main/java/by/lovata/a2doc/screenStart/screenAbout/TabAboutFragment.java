package by.lovata.a2doc.screenStart.screenAbout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.lovata.a2doc.R;

public class TabAboutFragment extends Fragment {


    public TabAboutFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_about, container, false);
    }

}
