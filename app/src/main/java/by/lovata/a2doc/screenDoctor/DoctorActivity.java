package by.lovata.a2doc.screenDoctor;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.BaseMenuActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.aboutDoctor.AboutDoctorActivity;
import by.lovata.a2doc.screenDoctor.aboutDoctor.PagerAdapterAboutDoctor;
import by.lovata.a2doc.screenDoctor.aboutDoctor.screenQualification.QualificationFragment;
import by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews.ReviewFragment;
import by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews.Reviews;
import by.lovata.a2doc.screenRecordDoctor.RecordDoctorActivity;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;
import by.lovata.a2doc.screenViewDoctor.SelectDoctor;

public class DoctorActivity extends BaseMenuActivity implements OnMapReadyCallback {

    public static final String SAVEPARAMETER_PARSALABEL = "SAVEPARAMETER_PARSALABEL";

    private static final String SAVEPARAMETER_PARSALABEL_SAVE = "SAVEPARAMETER_PARSALABEL_SAVE";
    private static final String ID_ORGANIZATION_SAVE = "ID_ORGANIZATION_SAVE";
    private static final String ID_SERVICE_SAVE = "ID_SERVICE_SAVE";
    public static final String ID_SELECTED_DOCTOR = "ID_SELECTED_DOCTOR";

    private SaveParameter saveParameter;
    private int id_organization;
    private int id_filter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int idDoctor;

    private GoogleMap googleMap;
    private Map<Integer, Marker> markers;
    private APIMethods apiMethods;
    private SlidingUpPanelLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        if (savedInstanceState == null) {
            initializeData();
        } else {
            restoreData(savedInstanceState);
        }

