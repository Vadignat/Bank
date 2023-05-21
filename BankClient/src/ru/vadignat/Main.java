package ru.vadignat;

import ru.vadignat.net.Client;
import ru.vadignat.ui.AuthWindow;
import ru.vadignat.ui.RegWindow;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client("localhost",5003);
            client.startReceiving();
            var w = new AuthWindow(client);
            w.setVisible(true);
        } catch (IOException e) {
            System.out.println("Ошибка");
        }


    }
}
