package ru.vadignat.net;

import ru.vadignat.data.User;
import ru.vadignat.data.UserVerifier;
import ru.vadignat.ui.AuthWindow;

import java.io.*;
import java.net.Socket;


public class Client
{
    private String _host;
    private int _port;
    private Socket s;
    private NetIO nio;
    private AuthWindow window;
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
                window.showMessage("Ошибка: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                window.showMessage(e.getMessage());
            }
        }).start();
    }


    public Void parse(Integer type, Object data){
        switch (type) {
            case 1 ->{
                if((boolean) data)
                    window.authorize();
                else{
                    window.showMessage("Пользователя с таким номером телефона не существует или неправильно введен пароль");
                }
            }
        }
        return null;
    }

    public void regUser(User user) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(baos);
        ous.writeInt(1);
        ous.writeObject(user);
        var ba = baos.toByteArray();
        nio.sendData(ba);
    }

    public void checkUser(UserVerifier userVerifier) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(baos);
        ous.writeInt(2);
        ous.writeObject(userVerifier);
        var ba = baos.toByteArray();
        nio.sendData(ba);
    }

    public void setWindow(AuthWindow window){
        this.window = window;
    }


}