        initialalizeConfigiguration();
        initialalizeView();
        setupTabIcons();
        initialalizeMap(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVEPARAMETER_PARSALABEL_SAVE, saveParameter);
        outState.putInt(ID_ORGANIZATION_SAVE, id_organization);
        outState.putInt(ID_SERVICE_SAVE, id_filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_about_doctor, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.view_about_doctor:
                layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
               // clickViewAboutDoctor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            this.googleMap = googleMap;
            //Move camera to initial position (Belarus)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(54, 27), 5f));
            changeCoordinate();

            markers = new HashMap<>();
            for (int id_organization : saveParameter.getSelectDoctor().
                    getDoctorInfo().getId_organization()) {
                Marker location = googleMap.addMarker(setParamMarker(id_organization));
                markers.put(id_organization, location);
                if (id_organization == this.id_organization) {
                    location.showInfoWindow();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MarkerOptions setParamMarker(int id_organization) {
        MarkerOptions markerOptions = new MarkerOptions();
        double lat_marker = saveParameter.getOrganizations().get(id_organization).getLat();
        double lng_marker = saveParameter.getOrganizations().get(id_organization).getLng();
        String name = saveParameter.getOrganizations().get(id_organization).getName();

        markerOptions.title(name);
        markerOptions.position(new LatLng(lat_marker, lng_marker));

        return markerOptions;
    }

    private double getLat() {
        return saveParameter.getOrganizations()
                .get(id_organization).getLat();
    }

    private double getLng() {
        return saveParameter.getOrganizations()
                .get(id_organization).getLng();
    }

    private void initializeData() {
        saveParameter = getIntent().getParcelableExtra(SAVEPARAMETER_PARSALABEL);
        id_organization = saveParameter.getSelectDoctor().getId_organization();
        id_filter = saveParameter.getSelectDoctor().getId_filter();
    }

    private void restoreData(Bundle savedInstanceState) {
        saveParameter = savedInstanceState.getParcelable(SAVEPARAMETER_PARSALABEL_SAVE);
        id_organization = savedInstanceState.getInt(ID_ORGANIZATION_SAVE);
        id_filter = savedInstanceState.getInt(ID_SERVICE_SAVE);
    }

    private void changeCoordinate() {
        if (markers != null) {
            markers.get(id_organization).showInfoWindow();
        }

        double lat = getLat();
        double lng = getLng();
        LatLng visible = new LatLng(lat, lng);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(visible)      // Sets the center of the map to location user
                .zoom(10)          // Sets the zoom
                .bearing(0)        // Sets the orientation of the camera to east
                .tilt(0)           // Sets the tilt of the camera to 40 degrees
                .build();          // Creates a CameraPosition from the builder

        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void initialalizeView() {

        String[] organizations_name = getOrganizationName(saveParameter);
        int positionOrganization = getPositionOrganization(saveParameter)+1;

        ListView organizations_profile = (ListView)findViewById(R.id.organizations_profile);
        setHeader(organizations_profile,saveParameter);
        setFooter(organizations_profile);

        organizations_profile.setAdapter(new ArrayAdapter<>(
                this,
                R.layout.organization_item ,
                organizations_name));

        organizations_profile.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        organizations_profile.setItemChecked(positionOrganization,true);
        organizations_profile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id_organization = saveParameter.getSelectDoctor().
                        getDoctorInfo().getId_organization()[position-1];
                changeCoordinate();
            }
        });
    }
    private void setFooter(ListView listView){

        View footer=getLayoutInflater().inflate(R.layout.footer_doctor_profile_main_content,null);
        idDoctor=saveParameter.getSelectDoctor().getId_doctor();
        viewPager = (ViewPager) footer.findViewById(R.id.viewpager_doctor_about);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) footer.findViewById(R.id.tabs_doctor_about);
        tabLayout.setupWithViewPager(viewPager);
        String[] services_name = getServicesName();
        int posotion_service = getPositionService(saveParameter);
        Spinner services_profile = (Spinner) footer.findViewById(R.id.services_profile);
        services_profile.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_activated_1,
                services_name));
        services_profile.setSelection(posotion_service);
        services_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_filter = setService(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        apiMethods=new APIMethods(this);
        Button btn_record = (Button) footer.findViewById(R.id.btn_record);
        if(apiMethods.getTimesFromJSON( saveParameter.getSelectDoctor().getId_doctor(),
                saveParameter.getIdSpeciality()).size()==0){
            btn_record.setEnabled(false);
            btn_record.setBackgroundColor(Color.parseColor("#ececec"));
            btn_record.setTextColor(Color.parseColor("#000000"));
            btn_record.setText("Нет приема");
        }
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorActivity.this, RecordDoctorActivity.class);
                setupSaveParameter();
                intent.putExtra(RecordDoctorActivity.SAVEPARAMETER_PARSALABEL, saveParameter);

                startActivity(intent);
            }

            private void setupSaveParameter() {
                SelectDoctor selectDoctor = saveParameter.getSelectDoctor();
                selectDoctor.setId_organization(id_organization);
                selectDoctor.setId_filter(id_filter);
                saveParameter.setSelectDoctor(selectDoctor);
            }
        });

        listView.addFooterView(footer);
    }
    private void setHeader(ListView listView,SaveParameter saveParameter){
        View header=getLayoutInflater().inflate(R.layout.header_doctor_profile_main_content,null);



        String img = getIMG();
        ImageView img_profile = (ImageView) header.findViewById(R.id.img_profile);
        Picasso.with(this)
                .load(img)
                .placeholder(R.drawable.ic_file_download_24dp)
                .error(R.drawable.ic_error_24dp)
                .into(img_profile);

        String name = getName();
        TextView fio_profile = (TextView) header.findViewById(R.id.fio_profile);
        fio_profile.setText(name);

        String speciality = getSpeciality();
        TextView speciality_profile = (TextView) header.findViewById(R.id.speciality_profile);
        speciality_profile.setText(speciality);

        String experience = getExperience();
        TextView expirience_profile = (TextView) header.findViewById(R.id.expirience_profile);
        expirience_profile.setText(experience);

        if (saveParameter.getSelectDoctor().getDoctorInfo().isMerto()) {
            ImageView is_metro = (ImageView) header.findViewById(R.id.profile_ismetro);
            is_metro.setImageResource(R.drawable.ic_directions_transit_24dp);
        }

        if (saveParameter.getSelectDoctor().getDoctorInfo().isBaby()) {
            ImageView is_baby = (ImageView) header.findViewById(R.id.profile_isbaby);
            is_baby.setImageResource(R.drawable.ic_child_friendly_24dp);
        }

        listView.addHeaderView(header);
    }
    private String getIMG() {
        return saveParameter.getSelectDoctor().getDoctorInfo().getUrl_img();
    }

    private String getName() {
        return saveParameter.getSelectDoctor().getDoctorInfo().getFull_name();
    }

    private String getSpeciality() {
        return saveParameter.getSelectDoctor().getDoctorInfo().getSpeciality();
    }

    private String getExperience() {
        String experience = getString(R.string.profile_experience);
        String count_year = Integer.toString(saveParameter.getSelectDoctor().getDoctorInfo().getExperience());
        String year = getString(R.string.profile_year);
        return String.format("%s %s %s", experience, count_year, year);
    }
    private void initialalizeConfigiguration() {
        layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        layout.setPanelHeight(0);
       //layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
      layout.setDragView(findViewById(R.id.view_about_doctor));
        //layout.setEnabled(true);
        setTitle(getString(R.string.profile));
    }

    private void initialalizeMap(Bundle savedInstanceState) {
        MapView mapView = (MapView) findViewById(R.id.map_profile);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);
    }

    public static String[] getOrganizationName(SaveParameter saveParameter) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (int id : saveParameter.getSelectDoctor().getDoctorInfo().getId_organization()) {
            arrayList.add(saveParameter.getOrganizations().get(id).getName());
        }

        return arrayList.toArray(new String[arrayList.size()]);
    }

    public static int getPositionOrganization(SaveParameter saveParameter) {
        int position = 0;
        int id_organization = saveParameter.getSelectDoctor().getId_organization();
        for (int key : saveParameter.getSelectDoctor().getDoctorInfo().getId_organization()) {
            if (key == id_organization) {
                break;
            }
            position++;
        }

        return position;
    }

    private String[] getServicesName() {
        ArrayList<String> arrayList = new ArrayList<>();

        for (int id : saveParameter.getSelectDoctor().getDoctorInfo().getService_list().keySet()) {
            arrayList.add(saveParameter.getServices().get(id)+" "+(saveParameter.getSelectDoctor().
                    getDoctorInfo().getService_list().get(id)+"руб"));
        }

        return arrayList.toArray(new String[arrayList.size()]);
    }

    public static  int getPositionService(SaveParameter saveParameter) {
        int position_service = 0;
        int id_filter = saveParameter.getSelectDoctor().getId_filter();

        for (int key : saveParameter.getSelectDoctor().getDoctorInfo().getService_list().keySet()) {
            if (key == id_filter) {
                break;
            }
            position_service++;
        }

        return position_service;
    }

    private int setService(int position) {
        Set<Integer> set_id_filter = saveParameter.getSelectDoctor().
                getDoctorInfo().getService_list().keySet();
        return set_id_filter.toArray(new Integer[set_id_filter.size()])[position];
    }

    private void clickViewAboutDoctor() {
        Intent intent = new Intent(DoctorActivity.this, AboutDoctorActivity.class);
        int id_doctor_selected = saveParameter.getSelectDoctor().getId_doctor();
        intent.putExtra(AboutDoctorActivity.ID_SELECTED_DOCTOR, id_doctor_selected);

        startActivity(intent);
    }






    private void setupViewPager(ViewPager viewPager) {
        PagerAdapterAboutDoctor adapter = new PagerAdapterAboutDoctor(getSupportFragmentManager());

        Fragment fragment_review = new ReviewFragment();
        Fragment fragment_qualification = new QualificationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID_SELECTED_DOCTOR, idDoctor);

        fragment_review.setArguments(bundle);
        fragment_qualification.setArguments(bundle);

        adapter.addFragment(fragment_qualification, "ONE");

        adapter.addFragment(fragment_review, "TWO");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText(R.string.about_doctor_qualification);
        tabLayout.getTabAt(1).setText(R.string.about_doctor_review);
    }
}
