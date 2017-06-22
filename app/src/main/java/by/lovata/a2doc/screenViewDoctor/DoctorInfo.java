package by.lovata.a2doc.screenViewDoctor;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class DoctorInfo implements Parcelable{

    public int id;
    public int[] id_organization;
    public String url_img;
    public String full_name;
    public String speciality;
    public Map<Integer, Integer> service_list;
    public int count_reviews;
    public int experience;
    public boolean merto;
    public boolean baby;

    public DoctorInfo(int id, int[] id_organization, String url_img, String full_name,
                      String speciality, Map<Integer, Integer> service_list,
                      int count_reviews, int experience,
                      boolean merto, boolean baby) {
        this.id = id;
        this.id_organization = id_organization;
        this.url_img = url_img;
        this.full_name = full_name;
        this.speciality = speciality;
        this.service_list = service_list;
        this.count_reviews = count_reviews;
        this.experience = experience;
        this.merto = merto;
        this.baby = baby;
    }

    public DoctorInfo(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static final Parcelable.Creator<DoctorInfo> CREATOR = new Parcelable.Creator<DoctorInfo>() {

        @Override
        public DoctorInfo createFromParcel(Parcel source) {
            return new DoctorInfo(source);
        }

        @Override
        public DoctorInfo[] newArray(int size) {
            return new DoctorInfo[size];
        }
    };
}
