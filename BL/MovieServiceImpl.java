package com.company.base.accenture.movies.BL;

import com.company.base.accenture.movies.Interfaces.IContainMovies;
import com.company.base.accenture.movies.Interfaces.MovieAccessService;
import com.company.base.accenture.movies.ObjModelClass.Movie;
import com.company.base.accenture.movies.ObjModelClass.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @RequestMapping("/movie/view/{fromYear}/{toYear}/{fromRating}/{toRating}")
    public void searchFilm(@QueryParam("search") String search,
                              @PathVariable(required = false) String fromYear,
                              @PathVariable(required = false) String toYear,
                              @PathVariable(required = false) String fromRating,
                              @PathVariable(required = false) String toRating
    ) {
        if(fromYear.equals(null)||toYear.equals(null)||fromRating.equals(null)||toRating.equals(null)) {
                searchFilm(search);
        }
        else if(!(fromYear.equals(null)&&toYear.equals(null)&&fromRating.equals(null)&&toRating.equals(null))){
            for (Movie movie : moviesList) {
                String rating = movie.getRating();
                String year = movie.getYear();
                if((Double.parseDouble(rating)>=Double.parseDouble(fromRating) && (Double.parseDouble(rating)<=Double.parseDouble(toRating)))
                        &&(Integer.parseInt(year)>=Integer.parseInt(fromYear) && Integer.parseInt(year)<=Integer.parseInt(toYear))
                ){
                    foundMovies.add(movie);
                }
            }
                for (Movie movie : foundMovies) {

                    System.out.println(movie.getNotFullInfo()); //film info

                    System.out.println("\n");
                }
            foundMovies.clear();
        }
    }

    public boolean searchFilm(String search) {

        searchResult = search;
        String imdb=null;

        ended=false;

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
    @RequestMapping("/movie/{id}")
    public void getFilmInfo(@PathVariable("id") String search) throws JSONException {
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
        }
        else {
            System.out.println("Фильм не найден!");
            foundMovies.clear();
        }
    }

    public static void setActiveUser(String activeUser) {
        MovieServiceImpl.activeUser = activeUser;
    }

    @Override
    @POST
    @RequestMapping("/movie/{id}/review")
    public void addReview(@PathVariable("id") String imdb,@QueryParam("review") String review, @QueryParam("rating") String rating) {

        wroteReview = false;
        LocalDate date = LocalDate.now();
        if(!UserServiceImpl.adminMode) {
            for (Movie movie : moviesList) {
                if (!movie.getReviewsList().isEmpty()) {
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
            }
            for (Movie movie : moviesList) {
                if (imdb.equals(movie.getMovieInfo().split(" ")[0])) {
                    if (!wroteReview) {
                        movie.addReview(review, rating, activeUser, date);
                        System.out.println("Обзор добавлен!");
                    }
                }
            }
        }
    }

    @Override
    @PUT
    @RequestMapping("/review")
    public void editReview(@QueryParam("imdb") String imdb, @QueryParam("review") String review, @QueryParam("rating") String rating,@QueryParam("login") String login) {//for user


        LocalDate date = LocalDate.now();

        if(!UserServiceImpl.adminMode){
            login=MovieServiceImpl.activeUser;
        }

        for (Movie movie : moviesList) {
            List<Review> reviewsList = movie.getReviewsList();
            for (Review revEntry : reviewsList) {
                String[] reviewInfo = revEntry.getReviewInfo().split(" ");
                if (login.equals(reviewInfo[2]) && imdb.equals(movie.getMovieInfo().split(" ")[0])) {
                    movie.editReview(reviewsList.lastIndexOf(revEntry), review, rating, activeUser, date);
                    return;
                }
            }
        }
    }

    @Override
    @DELETE
    @RequestMapping("/review/{id}")
    public void deleteReview(@PathVariable("id") String imdb,@QueryParam("login") String login) {

        if(!UserServiceImpl.adminMode){
            login=MovieServiceImpl.activeUser;
        }

        for (Movie movie : moviesList) {
            List<Review> reviewsList = movie.getReviewsList();
            for (Review revEntry : movie.getReviewsList()) {
                String[] reviewInfo = revEntry.getReviewInfo().split(" ");
                if (login.equals(reviewInfo[2]) && imdb.equals(movie.getMovieInfo().split(" ")[0])) {  // login     imdb
                    movie.deleteReview(reviewsList.lastIndexOf(revEntry));
                    return;
                }
            }
        }
    }

    @Override
    @POST
    @RequestMapping("/review/view")
    public List<String> getMyReviews(){
        List<String> reviewList = new ArrayList<>();
        for (Movie movie : moviesList) {
            for (Review rev : movie.getReviewsList()) {
                if (activeUser.equals(rev.getReviewInfo().split(" ")[2])) {
                    reviewList.add(movie.getMovieInfo().split(" ")[2]+" "+rev.getReviewInfo());
                }
            }
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

