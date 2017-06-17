package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import by.lovata.a2doc.R;

class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    private ArrayList<DoctorInfo> array_dorcors;
    private Listener listener;
    public static interface Listener {
        public void onClickRecord(int position);
        public void onClickDoctor(int position);
    }

    DoctorsAdapter(ArrayList<DoctorInfo> doctors) {
        this.array_dorcors = doctors;
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

        ImageView image_doctor = (ImageView) cardView.findViewById(R.id.img_card_doctor);
        image_doctor.setImageResource(array_dorcors.get(position).url_img);
        image_doctor.setOnClickListener(clickDoctor);

        TextView full_name_doctor = (TextView) cardView.findViewById(R.id.fio_card_doctor);
        full_name_doctor.setText(array_dorcors.get(position).full_name);
        full_name_doctor.setOnClickListener(clickDoctor);

        TextView speciality_doctor = (TextView) cardView.findViewById(R.id.speciality_card_doctor);
        speciality_doctor.setText(array_dorcors.get(position).speciality);

        TextView price_doctor = (TextView) cardView.findViewById(R.id.price_of_consultation_card_doctor);
        price_doctor.setText(array_dorcors.get(position).price_of_consultation);

        TextView service_doctor = (TextView) cardView.findViewById(R.id.services_card_doctor);
        service_doctor.setText(array_dorcors.get(position).service);

        TextView gps_doctor = (TextView) cardView.findViewById(R.id.gps_card_doctor);
        gps_doctor.setText(array_dorcors.get(position).gps);

        TextView review_doctor = (TextView) cardView.findViewById(R.id.review_card_doctor);
        review_doctor.setText(array_dorcors.get(position).review);

        Button btn_record_doctor = (Button) cardView.findViewById(R.id.btn_card_doctor);
        btn_record_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickRecord(position);
                }
            }
        });

    }

    @Override
    public int getItemCount(){
        return array_dorcors.size();
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

}
