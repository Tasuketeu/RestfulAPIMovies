package com.company.base.accenture.movies.ObjModelClass;

import java.time.LocalDate;
import java.util.*;

public class Review {

    private String review,
            rating,
            login;
    private LocalDate date;
    private String reviewInfo;


    public Review(String review, String rating, String login, LocalDate date) {
        this.review = review;
        this.rating = rating;
        this.login = login;
        this.date = date;

        reviewInfo = String.format("%s %s %s %s", this.review, this.rating, this.login,
                this.date);
    }

    public String getReviewInfo() {
        return reviewInfo;
    }
}
