package ru.vadignat.net;

import ru.vadignat.data.User;
import ru.vadignat.db.DBHelper;

import java.io.IOException;
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

    public void send(String userData) {
//        try {
//            if (name != null || cmd == Command.INTRODUCE) {
//                nio.sendData(cmd + ":" + userData);
//            }
//        } catch (IOException e) {
//            System.out.println("Ошибка (4): " + e);
//        }
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
        }
        return null;
    }
}
