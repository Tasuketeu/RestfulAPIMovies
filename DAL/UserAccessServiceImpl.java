package com.company.base.accenture.movies.DAL;

import com.company.base.accenture.movies.BL.MovieServiceImpl;
import com.company.base.accenture.movies.BL.UserServiceImpl;
import com.company.base.accenture.movies.Interfaces.UserAccessService;
import com.company.base.accenture.movies.ObjModelClass.User;
import org.springframework.stereotype.Component;

import javax.ws.rs.QueryParam;
import java.sql.*;

@Component
public class UserAccessServiceImpl implements UserAccessService {

    private static Connection con = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet rs = null;
    private static int userId = 1;

    @Override
    public void registerUsers(String regName, String regLogin, String regPassword, String admin) {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/./test", "sa", "1");
            preparedStatement = con.prepareStatement("select LOGIN from USERS where LOGIN like ?");
            preparedStatement.setString(1, regLogin);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                if (rs.getString("LOGIN").equals(regLogin)) {
                    UserServiceImpl.exist=true;
                    return ;
                }
            }

            if (!UserServiceImpl.exist) {
                preparedStatement = con.prepareStatement("insert into users(ID,NAME,LOGIN,PASSWORD,ADMIN) values(?,?,?,?,?)");
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, regName);
                preparedStatement.setString(3, regLogin);
                preparedStatement.setString(4, regPassword);
                preparedStatement.setString(5, admin);
                userId++;
                preparedStatement.executeUpdate();
                //usersList.put(regLogin,new User(regName,regLogin,regPassword,admin));
                UserServiceImpl.exist=false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (preparedStatement != null) preparedStatement.close();
            }
            catch (Exception e) {
            }
            ;
            try {
                if (con != null) con.close();
            }
            catch (Exception e) {
            }
            ;
        }
    }

    @Override
    public void searchUser(String name, String login, String password) {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/./test", "sa", "1");
            preparedStatement = con.prepareStatement("select LOGIN,NAME,PASSWORD,ADMIN from USERS where LOGIN like ?");
            preparedStatement.setString(1, login);

            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                if (rs.getString("NAME").equals(name) &&
                        rs.getString("LOGIN").equals(login) &&
                        rs.getString("PASSWORD").equals(password)
                ) {
                    if (rs.getString("ADMIN").equals("true")) {
                        UserServiceImpl.adminMode = true;
                    }

                    UserServiceImpl.inSystem = true;
                    return ;
                }
            }
            UserServiceImpl.inSystem = false;
        }
        catch (
                SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (con != null) con.close();
            } catch (Exception e) {
            }
            ;
        }
    }

}
