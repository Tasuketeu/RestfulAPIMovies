package com.company.base.accenture.movies.ObjModelClass;

public class User {
    private String regName,
            regLogin,
            regPassword,
            admin,
            userInfo;

    public User(String regName, String regLogin, String regPassword, String admin) {
        this.regName = regName;
        this.regLogin = regLogin;
        this.regPassword = regPassword;
        this.admin = admin;
        userInfo = String.format("%s %s %s %s", regName, regLogin, regPassword, admin);
    }

    public String getUserInfo() {
        return userInfo;
    }

}
