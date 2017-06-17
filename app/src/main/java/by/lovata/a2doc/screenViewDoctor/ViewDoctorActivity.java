package by.lovata.a2doc.screenViewDoctor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenStart.MainActivity;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.ListDoctorFragment;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.MenuFilterFragment;
import by.lovata.a2doc.screenViewDoctor.screenListDoctor.MenuSortFragment;
import by.lovata.a2doc.screenViewDoctor.screenMapDoctor.MapDoctorFragment;

public class ViewDoctorActivity extends AppCompatActivity {

    public static final String NAME_MODE_VIEW = "MODE_VIEW";
    public static final String LIST_MODE_VIEW = "LIST_MODE_VIEW";
    public static final String MAP_MODE_VIEW = "MAP_MODE_VIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor);

        if (savedInstanceState == null) {
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(NAME_MODE_VIEW, LIST_MODE_VIEW);
            editor.apply();
            Fragment fragment = new ListDoctorFragment();
            FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
            fragmentManager.replace(R.id.contain_view_doctor, fragment);
            fragmentManager.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_change, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);
        String title_list = getResources().getString(R.string.view_change_list);
        String title_map = getResources().getString(R.string.view_change_map);

        if (sharedPreferences.getString(NAME_MODE_VIEW, "").equals(MAP_MODE_VIEW)) {
            MenuItem menu_change = menu.findItem(R.id.view_change);
            menu_change.setTitle(title_list);
            menu_change.setIcon(R.drawable.ic_format_align_justify_24dp);

            MenuItem menu_sort = menu.findItem(R.id.menu_sort);
            menu_sort.setVisible(false);
        } else if (sharedPreferences.getString(NAME_MODE_VIEW, "").equals(LIST_MODE_VIEW)) {
            MenuItem menu_sort = menu.findItem(R.id.menu_sort);
            menu_sort.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_change:
                clickViewChange(item);
                return true;
            case R.id.menu_filter:
                MenuFilterFragment dialog_filter = new MenuFilterFragment();
                dialog_filter.show(getSupportFragmentManager(), "filter");
                return true;
            case R.id.menu_sort:
                MenuSortFragment dialog_sort = new MenuSortFragment();
                dialog_sort.show(getSupportFragmentManager(), "sort");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clickViewChange(MenuItem menuItem) {
        String title = menuItem.getTitle().toString();
        String title_list = getResources().getString(R.string.view_change_list);
        String title_map = getResources().getString(R.string.view_change_map);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Fragment fragment = null;
        FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();

        if (title_list.equals(title)) {
            menuItem.setTitle(title_map);
            menuItem.setIcon(R.drawable.ic_map_24dp);

            editor.putString(NAME_MODE_VIEW, LIST_MODE_VIEW);

            fragment = new ListDoctorFragment();
        } else if (title_map.equals(title)) {
            menuItem.setTitle(title_list);
            menuItem.setIcon(R.drawable.ic_format_align_justify_24dp);

            editor.putString(NAME_MODE_VIEW, MAP_MODE_VIEW);

            fragment = new MapDoctorFragment();
        }

        editor.apply();

        fragmentManager.replace(R.id.contain_view_doctor, fragment);
        fragmentManager.commit();
    }
}
