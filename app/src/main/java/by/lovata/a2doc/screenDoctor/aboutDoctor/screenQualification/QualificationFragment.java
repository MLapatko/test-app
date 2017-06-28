package by.lovata.a2doc.screenDoctor.aboutDoctor.screenQualification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.aboutDoctor.AboutDoctorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class QualificationFragment extends Fragment {

    private static final String QUALIFICATION_SAVE = "QUALIFICATION_SAVE";

    private Qualification qualification;

    public QualificationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_root = inflater.inflate(R.layout.fragment_qualification, container, false);

        if (savedInstanceState == null) {
            initializeData();
        } else {
            restoreData(savedInstanceState);
        }

        initializeView(view_root);

        return view_root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(QUALIFICATION_SAVE, qualification);
    }

    private void initializeView(View view_root) {
        setTreatment(view_root);
        setUpdatequalification(view_root);
        setExperience(view_root);
        setEducation(view_root);
    }

    private void setEducation(View view_root) {
        String[] education_label = getEducationLabel();
        String[] education_text = getEducationText();
        if (education_label.length == 0) {
            LinearLayout linearLayout = (LinearLayout) view_root.findViewById(R.id.education_text);
            setErrorContain(linearLayout);
        } else {
            int id = R.id.education_text;
            int id_img = R.drawable.ic_account_balance_24dp;
            createGroupView(view_root, education_label, education_text, id, id_img);
        }
    }

    private void setExperience(View view_root) {
        String[] experience_label = getExperienceLabel();
        String[] experience_text = getExperienceText();
        if (experience_label.length == 0) {
            LinearLayout linearLayout = (LinearLayout) view_root.findViewById(R.id.experience_text);
            setErrorContain(linearLayout);
        } else {
            int id = R.id.experience_text;
            int id_img = R.drawable.ic_work_24dp;
            createGroupView(view_root, experience_label, experience_text, id, id_img);
        }
    }

    private void setUpdatequalification(View view_root) {
        String[] updatequalification_label = getUpdatequalificationLabel();
        String[] updatequalification_text = getUpdatequalificationText();
        if (updatequalification_label.length == 0) {
            LinearLayout linearLayout = (LinearLayout) view_root.findViewById(R.id.updatequalification_text);
            setErrorContain(linearLayout);
        } else {
            int id = R.id.updatequalification_text;
            int id_img = R.drawable.ic_language_24dp;
            createGroupView(view_root, updatequalification_label, updatequalification_text, id, id_img);
        }
    }

    private void createGroupView(View view_root,
                                 String[] updatequalification_label,
                                 String[]updatequalification_text,
                                 int id, int id_img) {
        LinearLayout linearLayout = (LinearLayout) view_root.findViewById(id);

        for (int i = 0; i < updatequalification_label.length; i++) {
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.qualification, null);

            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.qualification_img);
            imageView.setImageResource(id_img);

            TextView textView_label = (TextView) relativeLayout.findViewById(R.id.qualification_period);
            textView_label.setText(updatequalification_label[i]);

            TextView textView_text = (TextView) relativeLayout.findViewById(R.id.qualification_discription);
            textView_text.setText(updatequalification_text[i]);

            linearLayout.addView(relativeLayout);
        }
    }

    private void setErrorContain(LinearLayout linearLayout) {
        String error = getString(R.string.review_error);
        TextView textView = new TextView(getContext());
        textView.setText(error);
        linearLayout.addView(textView);
    }

    private String[] getEducationLabel() {
        return qualification.getEducation_period()
                .toArray(new String[qualification.getEducation_period().size()]);
    }

    private String[] getEducationText() {
        return qualification.getEducation_description()
                .toArray(new String[qualification.getEducation_description().size()]);
    }

    private String[] getExperienceLabel() {
        return qualification.getExperience_period()
                .toArray(new String[qualification.getExperience_period().size()]);
    }

    private String[] getExperienceText() {
        return qualification.getExperience_description()
                .toArray(new String[qualification.getExperience_description().size()]);
    }

    private String[] getUpdatequalificationText() {
        return qualification.getUpdatequalification_description()
                .toArray(new String[qualification.getUpdatequalification_description().size()]);
    }

    private String[] getUpdatequalificationLabel() {
        return qualification.getUpdatequalification_period()
                .toArray(new String[qualification.getUpdatequalification_period().size()]);
    }

    private void setTreatment(View view_root) {
        if (qualification.getTreatment().equals("null")) {
            TextView textView = (TextView) view_root.findViewById(R.id.treatment_text);
            textView.setText(getString(R.string.review_error));
        } else {
            TextView treatment_text = (TextView) view_root.findViewById(R.id.treatment_text);
            treatment_text.setText(qualification.getTreatment());
        }
    }

    private void initializeData() {
        int id_doctor = getArguments().getInt(AboutDoctorActivity.ID_SELECTED_DOCTOR);

        APIMethods apiMethods = new APIMethods(getContext());
        qualification = apiMethods.getQualificationFromJSON(id_doctor);
    }

    private void restoreData(Bundle savedInstanceState) {
        qualification = savedInstanceState.getParcelable(QUALIFICATION_SAVE);
    }

}
