package by.lovata.a2doc.screenViewDoctor;


import android.os.Parcel;
import android.os.Parcelable;

public class OrganizationInfo implements Parcelable {

    private int id;
    private String name;
    private double lat;
    private double lng;

    public OrganizationInfo(int id, String name, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    protected OrganizationInfo(Parcel in) {
        id = in.readInt();
        name = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<OrganizationInfo> CREATOR = new Creator<OrganizationInfo>() {
        @Override
        public OrganizationInfo createFromParcel(Parcel in) {
            return new OrganizationInfo(in);
        }

        @Override
        public OrganizationInfo[] newArray(int size) {
            return new OrganizationInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
