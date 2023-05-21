package ru.vadignat.net;

import java.io.*;
import java.net.Socket;
import java.util.function.BiFunction;
import java.util.function.Function;

public class NetIO {
    private Socket s;
    /**
     * Признак необходимости завершения работы
     */
    private boolean stop = false;

    /**
     * Создание класса обеспечивающего сетевое взаимодействие клиентов и сервера
     * @param s клиентский сокет
     */
    public NetIO(Socket s){
        this.s = s;
    }

    /**
     * Метод, запускающий прием сообщений с удаленной стороны
     * @param parser функция, используемая для разбора получаемых сообщений
     * @throws IOException
     */
    public void startReceiving(BiFunction<Integer, Object, Void> parser) throws IOException, ClassNotFoundException {
        stop = false;
        while (!stop){
            var ba = new byte[10240];
            //var bat = s.getInputStream().readNBytes(4);
            s.getInputStream().read(ba);
            var ois = new ObjectInputStream(new ByteArrayInputStream(ba));
            var type = ois.readInt();
            Object data = ois.readObject();
            parser.apply(type, data);
        }
    }

    /**
     * Метод выполняет отправку данных по сети
     * @param bytes передаваемые данные
     * @throws IOException
     */
//    public void sendData(String data) throws IOException {
//        var pw = new PrintWriter(s.getOutputStream());
//        pw.println(data);
//        pw.flush();
//    }
    public void sendData(byte[] bytes) throws IOException {
        var os = s.getOutputStream();
        os.write(bytes);
        os.flush();
    }

    /**
     * Метод выполняет остановку процесса получения данных с удаленной стороны
     */
    public void stopReceiving(){
        stop = true;
    }

}
