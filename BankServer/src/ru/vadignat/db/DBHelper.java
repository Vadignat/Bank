package ru.vadignat.db;

import ru.vadignat.data.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private Connection connection;
    private String dbName = "db";
    public DBHelper(String host, int port, String user, String password) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ':' + port + "/" + dbName, user, password);
    }

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO `users` (phone, lastName, firstName, middleName, birth, password)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        connection.setAutoCommit(false);
        var stmt = connection.prepareStatement(sql);
        stmt.setString(1, user.getPhone());
        stmt.setString(2, user.getLastName());
        stmt.setString(3, user.getFirstName());
        stmt.setString(4, user.getMiddleName());
        stmt.setDate(5, user.getBirth());
        stmt.setString(6, user.getPassword());
        try {
            stmt.executeUpdate();
        }
        catch (Exception e){
            connection.rollback();
            throw new SQLException();
        }
        connection.commit();
    }
}
