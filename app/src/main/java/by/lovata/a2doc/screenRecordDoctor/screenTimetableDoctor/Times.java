package by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor;


import android.os.Parcel;
import android.os.Parcelable;

public class Times implements Parcelable {

    String day;
    String[] times;
    String start;
    String stop;

    public Times(String day, String[] times, String start, String stop) {
        this.day = day;
        this.times = times;
        this.start = start;
        this.stop = stop;
    }

    protected Times(Parcel in) {
        day = in.readString();
        times = in.createStringArray();
        start = in.readString();
        stop = in.readString();
    }

    public static final Creator<Times> CREATOR = new Creator<Times>() {
        @Override
        public Times createFromParcel(Parcel in) {
            return new Times(in);
        }

        @Override
        public Times[] newArray(int size) {
            return new Times[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeStringArray(times);
        dest.writeString(start);
        dest.writeString(stop);
    }
}
