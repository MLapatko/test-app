package by.lovata.a2doc.screenStart.screenCity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.MainActivity;

import static android.content.Context.MODE_PRIVATE;
import static by.lovata.a2doc.screenViewDoctor.ViewDoctorActivity.LIST_MODE_VIEW;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabCityFragment extends Fragment {

    String[] cities;


    public TabCityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_root = inflater.inflate(R.layout.fragment_tab_cities, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);

        Set<String> set_cities = sharedPreferences.getStringSet(MainActivity.CITY_ARRAY, new HashSet<String>());
        cities = set_cities.toArray(new String[set_cities.size()]);

        int city_item = sharedPreferences.getInt(MainActivity.CITY_SELECT, 0);
        if (cities.length <= city_item) city_item = 0;

        ListView lst_city = (ListView)view_root.findViewById(R.id.list_city);
        lst_city.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.select_dialog_singlechoice, cities));
        lst_city.setItemChecked(city_item, true);
        lst_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(MainActivity.CITY_SELECT, position);
                editor.apply();
            }
        });

        return view_root;
    }



}
