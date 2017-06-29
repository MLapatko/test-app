package by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews;


import android.os.Parcel;
import android.os.Parcelable;

public class Reviews implements Parcelable {

    private int id;
    private String name;
    private String date;
    private String discription;
    private boolean recommend;
    private boolean status = false;

    public Reviews(int id, String name, String date, String discription, boolean recommend) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.discription = discription;
        this.recommend = recommend;
    }

    protected Reviews(Parcel in) {
        id = in.readInt();
        name = in.readString();
        date = in.readString();
        discription = in.readString();
        recommend = in.readByte() != 0;
        status = in.readByte() != 0;
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
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
        dest.writeString(date);
        dest.writeString(discription);
        dest.writeByte((byte) (recommend ? 1 : 0));
        dest.writeByte((byte) (status ? 1 : 0));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDiscription() {
        return discription;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }
}
