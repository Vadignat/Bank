package ru.vadignat.db;

import com.lambdaworks.crypto.SCryptUtil;
import ru.vadignat.data.Product;
import ru.vadignat.data.Transfer;
import ru.vadignat.data.User;
import ru.vadignat.data.UserProduct;

import java.sql.*;
import java.time.Period;
import java.util.ArrayList;

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

    public User checkUser(String phone, String password) throws SQLException {
        String sql = "SELECT * FROM `users` WHERE `phone` = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, phone);
        ResultSet rs = stmt.executeQuery();
        if (rs.next() && verifyPassword(password, rs.getString("password"))) {
            User u = new User();
            u.setPhone(phone);
            u.setLastName(rs.getString("lastName"));
            u.setFirstName(rs.getString("firstName"));
            u.setMiddleName(rs.getString("middleName"));
            u.setBirth(rs.getDate("birth"));
            u.setPasswordHash(rs.getString("password"));
            return u;
        }
        return null;
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        return SCryptUtil.check(password, hashedPassword);
    }

    public boolean doTransfer(Transfer t) throws SQLException{
        String sql = "SELECT `accId`, `type` FROM `accounts` WHERE `userId` = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, t.getPhone2());
        ResultSet rs = stmt.executeQuery();
        boolean isAccFound = false;
        while(rs.next()) {
            if(rs.getInt("type") == 1){
                if(t.getAccount2() == null)
                    t.setAccount2(rs.getString("accId"));
                isAccFound = true;
                break;
            }
        }

        if(isAccFound) {
            String sql3 = "SELECT `balance` FROM `accounts` WHERE `accId` = ?";
            PreparedStatement stmt3 = connection.prepareStatement(sql3);
            stmt3.setString(1, t.getAccount1());
            ResultSet rs3 = stmt3.executeQuery();

            if(rs3.next())
                if(rs3.getFloat("balance") < t.getSum())
                    return false;

            String sql1 = "UPDATE `accounts` SET `balance` = `balance` - ? WHERE `accId` = ?";
            String sql2 = "UPDATE `accounts` SET `balance` = `balance` + ? WHERE `accId` = ?";

            connection.setAutoCommit(false);

            var stmt1 = connection.prepareStatement(sql1);
            stmt1.setFloat(1, t.getSum() + t.getFee());
            stmt1.setString(2, t.getAccount1());
            var stmt2 = connection.prepareStatement(sql2);
            stmt2.setFloat(1, t.getSum());
            stmt2.setString(2, t.getAccount2());

            try {
                stmt1.executeUpdate();
                stmt2.executeUpdate();

            } catch (Exception e) {
                connection.rollback();
            }

            connection.commit();
            return true;
        }
        return false;

    }

    public ArrayList<Product> getProducts() throws SQLException {
        String sql = "SELECT `type`, `productName`, `info` FROM `products`";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<Product> products = new ArrayList<>();

        while (rs.next()) {
            Product product = new Product();
            product.setType(rs.getInt("type"));
            product.setProductName(rs.getString("productName"));
            product.setInfo(rs.getString("info"));
            products.add(product);
        }

        return products;
    }


    public Product getProduct(String productName) throws SQLException {
        String sql = "SELECT `type`, `info` FROM `products` WHERE `productName` = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, productName);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Product product = new Product();
            product.setProductName(productName);
            product.setType(rs.getInt("type"));
            product.setInfo(rs.getString("info"));
            return product;
        }
        return null;
    }

    public boolean addProduct(User user, Product product) throws SQLException{
        String sql = "INSERT INTO `accounts` (accId, userId, accName, balance, type)" +
                " VALUES (?, ?, ?, 0, ?)";
        connection.setAutoCommit(false);
        var stmt = connection.prepareStatement(sql);
        stmt.setString(1, createAccId(product.getType()));
        stmt.setString(2, user.getPhone());
        stmt.setString(3, product.getProductName());
        stmt.setInt(4, product.getType());
        try {
            stmt.executeUpdate();
        }
        catch (Exception e){
            connection.rollback();
            return false;
        }
        connection.commit();
        return true;
    }

    private String createAccId(int type) {
        if (type == 1) {
            return generateCardNumber();
        }
        return generateAccountNumber();
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder("4444 2222 3333 ");
        for (int i = 0; i < 4; i++) {
            int randomDigit = (int) (Math.random() * 10);
            cardNumber.append(randomDigit);
        }
        return cardNumber.toString();
    }

    private String generateAccountNumber() {
        StringBuilder accountNumber = new StringBuilder("4081781009991000");
        for (int i = 0; i < 4; i++) {
            int randomDigit = (int) (Math.random() * 10);
            accountNumber.append(randomDigit);
        }
        return accountNumber.toString();
    }

    public ArrayList<Product> getUserProducts(String userId) throws SQLException{
        String sql = "SELECT * FROM `accounts` WHERE `userId` = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, userId);
        ResultSet rs = stmt.executeQuery();
        ArrayList<Product> products = new ArrayList<>();

        while(rs.next()) {
            Product product = new Product();
            product.setProductName(rs.getString("accName"));
            product.setType(rs.getInt("type"));
            product.setBalance(rs.getFloat("balance"));
            product.setAccId(rs.getString("accId"));
            products.add(product);
        }
        return products;
    }

}
