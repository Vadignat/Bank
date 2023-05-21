package ru.vadignat.ui;

import ru.vadignat.data.UserVerifier;
import ru.vadignat.net.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class AuthWindow extends JFrame {
    private static int MIN_SZ = GroupLayout.PREFERRED_SIZE;
    private static int MAX_SZ = GroupLayout.DEFAULT_SIZE;
    private JLabel lblPhone;
    private JLabel lblPassword;
    private JTextField tfPhone;
    private JTextField tfPassword;

    private JButton btnAuth;
    private JButton btnReg;
    private Client client;
    private GroupLayout authgl;
    public AuthWindow(Client client){
        this.client = client;
        client.setWindow(this);
        setSize(600,450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GroupLayout gl = new GroupLayout(getContentPane());
        setLayout(gl);
        lblPhone = new JLabel("Номер телефона: ");


        lblPassword = new JLabel("Пароль: ");

        tfPhone = new JTextField();

        tfPassword = new JTextField();

        btnAuth = new JButton("Войти");
        btnReg = new JButton("Регистрация");

        gl.setHorizontalGroup(
                gl.createSequentialGroup()
                        .addGap(8,8, Integer.MAX_VALUE)
                        .addGroup(gl.createParallelGroup()
                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addComponent(lblPhone, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(tfPhone,MAX_SZ, MAX_SZ, MAX_SZ)
                                )

                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addComponent(lblPassword, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(tfPassword,MAX_SZ, MAX_SZ, MAX_SZ)
                                )
                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addGap(8,8,Integer.MAX_VALUE)
                                                .addComponent(btnAuth, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(btnReg, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8,8,Integer.MAX_VALUE)
                                )
                        )
                        .addGap(8,8, Integer.MAX_VALUE)
        );
        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGap(8,8,Integer.MAX_VALUE)
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(lblPhone,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(tfPhone, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(4)

                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(lblPassword,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(tfPassword, MIN_SZ, MIN_SZ, MIN_SZ)
                        )

                        .addGap(8)
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(btnReg,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(btnAuth, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(8,8,Integer.MAX_VALUE)
        );
        gl.linkSize(0, lblPhone, lblPassword);
        gl.linkSize(0, btnReg, btnAuth);

        btnReg.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var rw = new RegWindow(AuthWindow.this, client);
                rw.setVisible(true);
                setVisible(false);
            }
        });


        authgl = new GroupLayout(getContentPane());
        btnAuth.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phone = tfPhone.getText();
                String password = tfPassword.getText();
                UserVerifier userVerifier = new UserVerifier(phone, password);
                try {
                    client.checkUser(userVerifier);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        btnAuth.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnAuth.doClick();
                }
            }
        });

    }
    public GroupLayout getAuthgl(){
        return authgl;
    }
    public void changeLayout(GroupLayout gl){
        getContentPane().removeAll();
        getContentPane().setLayout(gl);
        repaint();
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this,msg);
    }
}
