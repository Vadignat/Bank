package ru.vadignat.net;

import ru.vadignat.data.User;

import java.io.*;
import java.net.Socket;

public class Client
{
    private String _host;
    private int _port;
    private Socket s;
    private NetIO nio;
    public Client(String host, int port) throws IOException {
        _host = host;
        _port = port;
        s = new Socket(_host, _port);
        nio = new NetIO(s);
    }

    public void startReceiving(){
        new Thread(()->{
            try {
                nio.startReceiving(this::parse);
            } catch (IOException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public Void parse(Integer type, Object data){
//        var data = d.split(":", 2);
//        Command cmd = null;
//        try {
//            cmd = Command.valueOf(data[0]);
//        } catch (Exception ignored){
//        }
//        switch (cmd) {
//            case INTRODUCE -> {
//                if (data.length > 1 && data[1].trim().length() > 0) {
//                    System.out.println(data[1]);
//                } else {
//                    System.out.println("Представьтесь, пожалуйста:");
//                }
//            }
//            case MESSAGE -> {
//                if (data.length > 1 && data[1].trim().length() > 0) {
//                    System.out.println(data[1]);
//                }
//            }
//            case LOGGED_IN -> {
//                if (data.length > 1 && data[1].trim().length() > 0) {
//                    System.out.println("Пользователь " + data[1] + " вошёл в чат");
//                }
//            }
//            case LOGGED_OUT -> {
//                if (data.length > 1 && data[1].trim().length() > 0) {
//                    System.out.println("Пользователь " + data[1] + " покинул чат");
//                }
//            }
//            case null -> {
//
//            }
//        }
        return null;
    }

//    public void send(String userData) {
//        try {
//            nio.sendData(userData);
//        } catch (IOException e) {
//            System.out.println("Ошибка: " + e);
//        }
//    }

    public void regUser(User user) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(baos);
        ous.writeInt(1);
        ous.writeObject(user);
        var ba = baos.toByteArray();
        nio.sendData(ba);
    }
}
