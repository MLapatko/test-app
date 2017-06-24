package by.lovata.a2doc.screenViewDoctor;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class SaveParameter implements Parcelable {

    public DoctorInfo[] doctorsInfo;
    public Map<Integer, String> sevices;
    public Map<Integer, OrganizationInfo> organizations;

    SaveParameter(DoctorInfo[] doctorsInfo, Map<Integer, String> sevices,
                    Map<Integer, OrganizationInfo> organizations) {
        this.doctorsInfo = doctorsInfo;
        this.sevices = sevices;
        this.organizations = organizations;
    }

    protected SaveParameter(Parcel in) {
        doctorsInfo = in.createTypedArray(DoctorInfo.CREATOR);
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
        dest.writeTypedArray(doctorsInfo, flags);
    }
}
