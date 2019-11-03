package com.company.base.accenture.movies.Interfaces;

public interface IContainUsers {
    public void registerUsers(String regName, String regLogin, String regPassword);
    public void registerUsers(String regName, String regLogin, String regPassword, String admin);
    public void loginOldUsers(String name, String login, String password);

}
