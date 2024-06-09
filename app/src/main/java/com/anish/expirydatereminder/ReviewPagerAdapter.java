package com.anish.expirydatereminder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewPagerAdapter extends RecyclerView.Adapter<ReviewPagerAdapter.ReviewViewHolder> {

    private final List<Review> reviews;

    public ReviewPagerAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.userName.setText(review.getUserName());
        holder.reviewText.setText(review.getReviewText());
        // Load user image using your preferred image loading library, e.g., Glide or Picasso
        // Glide.with(holder.userImage.getContext()).load(review.getUserImage()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        TextView reviewText;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            reviewText = itemView.findViewById(R.id.review_text);
        }
    }
}
