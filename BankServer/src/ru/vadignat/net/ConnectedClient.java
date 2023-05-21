package ru.vadignat.net;

import ru.vadignat.data.User;
import ru.vadignat.data.UserVerifier;
import ru.vadignat.db.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

    /*public void send(String userData) {
        try {
            byte[] dataBytes = userData.getBytes();
            nio.sendData(dataBytes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
     */
    public void send(int dataType, Object data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(dataType);
        oos.writeObject(data);
        byte[] bytes = baos.toByteArray();
        nio.sendData(bytes);
    }


    public Void parse(Integer type,Object data){

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
                    boolean result = false;
                    if(dbh.checkUser(userVerifier.getPhone(), userVerifier.getPassword()))
                        result = true;
                    try {
                        send(1, result);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return null;
    }
}
