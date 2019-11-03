package com.company.base.accenture.movies.BL;

import com.company.base.accenture.movies.Interfaces.IContainMovies;
import com.company.base.accenture.movies.Interfaces.IContainUsers;
import com.company.base.accenture.movies.Interfaces.UserAccessService;
import com.company.base.accenture.movies.ObjModelClass.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.sql.*;

@Component
@RestController
@RequestMapping("/user")
@Consumes("application/json")
@Produces("application/json")
public class UserServiceImpl implements IContainUsers {

    @Autowired
    private UserAccessService icu;

    //private Map<String, User> usersList = new LinkedHashMap<>();
    public static boolean inSystem = false;
    public static boolean adminMode = false;
    public static boolean exist;

    @Override
    public void registerUsers(String regName, String regLogin, String regPassword, String admin) {
        exist=false;
        icu.registerUsers(regName, regLogin, regPassword, admin);
        if(exist){
            System.out.println("Пользователь с таким логином уже существует!");
        }
        else {
            System.out.println("User added");
        }
    }

    @Override
    @POST
    @RequestMapping("/register")
    public void registerUsers(@QueryParam("regName") String regName, @QueryParam("regLogin") String regLogin, @QueryParam("regPassword") String regPassword) {
        System.out.println(regName);
        System.out.println(regLogin);
        System.out.println(regPassword);
        exist=false;
        icu.registerUsers(regName, regLogin, regPassword, "false");
        if(exist){
            System.out.println("Пользователь с таким логином уже существует!");
        }
        else {
            System.out.println("User added");
        }
    }

    @Override
    @GET
    @RequestMapping("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public void loginOldUsers(@QueryParam("name") String name, @QueryParam("login") String login,@QueryParam("password") String password) {

        icu.searchUser(name,login,password);
        if(inSystem){
                System.out.println("Вы вошли в систему!");
                MovieServiceImpl.setActiveUser(login);
        }
        else {
            System.out.println("Неверно введённые данные!");
        }
    }
}
