package by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.List;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenDoctor.DoctorActivity;
import by.lovata.a2doc.screenViewDoctor.SaveParameter;

/**
 * Created by user on 24.08.2017.
 */

public class OrganizationsAdapter extends RecyclerView.Adapter<OrganizationsAdapter.ViewHolder>  {
    public int selectedItem = -1;
    private Context mContext;
    private List<String> mItems;
    private SaveParameter saveParameter;

    public SaveParameter getSaveParameter() {
        return saveParameter;
    }

    public OrganizationsAdapter(Context mContext, List<String> mItems, int selectedItem,
                                SaveParameter saveParapmeter) {
        this.selectedItem = selectedItem;
        this.mContext = mContext;
        this.mItems = mItems;
        this.saveParameter = saveParapmeter;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.organization_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrganizationsAdapter.ViewHolder viewHolder, int position) {
        viewHolder.mRadio.setChecked(position == selectedItem);
        viewHolder.mRadio.setText(mItems.get(position).toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RadioButton mRadio;

        public ViewHolder(final View inflate) {
            super(inflate);
            mRadio = (RadioButton) inflate.findViewById(R.id.radio);
            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItem = getAdapterPosition();
                    saveParameter.getSelectDoctor().setId_organization(saveParameter.getSelectDoctor().
                            getDoctorInfo().getId_organization()[getAdapterPosition()]);
                    Log.e("mylog","posAdapter"+getAdapterPosition());
                    Log.e("mylog","posAdapter jhg"+ DoctorActivity.getPositionOrganization(saveParameter));
                    notifyItemRangeChanged(0, mItems.size());


                }
            };
            itemView.setOnClickListener(l);
            mRadio.setOnClickListener(l);
        }
    }
}
