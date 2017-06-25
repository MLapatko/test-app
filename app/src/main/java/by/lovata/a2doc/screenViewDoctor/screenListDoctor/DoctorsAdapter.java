package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import by.lovata.a2doc.LogoActivity;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.OrganizationInfo;

class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    private DoctorInfo[] array_doctors;
    Map<Integer, String> sevices;
    Map<Integer, OrganizationInfo> organizations;

    private Listener listener;
    private Context context;

    private int id_filter;

    public static interface Listener {
        public void onClickRecord(int id_doctor, int id_filter, int id_organization);
        public void onClickDoctor(int position);
    }

    DoctorsAdapter(Context context, Map<Integer, String> sevices,
                    Map<Integer, OrganizationInfo> organizations) {
        this.context = context;
        this.sevices = sevices;
        this.organizations = organizations;
    }

    public void setArray_doctors(DoctorInfo[] doctors) {
        this.array_doctors = doctors;
    }

    public void setId_filter(int id_filter) {
        this.id_filter = id_filter;
    }

    public DoctorInfo[] getArray_doctors() {
        return array_doctors;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public DoctorsAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_doctor, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        CardView cardView = holder.cardView;
        ClickDoctor clickDoctor = new ClickDoctor(position);
        final ClickRecord clickRecord = new ClickRecord(array_doctors[position].getId(), id_filter);
        clickRecord.setId_organization(array_doctors[position].getId_organization()[0]);

        final Button btn_record_doctor = (Button) cardView.findViewById(R.id.btn_card_doctor);
        btn_record_doctor.setOnClickListener(clickRecord);

        ImageView image_doctor = (ImageView) cardView.findViewById(R.id.img_card_doctor);
        Picasso.with(context)
                .load(array_doctors[position].getUrl_img())
                .placeholder(R.drawable.ic_file_download_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .into(image_doctor);
        image_doctor.setOnClickListener(clickDoctor);

        TextView full_name_doctor = (TextView) cardView.findViewById(R.id.fio_card_doctor);
        full_name_doctor.setText(array_doctors[position].getFull_name());
        full_name_doctor.setOnClickListener(clickDoctor);

        TextView speciality_doctor = (TextView) cardView.findViewById(R.id.speciality_card_doctor);
        speciality_doctor.setText(array_doctors[position].getSpeciality());

        final TextView price_doctor = (TextView) cardView.findViewById(R.id.price_of_consultation_card_doctor);
        price_doctor.setText(Integer.toString(array_doctors[position].getService_list().get(id_filter)));

        String[] services_name = getServicesName(position);
        final Spinner services_doctor = (Spinner) cardView.findViewById(R.id.services_card_doctor);
        services_doctor.setAdapter(new ArrayAdapter<String>(
                context,
                android.R.layout.simple_list_item_activated_1,
                services_name));
        int posotion_service = getPositionService(id_filter);
        services_doctor.setSelection(posotion_service);
        services_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int posotion_service = getPositionService(id_filter);
                services_doctor.setSelection(posotion_service);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] organizations_name = getOrganizationName(position);
        Spinner gps_doctor = (Spinner) cardView.findViewById(R.id.gps_card_doctor);
        gps_doctor.setAdapter(new ArrayAdapter<String>(
                context,
                android.R.layout.simple_list_item_activated_1,
                organizations_name));
        final int[] id_organizations = array_doctors[position].getId_organization();
        gps_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clickRecord.setId_organization(id_organizations[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView review_doctor = (TextView) cardView.findViewById(R.id.review_card_doctor);
        review_doctor.setText(Integer.toString(array_doctors[position].getCount_reviews()));

    }

    @Override
    public int getItemCount(){
        return array_doctors.length;
    }

    private int getPositionService(int id_filter) {
        int position_service = 0;

        for (int key: array_doctors[0].getService_list().keySet()) {
            if (key == id_filter) {
                break;
            }
            position_service++;
        }

        return position_service;
    }

    private String[] getServicesName(int position) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (int id: array_doctors[position].getService_list().keySet()) {
            arrayList.add(sevices.get(id));
        }

        return arrayList.toArray(new String[arrayList.size()]);
    }

    private String[] getOrganizationName(int position) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (int id: array_doctors[position].getId_organization()) {
            arrayList.add(organizations.get(id).getName());
        }

        return arrayList.toArray(new String[arrayList.size()]);
    }

    public void synchronizedAdapter() {
        notifyDataSetChanged();
    }

    private class ClickDoctor implements View.OnClickListener {

        int position;

        ClickDoctor(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClickDoctor(position);
            }
        }
    }

    private class ClickRecord implements View.OnClickListener {

        int id_doctor;
        int id_filter;
        int id_organization;

        ClickRecord(int id_doctor, int id_filter) {
            this.id_doctor = id_doctor;
            this.id_filter = id_filter;
        }

        public void setId_organization(int organization) {
            this.id_organization = organization;
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClickRecord(id_doctor, id_filter, id_organization);
            }
        }
    }

}
