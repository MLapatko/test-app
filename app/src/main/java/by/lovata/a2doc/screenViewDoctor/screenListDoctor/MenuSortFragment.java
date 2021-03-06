package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenViewDoctor.ViewDoctorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuSortFragment extends DialogFragment {

    public static final String ID_SORT_SELECTED = "ID_SORT_SELECTED";

    String[] menu_sort_items;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int position_item_selected = getArguments().getInt(ID_SORT_SELECTED);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        menu_sort_items = getResources().getStringArray(R.array.menu_sort_items);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View screen_sort = inflater.inflate(R.layout.fragment_menu_sort, null);

        ListView lst_sort = (ListView) screen_sort.findViewById(R.id.lst_sort);
        lst_sort.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_single_choice,
                menu_sort_items
        ));
        lst_sort.setItemChecked(position_item_selected, true);

        final AlertDialog alertDialog = builder.setView(screen_sort).create();
        lst_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ListDoctorFragment) getParentFragment()).setId_sort(position, false);
                alertDialog.cancel();
            }
        });
        return alertDialog;
    }
}
