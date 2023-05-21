package ru.vadignat.db;

import com.lambdaworks.crypto.SCryptUtil;
import ru.vadignat.data.User;

import java.sql.*;

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

    public boolean checkUser(String phone, String password) throws SQLException {
        String sql = "SELECT `password` FROM `users` WHERE `phone` = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, phone);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return verifyPassword(password, rs.getString("password"));
        }
        return false;
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        return SCryptUtil.check(password, hashedPassword);
    }

}
