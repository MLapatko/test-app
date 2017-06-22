package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenViewDoctor.DoctorInfo;

class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    private DoctorInfo[] array_doctors;
    private Listener listener;
    private Context context;

    public static interface Listener {
        public void onClickRecord(int position);
        public void onClickDoctor(int position);
    }

    DoctorsAdapter(DoctorInfo[] doctors, Context context) {
        this.array_doctors = doctors;
        this.context = context;
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
        ClickRecord clickRecord = new ClickRecord(position);

        ImageView image_doctor = (ImageView) cardView.findViewById(R.id.img_card_doctor);
        Picasso.with(context)
                .load(array_doctors[position].url_img)
                .placeholder(R.drawable.ic_file_download_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .into(image_doctor);
        image_doctor.setOnClickListener(clickDoctor);

        TextView full_name_doctor = (TextView) cardView.findViewById(R.id.fio_card_doctor);
        full_name_doctor.setText(array_doctors[position].full_name);
        full_name_doctor.setOnClickListener(clickDoctor);

        TextView speciality_doctor = (TextView) cardView.findViewById(R.id.speciality_card_doctor);
        speciality_doctor.setText(array_doctors[position].speciality);

//        TextView price_doctor = (TextView) cardView.findViewById(R.id.price_of_consultation_card_doctor);
//        price_doctor.setText(array_doctors[position].price_of_consultation);
//
//        TextView service_doctor = (TextView) cardView.findViewById(R.id.services_card_doctor);
//        service_doctor.setText(array_doctors[position].service);
//
//        TextView gps_doctor = (TextView) cardView.findViewById(R.id.gps_card_doctor);
//        gps_doctor.setText(array_doctors[position].gps);
//
//        TextView review_doctor = (TextView) cardView.findViewById(R.id.review_card_doctor);
//        review_doctor.setText(array_doctors[position].review);

        Button btn_record_doctor = (Button) cardView.findViewById(R.id.btn_card_doctor);
        btn_record_doctor.setOnClickListener(clickRecord);

    }

    @Override
    public int getItemCount(){
        return array_doctors.length;
    }

    public void synchronizedAdapter(DoctorInfo[] array_doctors) {
        this.array_doctors = array_doctors;
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

        int position;

        ClickRecord(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClickRecord(position);
            }
        }
    }

}
