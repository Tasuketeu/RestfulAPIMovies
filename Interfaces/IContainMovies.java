package com.company.base.accenture.movies.Interfaces;

import com.company.base.accenture.movies.ObjModelClass.Review;
import org.springframework.boot.configurationprocessor.json.JSONException;

import javax.ws.rs.core.Response;
import java.util.List;

public interface IContainMovies {

    public void searchFilm(String search,String fromYear,
                              String toYear,
                              String fromRating,
                              String toRating);

    public boolean searchFilm(String search);
    public void getFilmInfo(String search) throws JSONException;
    public void addReview(String imdb, String review, String rating);
    public void editReview(String imdb, String review, String rating,String login);
    public void deleteReview(String imdb, String login);
    public List<String> getMyReviews();
}
