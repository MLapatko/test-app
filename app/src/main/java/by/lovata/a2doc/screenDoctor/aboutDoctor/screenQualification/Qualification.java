package by.lovata.a2doc.screenDoctor.aboutDoctor.screenQualification;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Qualification implements Parcelable{

    private String treatment;

    private ArrayList<String> updatequalification_period;
    private ArrayList<String> updatequalification_description;

    private ArrayList<String> experience_period;
    private ArrayList<String> experience_description;

    private ArrayList<String> education_period;
    private ArrayList<String> education_description;

    public Qualification(String treatment, ArrayList<String> updatequalification_period,
                         ArrayList<String> updatequalification_description,
                         ArrayList<String> experience_period, ArrayList<String> experience_description,
                         ArrayList<String> education_period, ArrayList<String> education_description) {
        this.treatment = treatment;
        this.updatequalification_period = updatequalification_period;
        this.updatequalification_description = updatequalification_description;
        this.experience_period = experience_period;
        this.experience_description = experience_description;
        this.education_period = education_period;
        this.education_description = education_description;
    }

    protected Qualification(Parcel in) {
        treatment = in.readString();
        updatequalification_period = in.createStringArrayList();
        updatequalification_description = in.createStringArrayList();
        experience_period = in.createStringArrayList();
        experience_description = in.createStringArrayList();
        education_period = in.createStringArrayList();
        education_description = in.createStringArrayList();
    }

    public static final Creator<Qualification> CREATOR = new Creator<Qualification>() {
        @Override
        public Qualification createFromParcel(Parcel in) {
            return new Qualification(in);
        }

        @Override
        public Qualification[] newArray(int size) {
            return new Qualification[size];
        }
    };

    public String getTreatment() {
        return treatment;
    }

    public ArrayList<String> getUpdatequalification_period() {
        return updatequalification_period;
    }

    public ArrayList<String> getUpdatequalification_description() {
        return updatequalification_description;
    }

    public ArrayList<String> getExperience_period() {
        return experience_period;
    }

    public ArrayList<String> getExperience_description() {
        return experience_description;
    }

    public ArrayList<String> getEducation_period() {
        return education_period;
    }

    public ArrayList<String> getEducation_description() {
        return education_description;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setUpdatequalification_period(ArrayList<String> updatequalification_period) {
        this.updatequalification_period = updatequalification_period;
    }

    public void setUpdatequalification_description(ArrayList<String> updatequalification_description) {
        this.updatequalification_description = updatequalification_description;
    }

    public void setExperience_period(ArrayList<String> experience_period) {
        this.experience_period = experience_period;
    }

    public void setExperience_description(ArrayList<String> experience_description) {
        this.experience_description = experience_description;
    }

    public void setEducation_period(ArrayList<String> education_period) {
        this.education_period = education_period;
    }

    public void setEducation_description(ArrayList<String> education_description) {
        this.education_description = education_description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(treatment);
        dest.writeStringList(updatequalification_period);
        dest.writeStringList(updatequalification_description);
        dest.writeStringList(experience_period);
        dest.writeStringList(experience_description);
        dest.writeStringList(education_period);
        dest.writeStringList(education_description);
    }
}
