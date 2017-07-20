package by.lovata.a2doc.screenViewDoctor;


import android.os.Parcel;
import android.os.Parcelable;

public class SelectDoctor implements Parcelable {

    private int id_doctor;
    private int id_filter;
    private int id_organization;

    private String day;
    private String time;

    DoctorInfo doctorInfo;

    public SelectDoctor(int id_doctor, int id_filter, int id_organization, String day, String time, DoctorInfo doctorInfo) {
        this.id_doctor = id_doctor;
        this.id_filter = id_filter;
        this.id_organization = id_organization;
        this.day = day;
        this.time = time;
        this.doctorInfo = doctorInfo;
    }

    protected SelectDoctor(Parcel in) {
        id_doctor = in.readInt();
        id_filter = in.readInt();
        id_organization = in.readInt();
        day = in.readString();
        time = in.readString();
        this.doctorInfo = DoctorInfo.class.cast(in.readParcelable(DoctorInfo.class.getClassLoader()));
    }

    public static final Creator<SelectDoctor> CREATOR = new Creator<SelectDoctor>() {
        @Override
        public SelectDoctor createFromParcel(Parcel in) {
            return new SelectDoctor(in);
        }

        @Override
        public SelectDoctor[] newArray(int size) {
            return new SelectDoctor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_doctor);
        dest.writeInt(id_filter);
        dest.writeInt(id_organization);
        dest.writeString(day);
        dest.writeString(time);
        dest.writeParcelable(doctorInfo, flags);
    }

    @Override
    public String toString() {
        return "SelectDoctor{" +
                "id_doctor=" + id_doctor +
                ", id_filter=" + id_filter +
                ", id_organization=" + id_organization +
                ", day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", doctorInfo=" + doctorInfo +
                '}';
    }

    public void setId_doctor(int id_doctor) {

        this.id_doctor = id_doctor;
    }

    public void setId_filter(int id_filter) {
        this.id_filter = id_filter;
    }

    public void setId_organization(int id_organization) {
        this.id_organization = id_organization;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId_doctor() {
        return id_doctor;
    }

    public int getId_filter() {
        return id_filter;
    }

    public int getId_organization() {
        return id_organization;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public DoctorInfo getDoctorInfo() {
        return doctorInfo;
    }

    public void setDoctorInfo(DoctorInfo doctorInfo) {
        this.doctorInfo = doctorInfo;
    }
}
