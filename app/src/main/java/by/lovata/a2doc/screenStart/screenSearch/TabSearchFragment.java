package by.lovata.a2doc.screenStart.screenSearch;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import by.lovata.a2doc.LogoActivity;
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

        View view_call_phone = (View) root_view.findViewById(R.id.call_phone);
        view_call_phone.setOnClickListener(clickListener_view_call_phone);

        TextView phone = (TextView) root_view.findViewById(R.id.phone);
        phone.setText(LogoActivity.phone);

        return root_view;
    }


    View.OnClickListener clickListener_view_call_phone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = "tel:" + getResources().getString(R.string.phone_call);
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phone));
            startActivity(intent);
        }
    };

    View.OnClickListener clickListener_btn_profession = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ListProfessionActivity.class);
            startActivity(intent);
        }
    };

}
