package by.lovata.a2doc.screenViewDoctor;


import android.os.Parcel;
import android.os.Parcelable;

public class DoctorInfo implements Parcelable{

    public int id;
    public String url_img;
    public String full_name;
    public String speciality;
    public String price_of_consultation;
    public String service;
    public String review;
    public String gps;
    public long lat;
    public long lng;

    public DoctorInfo(int id, String url_img, String full_name, String speciality,
                String price_of_consultation, String service, String gps,
                String review, long lat, long lng) {
        this.id = id;
        this.url_img = url_img;
        this.full_name = full_name;
        this.speciality = speciality;
        this.price_of_consultation = price_of_consultation;
        this.service = service;
        this.review = review;
        this.gps = gps;
        this.lat = lat;
        this.lng = lng;
    }

    public DoctorInfo(Parcel in) {
        this.id = id;
        this.url_img = url_img;
        this.full_name = full_name;
        this.speciality = speciality;
        this.price_of_consultation = price_of_consultation;
        this.service = service;
        this.review = review;
        this.gps = gps;
        this.lat = lat;
        this.lng = lng;
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
