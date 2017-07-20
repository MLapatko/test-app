package by.lovata.a2doc.screenViewDoctor;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import by.lovata.a2doc.LogoActivity;

public class DoctorInfo implements Parcelable {

    private int id;
    private int count_reviews;
    private int experience;
    private int[] id_organization;
    private String url_img;
    private String full_name;
    private String speciality;

    private Map<Integer, Integer> service_list;

    private boolean merto;
    private boolean baby;

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
        id = in.readInt();
        id_organization = in.createIntArray();
        url_img = in.readString();
        full_name = in.readString();
        speciality = in.readString();
        service_list = readServices(in);
        count_reviews = in.readInt();
        experience = in.readInt();
        merto = in.readByte() != 0;
        baby = in.readByte() != 0;
    }

    private void writeServices(Parcel out, int flags, Map<Integer, Integer> service_list) {
        int size = service_list.size();
        out.writeInt(size);
        for (Map.Entry<Integer, Integer> entry : service_list.entrySet()) {
            out.writeInt(entry.getKey());
            out.writeInt(entry.getValue());
        }
    }

    private Map<Integer, Integer> readServices(Parcel in) {
        Map<Integer, Integer> service_list = new TreeMap<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            Integer key = in.readInt();
            Integer value = in.readInt();
            service_list.put(key, value);
        }
        return service_list;
    }

    public static final Creator<DoctorInfo> CREATOR = new Creator<DoctorInfo>() {
        @Override
        public DoctorInfo createFromParcel(Parcel in) {
            return new DoctorInfo(in);
        }

        @Override
        public DoctorInfo[] newArray(int size) {
            return new DoctorInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeIntArray(id_organization);
        dest.writeString(url_img);
        dest.writeString(full_name);
        dest.writeString(speciality);
        writeServices(dest, flags, service_list);
        dest.writeInt(count_reviews);
        dest.writeInt(experience);
        dest.writeByte((byte) (merto ? 1 : 0));
        dest.writeByte((byte) (baby ? 1 : 0));
    }

    public int getId() {
        return id;
    }

    public int[] getId_organization() {
        return id_organization;
    }

    public String getUrl_img() {
        return url_img;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public Map<Integer, Integer> getService_list() {
        return service_list;
    }

    public int getCount_reviews() {
        return count_reviews;
    }

    public int getExperience() {
        return experience;
    }

    public boolean isMerto() {
        return merto;
    }

    public boolean isBaby() {
        return baby;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_organization(int[] id_organization) {
        this.id_organization = id_organization;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setService_list(Map<Integer, Integer> service_list) {
        this.service_list = service_list;
    }

    public void setCount_reviews(int count_reviews) {
        this.count_reviews = count_reviews;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setMerto(boolean merto) {
        this.merto = merto;
    }

    public void setBaby(boolean baby) {
        this.baby = baby;
    }

    @Override
    public String toString() {
        return  full_name ;
    }
}
