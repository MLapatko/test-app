package by.lovata.a2doc.screenViewDoctor;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SaveParameter implements Parcelable {

    private int id_spiciality;
    private int id_city;

    private int id_sort;

    private int id_filter;
    private boolean metro;
    private boolean baby;

    private ArrayList<DoctorInfo> doctorsInfo;
    private Map<Integer, String> services;
    private Map<Integer, OrganizationInfo> organizations;
    private SelectDoctor selectDoctor;


    public SaveParameter(int id_city, int id_spiciality, int id_filter, int id_sort,
                         ArrayList<DoctorInfo> doctorsInfo, Map<Integer, OrganizationInfo> organizations,
                         Map<Integer, String> sevices, boolean metro, boolean baby) {
        this.id_city = id_city;
        this.id_spiciality = id_spiciality;
        this.id_filter = id_filter;
        this.id_sort = id_sort;
        this.doctorsInfo = doctorsInfo;
        this.organizations = organizations;
        this.services = sevices;
        this.metro = metro;
        this.baby = baby;
    }

    protected SaveParameter(Parcel in) {
        id_spiciality = in.readInt();
        id_city = in.readInt();
        id_sort = in.readInt();
        id_filter = in.readInt();
        metro = in.readByte() != 0;
        baby = in.readByte() != 0;

        doctorsInfo=in.createTypedArrayList(DoctorInfo.CREATOR);
        services = readServices(in);
        organizations = readOrganizations(in);
        selectDoctor = SelectDoctor.class.cast(in.readParcelable(SelectDoctor.class.getClassLoader()));
    }


    private void writeServices(Parcel dest, int flags, Map<Integer, String> sevices) {
        int size = sevices.size();
        dest.writeInt(size);
        for (Map.Entry<Integer, String> entry : sevices.entrySet()) {
            dest.writeInt(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    private Map<Integer, String> readServices(Parcel in) {
        Map<Integer, String> sevices = new TreeMap<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            Integer key = in.readInt();
            String value = in.readString();
            sevices.put(key, value);
        }
        return sevices;
    }

    private void writeOrganizations(Parcel dest, int flags, Map<Integer, OrganizationInfo> organizations) {
        int size = organizations.size();
        dest.writeInt(size);
        for (Map.Entry<Integer, OrganizationInfo> entry : organizations.entrySet()) {
            dest.writeInt(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
    }

    private Map<Integer, OrganizationInfo> readOrganizations(Parcel in) {
        Map<Integer, OrganizationInfo> organizations = new TreeMap<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            Integer key = in.readInt();
            OrganizationInfo value = OrganizationInfo.class.cast(in.readParcelable(OrganizationInfo.class.getClassLoader()));
            organizations.put(key, value);
        }
        return organizations;
    }

    public static final Creator<SaveParameter> CREATOR = new Creator<SaveParameter>() {
        @Override
        public SaveParameter createFromParcel(Parcel in) {
            return new SaveParameter(in);
        }

        @Override
        public SaveParameter[] newArray(int size) {
            return new SaveParameter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_spiciality);
        dest.writeInt(id_city);
        dest.writeInt(id_sort);
        dest.writeInt(id_filter);
        dest.writeByte((byte) (metro ? 1 : 0));
        dest.writeByte((byte) (baby ? 1 : 0));
        dest.writeTypedList(doctorsInfo);
        writeServices(dest, flags, services);
        writeOrganizations(dest, flags, organizations);
        dest.writeParcelable(selectDoctor, flags);
    }

    public void setSelectDoctor(SelectDoctor selectDoctor) {
        this.selectDoctor = selectDoctor;
    }

    public SelectDoctor getSelectDoctor() {

        return selectDoctor;
    }

    public void setId_spiciality(int id_spiciality) {
        this.id_spiciality = id_spiciality;
    }

    public void setId_city(int id_city) {
        this.id_city = id_city;
    }

    public void setId_sort(int id_sort) {
        this.id_sort = id_sort;
    }

    public void setId_filter(int id_filter) {
        this.id_filter = id_filter;
    }

    public void setMetro(boolean metro) {
        this.metro = metro;
    }

    public void setBaby(boolean baby) {
        this.baby = baby;
    }

    public void setDoctorsInfo(ArrayList<DoctorInfo>doctorsInfo) {
        this.doctorsInfo = doctorsInfo;
    }

    public void setServices(Map<Integer, String> sevices) {
        this.services = sevices;
    }

    public void setOrganizations(Map<Integer, OrganizationInfo> organizations) {
        this.organizations = organizations;
    }

    public int getId_spiciality() {

        return id_spiciality;
    }

    public int getId_city() {
        return id_city;
    }

    public int getId_sort() {
        return id_sort;
    }

    public int getId_filter() {
        return id_filter;
    }

    public boolean isMetro() {
        return metro;
    }

    public boolean isBaby() {
        return baby;
    }

    public ArrayList<DoctorInfo> getDoctorsInfo() {
        return doctorsInfo;
    }

    public Map<Integer, String> getServices() {
        return services;
    }

    public Map<Integer, OrganizationInfo> getOrganizations() {
        return organizations;
    }

    @Override
    public String toString() {
        return "SaveParameter{" +
                "id_spiciality=" + id_spiciality +
                ", id_city=" + id_city +
                ", id_sort=" + id_sort +
                ", id_filter=" + id_filter +
                ", metro=" + metro +
                ", baby=" + baby +
                ", doctorsInfo=" + doctorsInfo +
                ", services=" + services +
                ", organizations=" + organizations +
                ", selectDoctor=" + selectDoctor +
                '}';
    }
}
