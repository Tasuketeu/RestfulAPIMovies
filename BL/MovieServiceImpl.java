package com.company.base.accenture.movies.BL;

import com.company.base.accenture.movies.Interfaces.IContainMovies;
import com.company.base.accenture.movies.Interfaces.MovieAccessService;
import com.company.base.accenture.movies.ObjModelClass.Movie;
import com.company.base.accenture.movies.ObjModelClass.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RestController
@RequestMapping("/movie")
@Consumes("application/json")
@Produces("application/json")
public class MovieServiceImpl implements Runnable, IContainMovies {

    @Autowired
    private MovieAccessService icm;

    private static List<Movie> moviesList = new ArrayList<>();
    static String searchResult = "";

    static Pattern pattern;
    static Matcher titleMatcher;
    static Matcher yearMatcher;
    static boolean wroteReview = false;
    static boolean ended = false;

    public static String activeUser = null;
    private static List<Movie> foundMovies = new ArrayList<>();

    public static void getMoviesFromCSV(String uri) throws java.io.IOException {
        List<String> lines = Files.readAllLines(Paths.get(uri), StandardCharsets.UTF_8);
        for (String line : lines) {
            String[] temp = line.split(";");
            moviesList.add(new Movie(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6]));
        }

        Collections.sort(moviesList);
    }

    @Override
    @POST
    @RequestMapping("/view")
    public boolean searchFilm(@QueryParam("search") String search) {
        searchResult = search;
        String imdb=null;

        pattern = Pattern.compile(searchResult.toLowerCase() + ".++"); //{search}.++  greedy matching

        new Thread(this).start();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        ended = true;

        for (Movie movie : moviesList) {
            String[] movieInfo = movie.getMovieInfo().split(" ");
            imdb = movieInfo[0];
            String title = movieInfo[2].toLowerCase();
            String year = movie.getYear();
            titleMatcher = pattern.matcher(title);
            yearMatcher = pattern.matcher(year);

            if (titleMatcher.matches() || yearMatcher.matches()
                    || searchResult.toLowerCase().equals(title) || searchResult.equals(year)) {
                foundMovies.add(movie);
            }
            if (searchResult.equals(imdb)) {
                foundMovies.add(movie);
                return true;
            }
        }
        if(!searchResult.equals(imdb)) {
            for (Movie movie : foundMovies) {

                System.out.println(movie.getNotFullInfo()); //film info

                System.out.println("\n");
            }
        }
        foundMovies.clear();
        return false;
    }

    @Override
    @GET
    @RequestMapping("/id")
    public void getFilmInfo(@QueryParam("id") String search) {
        if(searchFilm(search)){
            for (Movie movie : foundMovies) {

                String[] movieInfo = movie.getMovieInfo().split(" ");

                System.out.println(movie.getNotFullInfo()); //film info

                System.out.println("\n");


                if (searchResult.equals(movieInfo[0])) {

                    System.out.println(movie.getFullInfo()); //film full info

                    if (!movie.getReviewsList().isEmpty()) {
                        for (Review review : movie.getReviewsList()) {
                            String[] reviewInfo = review.getReviewInfo().split(" ");
                            System.out.println(reviewInfo[3]); //date

                            System.out.println(reviewInfo[2]); //login
                            System.out.println(reviewInfo[0]); //review

                            System.out.println(reviewInfo[1]); //rating

                            System.out.println("\n");
                        }
                    }
                }
            }
        } else {
            System.out.println("Фильм не найден!");
        }
        foundMovies.clear();
    }

    public static void setActiveUser(String activeUser) {
        MovieServiceImpl.activeUser = activeUser;
    }

    @Override
    @POST
    @RequestMapping("/id/review")
    public void addReview(@QueryParam("imdb") String imdb,@QueryParam("review") String review, @QueryParam("rating") String rating) {
        LocalDate date = LocalDate.now();

        for (Movie movie : moviesList) {
            if(!movie.getReviewsList().isEmpty()) {
                for (Review revEntry : movie.getReviewsList()) {
                    String[] reviewInfo = revEntry.getReviewInfo().split(" ");
                    if (imdb.equals(movie.getMovieInfo().split(" ")[0])) {
                        if (activeUser.equals(reviewInfo[2])) { //login
                            wroteReview = true;
                            System.out.println("Вы уже написали обзор!");
                            return;
                        }
                    }
                }
            }

            if (imdb.equals(movie.getMovieInfo().split(" ")[0])) {
                if (!wroteReview) {
                    movie.addReview(review, rating, activeUser, date);
                    System.out.println("Обзор добавлен!");
                }
            }
        }

        wroteReview = false;

    }

    @Override
    @PUT
    @RequestMapping("/review")
    public void editReview(@QueryParam("imdb") String imdb, @QueryParam("review") String review, @QueryParam("rating") String rating,@QueryParam("login") String login) {//for user

        System.out.println(imdb);
        System.out.println(review);
        System.out.println(rating);
        System.out.println(login);
        LocalDate date = LocalDate.now();

        if(!UserServiceImpl.adminMode){
            login=MovieServiceImpl.activeUser;
        }

        for (Movie movie : moviesList) {
            List<Review> reviewsList = movie.getReviewsList();
            for (Review revEntry : reviewsList) {
                String[] reviewInfo = revEntry.getReviewInfo().split(" ");
                if (login.equals(reviewInfo[3]) && imdb.equals(reviewInfo[1])) {
                    movie.editReview(reviewsList.lastIndexOf(revEntry), review, rating, activeUser, date);
                    return;
                }
            }
        }
    }

    @Override
    @DELETE
    @RequestMapping("/review/id")
    public void deleteReview(@QueryParam("imdb") String imdb,@QueryParam("login") String login) {

        if(!UserServiceImpl.adminMode){
            login=MovieServiceImpl.activeUser;
        }

        for (Movie movie : moviesList) {
            List<Review> reviewsList = movie.getReviewsList();
            for (Review revEntry : movie.getReviewsList()) {
                String[] reviewInfo = revEntry.getReviewInfo().split(" ");
                if (login.equals(reviewInfo[3]) && imdb.equals(reviewInfo[1])) {  // login     imdb
                    movie.deleteReview(reviewsList.lastIndexOf(revEntry));
                    return;
                }
            }
        }
    }

    @Override
    @POST
    @RequestMapping("/review/view")
    public List<Review> getMyReviews(){
        List<Review> reviewList = new ArrayList<>();
        for (Movie movie : moviesList) {
            reviewList.addAll(movie.getReviewsList());
        }
        return reviewList;
    }

    @Override
    public void run() {
        String temp = "";
        while (!ended) {
            temp += ".";
            if (temp.length() == 6) {
                temp = ".";
            }
            System.out.println("Пожалуйста, подождите, выполняется поиск" + temp);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

