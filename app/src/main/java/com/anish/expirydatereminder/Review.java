package com.anish.expirydatereminder;

public class Review {
    private final String userName;
    private final String reviewText;
    private final int userImage;

    public Review(String userName, String reviewText, int userImage) {
        this.userName = userName;
        this.reviewText = reviewText;
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public int getUserImage() {
        return userImage;
    }
}
