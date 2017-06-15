package by.lovata.a2doc.doctorsListScreen;


import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import by.lovata.a2doc.MainActivity;
import by.lovata.a2doc.R;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    ArrayList<DoctorInfo> array_dorcors;

    DoctorsAdapter(ArrayList<DoctorInfo> doctors) {
        this.array_dorcors = doctors;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public DoctorsAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_doctor, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        CardView cardView = holder.cardView;
        TextView imageView = (TextView) cardView.findViewById(R.id.fio_card_doctor);
        imageView.setText(array_dorcors.get(position).full_name);
    }

    @Override
    public int getItemCount(){
        return array_dorcors.size();
    }
}
