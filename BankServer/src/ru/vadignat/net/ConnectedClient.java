package ru.vadignat.net;

import ru.vadignat.data.*;
import ru.vadignat.db.DBHelper;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectedClient {
    private static final ArrayList<ConnectedClient> clients = new ArrayList<>();
    private final Socket cs;
    private final NetIO nio;
    private String name = null;
    private static DBHelper dbh;

    static {
        try {
            dbh = new DBHelper("localhost",3306,"root","");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ConnectedClient(Socket client){
        cs = client;
        nio = new NetIO(cs);
        clients.add(this);
    }

    public void start(){
        new Thread(()->{
            try {
                nio.startReceiving(this::parse);
            } catch (IOException | ClassNotFoundException e) {
                clients.remove(this);
            }
        }).start();
    }
    public void send(int dataType, Object data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(dataType);
        oos.writeObject(data);
        byte[] bytes = baos.toByteArray();
        nio.sendData(bytes);
    }


    public Void parse(Integer type, Object data){

        switch (type)
        {
            case 1 -> {
                User u = (User) data;
                try {
                    dbh.addUser(u);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 2 ->{
                UserVerifier userVerifier = (UserVerifier) data;
                try {
                    User result = dbh.checkUser(userVerifier.getPhone(), userVerifier.getPassword());
                    try {
                        if (result != null) {
                            var products = dbh.getUserProducts(result.getPhone());
                            send(7, products);
                        }
                        send(2, result);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 3 ->{
                Transfer t = (Transfer) data;
                try {
                    dbh.doTransfer(t);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

            case 4 -> {
                try {
                    var products = dbh.getProducts();
                    send(4, products);
                } catch (SQLException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            case 5->
            {
                try {
                    String productName = (String) data;
                    Product product = dbh.getProduct(productName);
                    send(5, product);
                } catch (SQLException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 6 ->
            {
                UserProduct up = (UserProduct) data;
                try {
                    if(dbh.addProduct(up.getUser(), up.getProduct()))
                        send(6, true);
                    else
                        send(6, false);
                } catch (SQLException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 7 ->
            {
                try {
                    User u = (User) data;
                    var products = dbh.getUserProducts(u.getPhone());
                    send(7, products);
                } catch (SQLException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return null;
    }
}
