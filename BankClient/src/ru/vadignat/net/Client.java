package ru.vadignat.net;

import ru.vadignat.data.Product;
import ru.vadignat.data.User;
import ru.vadignat.data.UserProduct;
import ru.vadignat.data.UserVerifier;
import ru.vadignat.ui.AuthWindow;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


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
            } catch (IOException | ClassNotFoundException e) {
                window.showMessage("Ошибка: " + e.getMessage());
            }
        }).start();
    }


    public Void parse(Integer type, Object data){
        switch (type) {
            case 2 ->{
                User user = (User) data;
                if(user != null)
                {
                    window.authorize(user);
                }
                else{
                    window.showMessage("Пользователя с таким номером телефона не существует или неправильно введен пароль");
                }
            }
            case 4 -> {
                ArrayList<Product> array = (ArrayList<Product>) data;
                window.createProducts(array);
            }
            case 5 -> {
                window.setChoosedProduct((Product) data);
                window.chooseProduct();
            }

            case 6 ->
            {
                if((boolean) data){
                    window.showMessage("Продукт успешно добавлен");
                }
                else{
                    window.showMessage("Произошла ошибка при добавлении. Попробуйте еще раз");
                }
            }

            case 7 ->
            {
                ArrayList<Product> array = (ArrayList<Product>) data;
                window.createUserProducts(array);
            }
        }
        return null;
    }
    public void setWindow(AuthWindow window){
        this.window = window;
    }
    public void sendData(int type, Serializable data) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(baos);
        ous.writeInt(type);
        ous.writeObject(data);
        var ba = baos.toByteArray();
        nio.sendData(ba);
    }
    public void regUser(User user) throws IOException {
        sendData(1, user);
    }

    public void checkUser(UserVerifier userVerifier) throws IOException {
        sendData(2, userVerifier);
    }

    public void getProduct(String productName) throws IOException{
        sendData(5, productName);
    }

    public void addProduct(User user, Product product) throws IOException{
        UserProduct data = new UserProduct();
        data.setUser(user);
        data.setProduct(product);
        sendData(6, data);
    }
    public void getUserProducts(User user) throws IOException{
        sendData(7, user);
    }
}
