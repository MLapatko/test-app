package by.lovata.a2doc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import by.lovata.a2doc.aboutScreen.AboutFragment;
import by.lovata.a2doc.doctorsListScreen.DoctorsListFragment;
import by.lovata.a2doc.for_medcenters.ForMedcentersFragment;
import by.lovata.a2doc.mainScreen.MainScreenFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String NAME_PREFERANCE = "2doc_pref";
    public static final String SEARCH_MODE = "SEARCH_MODE";
    public static final String CITY_SELECTED = "CITY_SELECTED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Spinner spinner_cities = (Spinner) findViewById(R.id.spinner_cities);
        spinner_cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreferences = getSharedPreferences(NAME_PREFERANCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(MainActivity.CITY_SELECTED, getResources().getStringArray(R.array.cities)[position]);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (savedInstanceState == null) {
            initSettings();
            setFragment(new MainScreenFragment());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.toolbar_search);
        SharedPreferences sharedPreferences = getSharedPreferences(NAME_PREFERANCE, MODE_PRIVATE);

        String search_mode = sharedPreferences.getString(MainActivity.SEARCH_MODE, "");

        if (search_mode.equals("show")) {
            if (!menuItem.isVisible()) menuItem.setVisible(true);
        } else if (search_mode.equals("hide")) {
            if (menuItem.isVisible()) menuItem.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences(NAME_PREFERANCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.SEARCH_MODE, "hide");
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.toolbar_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        SharedPreferences sharedPreferences = getSharedPreferences(NAME_PREFERANCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new MainScreenFragment();
            editor.putString(MainActivity.SEARCH_MODE, "hide");
        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
            editor.putString(MainActivity.SEARCH_MODE, "show");
        } else if (id == R.id.nav_formedcenter) {
            //fragment = new ForMedcentersFragment();
            fragment = new DoctorsListFragment();
            editor.putString(MainActivity.SEARCH_MODE, "show");
        } else if (id == R.id.nav_call) {

        }


        if (!item.isChecked()) {
            editor.apply();
            setFragment(fragment);
            invalidateOptionsMenu();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main_screen, fragment)
                .commit();
    }

    void initSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(NAME_PREFERANCE, MODE_PRIVATE);

        String search_mode = sharedPreferences.getString(MainActivity.SEARCH_MODE, "");
        if (search_mode.equals("")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(MainActivity.SEARCH_MODE, "hide");
            editor.apply();
        }

        String city_selected = sharedPreferences.getString(MainActivity.CITY_SELECTED, "");
        if (city_selected.equals("")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(MainActivity.CITY_SELECTED, getResources().getStringArray(R.array.cities)[0]);
            editor.apply();
        }

    }

}
