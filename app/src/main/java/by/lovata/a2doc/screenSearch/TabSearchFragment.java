package by.lovata.a2doc.screenSearch;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import by.lovata.a2doc.R;

public class TabSearchFragment extends Fragment {


    public TabSearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_tab_search, container, false);

        Button btn_search = (Button) root_view.findViewById(R.id.search_fragment_btn_search);
        btn_search.setOnClickListener(clickListener_btn_profession);

        return root_view;
    }

    View.OnClickListener clickListener_btn_profession = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ListProfessionActivity.class);
            startActivity(intent);
        }
    };

}
