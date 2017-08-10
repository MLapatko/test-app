package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import by.lovata.a2doc.API.APIMethods;
import by.lovata.a2doc.R;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;
import by.lovata.a2doc.screenViewDoctor.OrganizationInfo;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;
import by.lovata.a2doc.screenViewDoctor.ViewDoctorActivity;
import by.lovata.a2doc.screenViewDoctor.screenMapDoctor.MapDoctorFragment;

import static java.security.AccessController.getContext;

class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    private ArrayList<DoctorInfo> array_doctors;
    private Map<Integer, String> services;
    private Map<Integer, OrganizationInfo> organizations;
    private Listener listener;
    private Context context;
    private  APIMethods apiMethods;
    private int id_filter;
    private int idSpeciality;
    private SaveParameter saveParameter;
    public static interface Listener {
        public void onClickRecord(int id_doctor, int id_filter, int id_organization);

        public void onClickDoctor(int id_doctor, int id_filter, int id_organization);
    }

    DoctorsAdapter(Context context, Map<Integer, String> sevices,
                   Map<Integer, OrganizationInfo> organizations,SaveParameter saveParameter) {
        this.context = context;
        this.services = sevices;
        this.organizations = organizations;
        this.saveParameter=saveParameter;
    }

    public void setApiMethods(APIMethods apiMethods) {this.apiMethods = apiMethods;}

    public void setIdSpeciality(int idSpeciality) {this.idSpeciality = idSpeciality;}

    public void setArray_doctors(ArrayList<DoctorInfo> doctors) {
        this.array_doctors = doctors;
    }

    public void setId_filter(int id_filter) {
        this.id_filter = id_filter;
    }

    public ArrayList<DoctorInfo> getArray_doctors() {
        return array_doctors;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public DoctorsAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_doctor, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        final ClickDoctor clickDoctor = new ClickDoctor(array_doctors.get(position).getId(), id_filter);
        final ClickRecord clickRecord = new ClickRecord(array_doctors.get(position).getId(), id_filter);
        clickDoctor.setId_organization(array_doctors.get(position).getId_organization()[0]);
        clickRecord.setId_organization(array_doctors.get(position).getId_organization()[0]);


        final Button btn_record_doctor = (Button) cardView.findViewById(R.id.btn_card_doctor);
        //если у врача нет расписания, делаем кнопку неактивной и изменяем Background
        if(apiMethods.getTimesFromJSON(array_doctors.get(position).getId(),idSpeciality).size()==0){
            btn_record_doctor.setEnabled(false);
            btn_record_doctor.setBackgroundColor(Color.parseColor("#ececec"));
            btn_record_doctor.setTextColor(Color.parseColor("#000000"));
            btn_record_doctor.setText("Нет приема");
        }
        btn_record_doctor.setOnClickListener(clickRecord);
        ImageView image_doctor = (ImageView) cardView.findViewById(R.id.img_card_doctor);

        Log.e("mylog","array_doctors.get(position).getUrl_img()"+array_doctors.get(position).getUrl_img());
        Picasso.with(context)
                .load(array_doctors.get(position).getUrl_img())
                .placeholder(R.drawable.ic_file_download_24dp)
                .error(R.drawable.ic_error_24dp)
                .into(image_doctor);
        cardView.setOnClickListener(clickDoctor);

        TextView full_name_doctor = (TextView) cardView.findViewById(R.id.fio_card_doctor);
        full_name_doctor.setText(array_doctors.get(position).getFull_name());
        full_name_doctor.setOnClickListener(clickDoctor);

        TextView speciality_doctor = (TextView) cardView.findViewById(R.id.speciality_card_doctor);
        speciality_doctor.setText(array_doctors.get(position).getSpeciality());

        final String[] services_name = getServicesName(position);
        final Spinner servicesDoctor = (Spinner) cardView.findViewById(R.id.services_card_doctor);
        final ArrayAdapter adapter=new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_activated_1,
                services_name);
        servicesDoctor.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(services.get(id_filter));
        servicesDoctor.setSelection(spinnerPosition);
        servicesDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //ищем позицию элемента, который будет выделен, по значению
                int spinnerPosition = adapter.getPosition(services.get(id_filter)+" "
                        +array_doctors.get(position).getService_list().get(id_filter)+"руб");
                servicesDoctor.setSelection(spinnerPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] organizations_name = getOrganizationName(position);
        ListView gps_doctor = (ListView) cardView.findViewById(R.id.gps_card_doctor);
        gps_doctor.setAdapter(new ArrayAdapter<>(
                context,R.layout.map_list_item,
                R.id.organization_name,organizations_name));
        final int[] id_organizations = array_doctors.get(position).getId_organization();
      gps_doctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
              ArrayList<DoctorInfo>docInf=new ArrayList<DoctorInfo>();
              docInf.add(array_doctors.get(position));
             // int org[]=new int [1];
              //org[0]=array_doctors.get(position).getId_organization()[pos];
              //docInf.get(0).setId_organization(org);
              saveParameter.setDoctorsInfo(docInf);
              Fragment fragment = new MapDoctorFragment();

              Bundle bundle = new Bundle();
              bundle.putParcelable(MapDoctorFragment.SAVEPARAMETER_PARSALABEL, saveParameter);
              bundle.putInt(MapDoctorFragment.POSITION,pos);
              fragment.setArguments(bundle);
              FragmentTransaction ft = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
              ft.replace(R.id.contain_view_doctor, fragment, ViewDoctorActivity.MAP_VIEW_FRAGMENT);
              ft.commit();
          }
      });

        TextView review_doctor = (TextView) cardView.findViewById(R.id.review_card_doctor);
        review_doctor.setText(String.format("%s %s", Integer.toString(array_doctors.get(position).getCount_reviews()),
                context.getString(R.string.list_doctors_reviw)));

    }

    @Override
    public int getItemCount() {
        return array_doctors.size();
    }

    private String[] getServicesName(int position) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int id : array_doctors.get(position).getService_list().keySet()) {
            arrayList.add(services.get(id) +" "
                    +array_doctors.get(position).getService_list().get(id)+"руб");
        }

        return arrayList.toArray(new String[arrayList.size()]);
    }

    private String[] getOrganizationName(int position) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (int id : array_doctors.get(position).getId_organization()) {
            arrayList.add(organizations.get(id).getName());
        }

        return arrayList.toArray(new String[arrayList.size()]);
    }

    public void synchronizedAdapter() {
        notifyDataSetChanged();
    }

    private class ClickDoctor implements View.OnClickListener {

        int id_doctor;
        int id_filter;
        int id_organization;

        ClickDoctor(int id_doctor, int id_filter) {
            this.id_doctor = id_doctor;
            this.id_filter = id_filter;
        }

        public void setId_organization(int organization) {
            this.id_organization = organization;
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClickDoctor(id_doctor, id_filter, id_organization);
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
