package by.lovata.a2doc.screenDoctor.aboutDoctor.screenQualification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.aboutDoctor.AboutDoctorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class QualificationFragment extends Fragment {

    private static final String QUALIFICATION_SAVE = "QUALIFICATION_SAVE";

    Qualification qualification;

    public QualificationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout view_root = (LinearLayout) inflater.inflate(R.layout.fragment_qualification, container, false);

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

    private void initializeView(LinearLayout view_root) {
        setTreatment(view_root);
        setUpdatequalification(view_root);


        TextView experience_label = (TextView) view_root.findViewById(R.id.experience_label);
        TextView education_label = (TextView) view_root.findViewById(R.id.education_label);

    }
    private void setUpdatequalification(LinearLayout view_root) {
        String[] updatequalification_label = qualification.getUpdatequalification_period()
                .toArray(new String[qualification.getUpdatequalification_period().size()]);
        String[] updatequalification_text = qualification.getUpdatequalification_description()
                .toArray(new String[qualification.getUpdatequalification_description().size()]);
        if (updatequalification_label.length == 0) {
            view_root.removeViewAt(R.id.updatequalification_label);
            view_root.removeViewAt(R.id.updatequalification_text);
        } else {
            int id = R.id.updatequalification_text;
            createGroupView(view_root, updatequalification_label, updatequalification_text, id);
        }
    }

    private void createGroupView(View view_root,
                                 String[] updatequalification_label,
                                 String[]updatequalification_text,
                                 int id) {
        LinearLayout linearLayout = (LinearLayout) view_root.findViewById(id);

        RelativeLayout relativeLayout = (RelativeLayout) getContext().getResources().getLayout(R.layout.qualification);
        for (int i = 0; i < updatequalification_label.length; i++) {
            TextView textView_label = (TextView) relativeLayout.findViewById(R.id.qualification_period);
            textView_label.setText(updatequalification_label[i]);

            TextView textView_text = (TextView) relativeLayout.findViewById(R.id.qualification_discription);
            textView_text.setText(updatequalification_text[i]);

            linearLayout.addView(relativeLayout);
        }
    }

    private void setTreatment(LinearLayout view_root) {
        if (qualification.getTreatment().equals("null")) {
            view_root.removeViewAt(R.id.treatment_label);
            view_root.removeViewAt(R.id.treatment_text);
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
