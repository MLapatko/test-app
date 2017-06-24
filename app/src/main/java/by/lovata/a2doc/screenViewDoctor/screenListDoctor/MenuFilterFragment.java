package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.LogoActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenViewDoctor.ViewDoctorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFilterFragment extends DialogFragment {

    public static interface AccessFilter {
        public void setFilters(int id_filter, boolean metro, boolean baby);
        public Map<Integer, String> getSevices();
    }

    public static final String ID_FILTER_SELECTED = "ID_FILTER_SELECTED";

    public static final String IS_METRO = "IS_METRO";
    public static final String IS_BABY = "IS_BABY";

    int id_filter;
    boolean metro;
    boolean baby;
    Map<Integer, String> services;


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View screen_filter = inflater.inflate(R.layout.fragment_menu_filter, null);

        final AlertDialog alertDialog = builder.setView(screen_filter).create();
        services = ((AccessFilter) getParentFragment()).getSevices();

        id_filter = getArguments().getInt(ID_FILTER_SELECTED);
        metro = getArguments().getBoolean(IS_METRO);
        baby = getArguments().getBoolean(IS_BABY);

        final CheckBox checkBox_metro = (CheckBox)screen_filter.findViewById(R.id.filterCheckBox2);
        checkBox_metro.setChecked(metro);
        final CheckBox checkBox_baby = (CheckBox)screen_filter.findViewById(R.id.filterCheckBox1);
        checkBox_baby.setChecked(baby);

        Set<Integer> set_key_services = services.keySet();
        final Integer[] key_services = set_key_services.toArray(new Integer[set_key_services.size()]);

        Collection<String> set_services = services.values();
        String[] array_services = set_services.toArray(new String[set_services.size()]);

        final Spinner spinner = (Spinner) screen_filter.findViewById(R.id.services_spinner);
        spinner.setAdapter(new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                array_services));

        int position = getPosition(key_services);
        spinner.setSelection(position);

        Button button = (Button) screen_filter.findViewById(R.id.filterApplyButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_filter = key_services[spinner.getSelectedItemPosition()];
                metro = checkBox_metro.isChecked();
                baby = checkBox_baby.isChecked();
                ((AccessFilter) getParentFragment()).setFilters(id_filter, metro, baby);
                alertDialog.cancel();
            }
        });
        return alertDialog;
    }

    private int getPosition(Integer[] key_services) {
        int position = 0;
        for (Integer key: key_services) {
            if (key == id_filter) {
                break;
            }
            position++;
        }
        return position;
    }

}
