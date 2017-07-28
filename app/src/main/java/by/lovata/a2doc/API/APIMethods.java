package by.lovata.a2doc.API;



import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
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

    public boolean isSpecBelongToDoctor(int idSpec, JSONArray doctorIdsSpec) throws JSONException {

        for (int i = 0; i <doctorIdsSpec.length() ; i++) {
            if (doctorIdsSpec.getInt(i)==idSpec)
                return true;
        }
        return false;
    }

        public ArrayList<DoctorInfo> getDoctorsInfoFromJSON(int id_city, int id_speciality) {
        ArrayList<DoctorInfo> item_set=new ArrayList<>();
        JSONObject dataJsonObj;
        String doctorsInfo_JSON = loadDoctorsInfoFromAPI(id_city);
        try {
            dataJsonObj = new JSONObject(doctorsInfo_JSON);
            JSONArray items = dataJsonObj.getJSONArray("doctors");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (id_speciality!=-1){
                    if (!isSpecBelongToDoctor(id_speciality,item.getJSONArray("id_specialities")))
                    continue;
                }
                int id = item.getInt("id");

                JSONArray id_organizationArray = item.getJSONArray("id_organization");
                int[] id_organization = new int[id_organizationArray.length()];
                for (int j = 0; j < id_organizationArray.length(); j++) {
                    id_organization[j] = id_organizationArray.getInt(j);
                }
                JSONArray idSpecArray = item.getJSONArray("id_specialities");
                int[] idSpecialities = new int[idSpecArray.length()];
                for (int j = 0; j < idSpecArray.length(); j++) {
                    idSpecialities[j] = idSpecArray.getInt(j);
                }

                String img = item.getString("img");
                String fullname = item.getString("fullname");
                String speciality = item.getString("speciality");

                JSONArray service_listArray = item.getJSONArray("service_list");
                Map<Integer, Integer> serviceList = new TreeMap<>();
                for (int j = 0; j < service_listArray.length(); j++) {
                    JSONObject item_service_list = service_listArray.getJSONObject(j);
                    int idService = item_service_list.getInt("id");
                    int priceService = item_service_list.getInt("price");
                    serviceList.put(idService,priceService);
                }

                int count_reviews = getReviewsFromJSON(id).size();
                int experience = item.getInt("experience");
                boolean merto = item.getBoolean("merto");
                boolean baby = item.getBoolean("baby");
                item_set.add(new DoctorInfo(id, id_organization,idSpecialities, img, fullname,
                        speciality, serviceList, count_reviews, experience, merto, baby));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return item_set;
    }

    private String loadDoctorsInfoFromAPI(int id_city) {
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
    // получает услуги из json
    public Map<Integer, String> getServicesFromJSON(int idCity, int idSpeciality) {
        Map<Integer, String> servicesMap = new TreeMap<>();
        JSONObject dataJsonObj;
        //loadServicesFromAPI(id_city) по id_city открывает нужный json со списком услуг
        String servicesJSON = loadServicesFromAPI(idCity);
        try {
            dataJsonObj = new JSONObject(servicesJSON);
            JSONArray items = dataJsonObj.getJSONArray("services");
            for (int i = 0; i < items.length(); i++) {
                //получаем услугу из json
                JSONObject item = items.getJSONObject(i);
                //если улуга не оказывается на специальности, id которой =idSpeciality, то пропускаем ее
                //иначе добавляем в servicesMap
                 if(idSpeciality!=-1){
                        if (!isSpecBelongToDoctor(idSpeciality, item.getJSONArray("id_speciality")))
                        continue;
                 }
                String serviceName = item.getString("name");
                int serviceId = item.getInt("id");
                servicesMap.put(serviceId, serviceName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return servicesMap;
    }

    private String loadServicesFromAPI(int id_city) {
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
    public  int findIdSpeciality(int id,int idCity){
        int ind=-1;
        JSONObject dataJsonObj;
        String servicesJSON = loadServicesFromAPI(idCity);
        try {
            dataJsonObj = new JSONObject(servicesJSON);
            JSONArray items = dataJsonObj.getJSONArray("services");
            for (int i = 0; i < items.length(); i++) {
                //получаем услугу из json
                JSONObject item = items.getJSONObject(i);
                if (item.getInt("id")==id) {
                   ind=item.getJSONArray("id_speciality").getInt(0);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ind;

    }

    public ArrayList<Times> getTimesFromJSON(int idDoctor, int idSpeciality) {
        ArrayList<Times> time = new ArrayList<>();
        JSONObject dataJsonObj;
        String doctorsInfoJSON = loadTimesFromAPI(idSpeciality);
        if (doctorsInfoJSON.equals(""))
            return time;
            try {
            dataJsonObj = new JSONObject(doctorsInfoJSON);
            JSONArray items = dataJsonObj.getJSONArray("dates");
            for (int i = 0; i < items.length(); i++) {

                JSONObject item = items.getJSONObject(i);
                JSONArray timetables=item.getJSONArray("timetable");
                int idDoctorJSON=item.getInt("id_doctor");
                if (idDoctorJSON!=idDoctor)
                    continue;
                for (int j = 0; j <timetables.length() ; j++) {

                    JSONObject timetable = timetables.getJSONObject(j);

                    String day = timetable.getString("day");

                    JSONArray times = timetable.getJSONArray("times");
                    String[] times_array = new String[times.length()];
                    for (int k = 0; k < times.length(); k++) {
                        times_array[k] = times.getString(k);
                    }

                    String start = timetable.getString("start");
                    String stop = timetable.getString("stop");

                    time.add(new Times(day, times_array, start, stop));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("mylog","time[]"+time.toString());
        return time;
    }

    private String loadTimesFromAPI(int idSpeciality) {
        load();

        Resources r = context.getResources();
        InputStream is=null;
        switch (idSpeciality) {
            case 14:
                is = r.openRawResource(R.raw.timetable_id_speciality_14);
                break;
            default: return "";
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
    public ArrayList<Reviews> getReviewsFromJSON(int id_doctor) {
        ArrayList<Reviews> reviews = new ArrayList<>();
        JSONObject dataJsonObj = null;
        String reviewses_JSON = loadReviewsFromAPI();

        try {
            dataJsonObj = new JSONObject(reviewses_JSON);
            JSONArray items = dataJsonObj.getJSONArray("reviews");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (!(item.getInt("id_doctor") ==id_doctor))
                    continue;
                int id = item.getInt("id");
                String name = item.getString("name");
                String date = item.getString("date");
                String discription = item.getString("discription");
                boolean recommend = item.getBoolean("recommend");
                reviews.add(new Reviews(id, name, date, discription, recommend));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }

    private String loadReviewsFromAPI() {
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


    }

}
