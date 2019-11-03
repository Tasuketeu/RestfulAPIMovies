package com.company.base.accenture.movies.Interfaces;

import com.company.base.accenture.movies.ObjModelClass.Review;

import java.util.List;

public interface IContainMovies {

    public boolean searchFilm(String search);
    public void getFilmInfo(String search);
    public void addReview(String imdb, String review, String rating);
    public void editReview(String imdb, String review, String rating,String login);
    public void deleteReview(String imdb, String login);
    public List<Review> getMyReviews();
}
