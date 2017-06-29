package by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import by.lovata.a2doc.R;
import by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor.Times;

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Reviews[] reviews;
    private Context context;

    ReviewsAdapter(Context context, Reviews[] review) {
        this.context = context;
        this.reviews = review;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_review, parent, false);
        return new ReviewsAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;

        TextView name = (TextView) cardView.findViewById(R.id.review_name);
        name.setText(reviews[position].getName());

        TextView date = (TextView) cardView.findViewById(R.id.review_date);
        date.setText(reviews[position].getDate());

        String discription_text = getDiscription(reviews[position].getDiscription(),
                reviews[position].isStatus());
        final TextView discription = (TextView) cardView.findViewById(R.id.review_discription);
        discription.setText(discription_text);

        if (reviews[position].isRecommend()) {
            TextView recommend = (TextView) cardView.findViewById(R.id.review_recommend);
            recommend.setText(context.getString(R.string.review_recommend));

            ImageView recommend_img = (ImageView) cardView.findViewById(R.id.review_recommend_img);
            recommend_img.setImageResource(R.drawable.ic_thumb_up_24dp);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status_opposite = !reviews[position].isStatus();
                reviews[position].setStatus(status_opposite);

                String discription_text = getDiscription(reviews[position].getDiscription(), status_opposite);
                discription.setText(discription_text);
            }
        });

    }

    @Override
    public int getItemCount() {
        return reviews.length;
    }

    private String getDiscription(String full_discription, boolean status) {
        String discription = null;
        int size = full_discription.length();

        if (status) {
            discription = full_discription;
        } else {
            int length = size > 150 ? 150 : size;
            discription = full_discription.substring(0, length) + (length == size ? "" : "...");
        }

        return discription;
    }
}
