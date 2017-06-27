package by.lovata.a2doc.screenDoctor.aboutDoctor.screenReviews;


import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import by.lovata.a2doc.R;

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Reviews[] reviewses;
    private Context context;

    ReviewsAdapter(Context context,Reviews[] reviewses) {
        this.context = context;
        this.reviewses = reviewses;
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
        name.setText(reviewses[position].getName());

        TextView date = (TextView) cardView.findViewById(R.id.review_date);
        date.setText(reviewses[position].getDate());

        String discription_text = getDiscription(reviewses[position].getDiscription());
        TextView discription = (TextView) cardView.findViewById(R.id.review_discription);
        discription.setText(discription_text);

        if (reviewses[position].isRecommend()) {
            TextView recommend = (TextView) cardView.findViewById(R.id.review_recommend);
            recommend.setText(context.getString(R.string.review_recommend));

            ImageView recommend_img = (ImageView) cardView.findViewById(R.id.review_recommend_img);
            recommend_img.setImageResource(R.drawable.ic_thumb_up_24dp);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowReviewActivity.class);
                intent.putExtra(ShowReviewActivity.DISCRIBTION, reviewses[position].getDiscription());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return reviewses.length;
    }

    private String getDiscription(String full_discription) {
        int length = full_discription.length() > 150 ? 150 : full_discription.length();
        return full_discription.substring(0, length) + "...";
    }
}
