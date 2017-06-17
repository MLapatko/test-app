package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import by.lovata.a2doc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFilterFragment extends DialogFragment {

    String[] menu_sort_items;
    int position_item_selected = 0;


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View screen_filter = inflater.inflate(R.layout.fragment_menu_filter, null);

        final AlertDialog alertDialog = builder.setView(screen_filter).create();


        Button button = (Button)screen_filter.findViewById(R.id.filterApplyButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }
}
