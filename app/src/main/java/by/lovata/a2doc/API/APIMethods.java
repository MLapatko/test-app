package by.lovata.a2doc.API;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.aboutDoctor.screenQualification.Qualification;
import by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews.Reviews;
import by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor.Times;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.OrganizationInfo;

public class APIMethods {

    private Context context;

    public APIMethods(Context context) {
        this.context = context;
    }

    public Map<Integer, String> getCitiesFromJSON() {
        Map<Integer, String> cities_map = new TreeMap<>();
        JSONObject dataJsonObj = null;
        String cities_JSON = loadCitiesFromAPI(R.raw.cities);

        try {
            dataJsonObj = new JSONObject(cities_JSON);
            JSONArray items = dataJsonObj.getJSONArray("cities");

            String city_name;
            int city_id;
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                city_name = item.getString("name");
                city_id = item.getInt("id");
                cities_map.put(city_id, city_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cities_map;
    }

    private String loadCitiesFromAPI(int choice_id) {
        load();

        Resources r = context.getResources();
        InputStream is = r.openRawResource(choice_id);
        String cities_JSON = null;
        try {
            cities_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities_JSON;
    }

    public String getPhoneFromJSON() {
        String phone = null;
        String phone_JSON = loadPhoneFromAPI(R.raw.phone);
        JSONObject dataJsonObj;

        try {
            dataJsonObj = new JSONObject(phone_JSON);
            phone = dataJsonObj.getString("phone");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return phone;
    }

    private String loadPhoneFromAPI(int choice_id) {
        load();

        Resources r = context.getResources();
        InputStream is = r.openRawResource(choice_id);
        String phone_JSON = null;
        try {
            phone_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return phone_JSON;
    }

    public Map<Integer, String> getSpecialitiesFromJSON(int id_city) {
        Map<Integer, String> specialities_map = new TreeMap<>();
        JSONObject dataJsonObj = null;
        String specialities_JSON = loadSpecialitiesFromAPI(id_city);

        try {
            dataJsonObj = new JSONObject(specialities_JSON);
            JSONArray items = dataJsonObj.getJSONArray("specialities");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                String speciality_name = item.getString("name");
                int speciality_id = item.getInt("id");
                specialities_map.put(speciality_id, speciality_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return specialities_map;
    }

    private String loadSpecialitiesFromAPI(int id_city) {
        load();

        Resources r = context.getResources();
        InputStream is = null;
        switch (id_city) {
            case 1:
                is = r.openRawResource(R.raw.specialities_cityid_1);
                break;
            case 2:
                is = r.openRawResource(R.raw.specialities_cityid_2);
                break;
            case 3:
                is = r.openRawResource(R.raw.specialities_cityid_3);
                break;
        }

        String specialities_JSON = null;
        try {
            specialities_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return specialities_JSON;
    }

    public DoctorInfo[] getDoctorsInfoFromJSON(int id_city, int id_speciality) {
        DoctorInfo[] item_set = null;
        JSONObject dataJsonObj = null;
        String doctorsInfo_JSON = loadDoctorsInfoFromAPI(id_city, id_speciality);

        try {
            dataJsonObj = new JSONObject(doctorsInfo_JSON);
            JSONArray items = dataJsonObj.getJSONArray("doctors");
            item_set = new DoctorInfo[items.length()];
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                int id = item.getInt("id");

                JSONArray id_organizationArray = item.getJSONArray("id_organization");
                int[] id_organization = new int[id_organizationArray.length()];
                for (int j = 0; j < id_organizationArray.length(); j++) {
                    id_organization[j] = id_organizationArray.getInt(j);
                }

                String img = item.getString("img");
                String fullname = item.getString("fullname");
                String speciality = item.getString("speciality");

                JSONArray service_listArray = item.getJSONArray("service_list");
                Map<Integer, Integer> service_list = new TreeMap<>();
                for (int j = 0; j < service_listArray.length(); j++) {
                    JSONObject item_service_list = service_listArray.getJSONObject(j);
                    int key_service = item_service_list.getInt("id");
                    int price_service = item_service_list.getInt("price");
                    service_list.put(key_service, price_service);
                }

                int count_reviews = item.getInt("count_reviews");
                int experience = item.getInt("experience");
                boolean merto = item.getBoolean("merto");
                boolean baby = item.getBoolean("baby");
                item_set[i] = new DoctorInfo(id, id_organization, img, fullname, speciality,
                        service_list, count_reviews, experience, merto, baby);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return item_set;
    }

    private String loadDoctorsInfoFromAPI(int id_city, int id_speciality) {
        load();

        Resources r = context.getResources();
        InputStream is = null;
        switch (id_city) {
            case 1:
                is = r.openRawResource(R.raw.doctors_cityid_1_specialityid);
                break;
            case 2:
                is = r.openRawResource(R.raw.doctors_cityid_2_specialityid);
                break;
            case 3:
                is = r.openRawResource(R.raw.doctors_cityid_3_specialityid);
                break;
        }

        String doctorsInfo_JSON = null;
        try {
            doctorsInfo_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doctorsInfo_JSON;
    }

    public Map<Integer, OrganizationInfo> getOrganizationsInfoFromJSON(int id_city, int id_speciality) {
        Map<Integer, OrganizationInfo> organizationInfo_map = new TreeMap<>();
        JSONObject dataJsonObj = null;
        String organizationInfo_JSON = loadOrganizationsInfoFromAPI(id_city, id_speciality);

        try {
            dataJsonObj = new JSONObject(organizationInfo_JSON);
            JSONArray items = dataJsonObj.getJSONArray("organizations");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                int id = item.getInt("id");
                String name = item.getString("name");
                double lat = item.getDouble("lat");
                double lng = item.getDouble("lng");
                organizationInfo_map.put(id, new OrganizationInfo(id, name, lat, lng));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return organizationInfo_map;
    }

    private String loadOrganizationsInfoFromAPI(int id_city, int id_speciality) {
        load();

        Resources r = context.getResources();
        InputStream is = null;
        switch (id_city) {
            case 1:
                is = r.openRawResource(R.raw.organizations_cityid_1_specialityid);
                break;
            case 2:
                is = r.openRawResource(R.raw.organizations_cityid_2_specialityid);
                break;
            case 3:
                is = r.openRawResource(R.raw.organizations_cityid_3_specialityid);
                break;
        }

        String doctorsInfo_JSON = null;
        try {
            doctorsInfo_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doctorsInfo_JSON;
    }

    public Map<Integer, String> getServicesFromJSON(int id_city, Set<Integer> services_list) {
        Map<Integer, String> services_map = new TreeMap<>();
        JSONObject dataJsonObj = null;
        String services_JSON = loadServicesFromAPI(id_city, services_list);

        try {
            dataJsonObj = new JSONObject(services_JSON);
            JSONArray items = dataJsonObj.getJSONArray("services");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                String service_name = item.getString("name");
                int service_id = item.getInt("id");
                services_map.put(service_id, service_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return services_map;
    }

    private String loadServicesFromAPI(int id_city, Set<Integer> services_list) {
        load();

        Resources r = context.getResources();
        InputStream is = null;
        switch (id_city) {
            case 1:
                is = r.openRawResource(R.raw.service_cityid_1);
                break;
            case 2:
                is = r.openRawResource(R.raw.service_cityid_2);
                break;
            case 3:
                is = r.openRawResource(R.raw.service_cityid_3);
                break;
        }

        String specialities_JSON = null;
        try {
            specialities_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return specialities_JSON;
    }

    public Times[] getTimesFromJSON(int id_doctor, int id_filter, int id_organization, int week) {
        Times[] time = null;
        JSONObject dataJsonObj = null;
        String doctorsInfo_JSON = loadTimesFromAPI(id_doctor, id_filter, id_organization, week);

        try {
            dataJsonObj = new JSONObject(doctorsInfo_JSON);
            JSONArray items = dataJsonObj.getJSONArray("dates");
            time = new Times[items.length()];
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                String day = item.getString("day");

                JSONArray times = item.getJSONArray("times");
                String[] times_array = new String[times.length()];
                for (int j = 0; j < times.length(); j++) {
                    times_array[j] = times.getString(j);
                }

                String start = item.getString("start");
                String stop = item.getString("stop");

                time[i] = new Times(day, times_array, start, stop);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }

    private String loadTimesFromAPI(int id_doctor, int id_filter, int id_organization, int week) {
        load();

        Resources r = context.getResources();
        InputStream is = null;
        switch (week) {
            case 0:
                is = r.openRawResource(R.raw.timetable_doctorid_1);
                break;
            default:
                is = r.openRawResource(R.raw.timetable_doctorid_2);
                break;
        }

        String doctorsInfo_JSON = null;
        try {
            doctorsInfo_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doctorsInfo_JSON;
    }

    public Reviews[] getReviewsFromJSON(int id_doctor) {
        Reviews[] reviews = null;
        JSONObject dataJsonObj = null;
        String reviewses_JSON = loadReviewsFromAPI(id_doctor);

        try {
            dataJsonObj = new JSONObject(reviewses_JSON);
            JSONArray items = dataJsonObj.getJSONArray("reviews");
            reviews = new Reviews[items.length()];

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                int id = item.getInt("id");
                String name = item.getString("name");
                String date = item.getString("date");
                String discription = item.getString("discription");
                boolean recommend = item.getBoolean("recommend");
                reviews[i] = new Reviews(id, name, date, discription, recommend);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }

    private String loadReviewsFromAPI(int id_doctor) {
        load();

        Resources r = context.getResources();
        InputStream is = r.openRawResource(R.raw.reviews);

        String specialities_JSON = null;
        try {
            specialities_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return specialities_JSON;
    }

    public Qualification getQualificationFromJSON(int id_doctor) {
        Qualification qualification = null;
        JSONObject dataJsonObj = null;
        String qualification_JSON = loadQualificationFromAPI(id_doctor);

        try {
            dataJsonObj = new JSONObject(qualification_JSON);
            JSONObject item = dataJsonObj.getJSONObject("qualification");

            String treatment = item.getString("treatment");

            JSONArray items_updatequalification = item.getJSONArray("updatequalification");
            ArrayList<String> updatequalification_period = new ArrayList<>();
            ArrayList<String> updatequalification_description = new ArrayList<>();
            for (int i = 0; i < items_updatequalification.length(); i++) {
                JSONObject item_updatequalification = items_updatequalification.getJSONObject(i);
                updatequalification_period.add(item_updatequalification.getString("period"));
                updatequalification_description.add(item_updatequalification.getString("description"));
            }

            JSONArray items_experience = item.getJSONArray("experience");
            ArrayList<String> experience_period = new ArrayList<>();
            ArrayList<String> experience_description = new ArrayList<>();
            for (int i = 0; i < items_experience.length(); i++) {
                JSONObject item_experience = items_experience.getJSONObject(i);
                experience_period.add(item_experience.getString("period"));
                experience_description.add(item_experience.getString("description"));
            }

            JSONArray items_education = item.getJSONArray("education");
            ArrayList<String> education_period = new ArrayList<>();
            ArrayList<String> education_description = new ArrayList<>();
            for (int i = 0; i < items_education.length(); i++) {
                JSONObject item_education = items_education.getJSONObject(i);
                education_period.add(item_education.getString("period"));
                education_description.add(item_education.getString("description"));
            }


            qualification = new Qualification(treatment,
                    updatequalification_period, updatequalification_description,
                    experience_period, experience_description,
                    education_period, education_description);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return qualification;
    }

    private String loadQualificationFromAPI(int id_doctor) {
        load();

        Resources r = context.getResources();
        InputStream is = r.openRawResource(R.raw.qualification);

        String specialities_JSON = null;
        try {
            specialities_JSON = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return specialities_JSON;
    }

    private String convertStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = is.read();
        while (i != -1) {
            baos.write(i);
            i = is.read();
        }
        return baos.toString();
    }

    private void load() {
        if (!hasConnection()) {
            //showDialog();
        }

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(context.getString(R.string.API_connect_title));
        builder.setMessage(context.getString(R.string.API_connect_message));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.API_connect_positive_btn),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ((Activity) context).finish();
                    }
                });
        builder.setNegativeButton(context.getString(R.string.API_connect_negative_btn),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ((Activity) context).moveTaskToBack(true);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
