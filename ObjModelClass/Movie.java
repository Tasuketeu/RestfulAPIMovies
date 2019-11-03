package com.company.base.accenture.movies.ObjModelClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Comparable<Movie> {

    private String imdb,
            filmType,
            title,
            genre,
            date,
            rating,
            description,
            movieInfo,
            movieNotFullInfo,
            movieFullInfo;

    private String[] dateVars;

    private double ratingToDouble;

    private static List<Review> reviewsList = new ArrayList<>();

    public Movie(String imdb, String filmType, String title,
                 String genre, String date, String rating, String description) {
        this.imdb = imdb;
        this.filmType = filmType;
        this.title = title;
        this.genre = genre;
        this.date = date;
        this.rating = rating;
        this.description = description;
        dateVars = this.date.split(" ");
        ratingToDouble = Double.parseDouble(this.rating);

        movieInfo = String.format("%s %s %s %s %s %s %s", this.imdb, this.filmType, this.title, this.genre,
                this.date, this.rating, this.description);

        movieNotFullInfo = String.format("%s %s %s %s %s", this.filmType, this.title, this.genre,
                this.imdb, this.rating);

        movieFullInfo = String.format("%s %s", this.date, this.description);
    }

    @Override
    public int compareTo(Movie movie) {
        if (ratingToDouble < movie.ratingToDouble)
            return 1;
        else if (movie.ratingToDouble < ratingToDouble)
            return -1;
        return 0;
    }

    public void addReview(String review, String rating, String login, LocalDate date) {
        reviewsList.add(new Review(review, rating, login, date));
    }

    public void editReview(int i, String review, String rating, String login, LocalDate date) {
        reviewsList.set(i, new Review(review, rating, login, date));
    }

    public void deleteReview(int i) {
        reviewsList.remove(i);
    }


    public String getMovieInfo() {
        return movieInfo;
    }

    public String getNotFullInfo() {
        return movieNotFullInfo;
    }

    public String getFullInfo() {
        return movieFullInfo;
    }

    public List<Review> getReviewsList() {
        return reviewsList;
    }

    public String getYear() {
        return this.dateVars[0];
    }
}
