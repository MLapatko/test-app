package by.lovata.a2doc.screenStart.screenCity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.LogoActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.MainActivity;

import static android.content.Context.MODE_PRIVATE;
import static by.lovata.a2doc.screenViewDoctor.ViewDoctorActivity.LIST_MODE_VIEW;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabCityFragment extends Fragment {

    String[] cities;
    Integer[] key_cities;


    public TabCityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_root = inflater.inflate(R.layout.fragment_tab_cities, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);

        Collection<String> set_cities = LogoActivity.getCities().values();
        cities = set_cities.toArray(new String[set_cities.size()]);

        Set<Integer> set_key_cities = LogoActivity.getCities().keySet();
        key_cities = set_key_cities.toArray(new Integer[set_key_cities.size()]);

        int city_item = sharedPreferences.getInt(MainActivity.CITY_SELECT, key_cities[0]);
        int position = 0;
        for (int key: key_cities) {
            if (key == city_item) {
                break;
            }
            position++;
        }

        ListView lst_city = (ListView)view_root.findViewById(R.id.list_city);
        lst_city.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.select_dialog_singlechoice, cities));
        lst_city.setItemChecked(position, true);
        lst_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id_city_select = key_cities[position];
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(MainActivity.CITY_SELECT, id_city_select);
                editor.apply();

                APIMethods apiMethods = new APIMethods(getActivity());

                String s_specialities = apiMethods.loadSpecialitiesFromJSON(R.raw.specialities, id_city_select);
                LogoActivity.setSpecialities(apiMethods.parseSpecialitiesFromJSON(s_specialities, "specialities"));
            }
        });

        return view_root;
    }



}
