package com.company.base.accenture.movies.Interfaces;

public interface UserAccessService {
    public void searchUser(String name, String login, String password);
    public void registerUsers(String regName, String regLogin, String regPassword, String admin);
}
