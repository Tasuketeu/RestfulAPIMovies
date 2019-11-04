package com.company.base.accenture.movies;

import com.company.base.accenture.movies.BL.MovieServiceImpl;
import com.company.base.accenture.movies.BL.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication(scanBasePackages = "com.company.base.accenture.movies")
public class MoviesApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws IOException, JSONException {
        SpringApplication.run(MoviesApplication.class, args);
        //new Main().configure(new SpringApplicationBuilder(Main.class)).run(args);
        ApplicationContext context =
                new AnnotationConfigApplicationContext("com.company.base.accenture.movies");

        MovieServiceImpl containMovies= context.getBean(MovieServiceImpl.class);
        UserServiceImpl containUsers = context.getBean(UserServiceImpl.class);

        containUsers.registerUsers("admin", "admin", "admin", "true");

        MovieServiceImpl.getMoviesFromCSV("E:/movies.csv");

        String notInSystem = "Вы не в системе";
        System.out.println(notInSystem);
        Scanner sc = new Scanner(System.in);

        System.out.println("Введите команду\n" +
                "register -- регистрация\n" +
                "login -- логин\n" +
                "logout -- выход из аккаунта\n" +
                "search -- поиск фильма\n" +
                "addReview -- добавить отзыв\n" +
                "editReview -- редактировать отзыв(для пользователей и админов)\n" +
                "deleteReview -- удалить отзыв(для пользователей и админов)\n" +
                "exit -- выход из программы\n"+
                "myReviews -- получить мои ревью:");

        String commands = sc.next();
        String login1;


        while (!(commands.equals("exit"))) {
            if (UserServiceImpl.inSystem) {

                if (commands.equals("logout")) {
                    MovieServiceImpl.activeUser=null;
                    UserServiceImpl.inSystem = false;
                    if (UserServiceImpl.adminMode) {
                        UserServiceImpl.adminMode = false;
                    }
                    System.out.println("Вы не в системе");
                }

                if (commands.equals("search")) {
                    System.out.println("Поиск фильмов:");
                    containMovies.searchFilm(commands);
                    containMovies.getFilmInfo(sc.next());
                    System.out.println("Введите imdb идентификатор, чтобы получить детали фильма:");
                    commands = sc.next();
                    containMovies.getFilmInfo(commands);
                }

                if(!UserServiceImpl.adminMode) {
                    if (commands.equals("addReview")) {
                        System.out.println("Введите imdb фильма, на который хотите написать обзор:");
                        commands = sc.next();
                        String myReview;
                        String myRating;
                        if (containMovies.searchFilm(commands)) {
                            System.out.println("Фильм найден! Напишите отзыв:");
                            myReview = sc.next();
                            System.out.println("Дайте оценку:");
                            myRating = sc.next();
                            containMovies.addReview(commands, myReview, myRating);
                        } else {
                            System.out.println("Некорректный imdb!");
                        }
                    }

                    if (commands.equals("myReviews")) {
                        containMovies.getMyReviews();
                    }
                }

                if (commands.equals("editReview")) {
                    if (!UserServiceImpl.adminMode) {
                        System.out.println("Введите imdb фильма, на который вы написали отзыв и хотите отредактировать:");
                        commands = sc.next();
                        String myReview;
                        String myRating;
                        if (containMovies.searchFilm(commands)) {
                            System.out.println("Фильм найден! Отредактируйте отзыв:");
                            myReview = sc.next();
                            System.out.println("Дайте оценку:");
                            myRating = sc.next();
                            containMovies.editReview(commands, myReview, myRating,MovieServiceImpl.activeUser);
                            System.out.println("Обзор изменён!");
                        } else {
                            System.out.println("Некорректный imdb!");
                        }
                    } else {
                        System.out.println("Введите imdb фильма, для которого хотите отредактировать отзыв:");
                        commands = sc.next();
                        String myReview;
                        String myRating;
                        if (containMovies.searchFilm(commands)) {
                            System.out.println("Фильм найден! Выберите пользователя, чей отзыв хотите отредактировать:");
                            login1 = sc.next();
                            System.out.println("Отредактируйте отзыв:");
                            myReview = sc.next();
                            System.out.println("Дайте оценку:");
                            myRating = sc.next();
                            containMovies.editReview(commands, myReview, myRating, login1);
                            System.out.println("Обзор изменён!");
                        } else {
                            System.out.println("Некорректный imdb!");
                        }
                    }
                }

                if (commands.equals("deleteReview")) {
                    if (!UserServiceImpl.adminMode) {
                        System.out.println("Введите imdb фильма, на который вы написали отзыв и хотите удалить:");
                        commands = sc.next();
                        if (containMovies.searchFilm(commands)) {
                            containMovies.deleteReview(commands,MovieServiceImpl.activeUser);

                            System.out.println("Фильм найден!Обзор удалён!");
                        } else {
                            System.out.println("Некорректный imdb!");
                        }
                    } else {
                        System.out.println("Введите imdb фильма, для которого хотите удалить отзыв:");
                        commands = sc.next();
                        if (containMovies.searchFilm(commands)) {
                            System.out.println("Фильм найден! Выберите пользователя, чей отзыв хотите удалить:");
                            login1 = sc.next();
                            containMovies.deleteReview(commands, login1);
                            System.out.println("Фильм найден!Обзор удалён!");
                        } else {
                            System.out.println("Некорректный imdb!");
                        }
                    }
                }

            } else {

                if (commands.equals("register")) {


                    System.out.println("Зарегистрируйтесь:");
                    System.out.println("Введите имя:");
                    String regName = sc.next();
                    System.out.println("Введите логин:");
                    String regLogin = sc.next();
                    System.out.println("Введите пароль:");
                    String regPassword = sc.next();
                    containUsers.registerUsers(regName, regLogin, regPassword, "false");


                }

                if (commands.equals("login")) {
                    System.out.println("Залогиньтесь:");
                    System.out.println("Введите имя:");
                    String name = sc.next();
                    System.out.println("Введите логин:");
                    String login = sc.next();
                    System.out.println("Введите пароль:");
                    String password = sc.next();
                    containUsers.loginOldUsers(name, login, password);
                }

            }
            commands = sc.next();
        }

    }

}
