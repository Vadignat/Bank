package ru.vadignat.ui;

import ru.vadignat.data.User;
import ru.vadignat.net.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Date;

public class RegWindow extends JFrame {
    private static int MIN_SZ = GroupLayout.PREFERRED_SIZE;
    private static int MAX_SZ = GroupLayout.DEFAULT_SIZE;
    private JLabel lblPhone;
    private JLabel lblLastName;
    private JLabel lblFirstName;
    private JLabel lblMiddleName;
    private JLabel lblBirth;
    private JLabel lblPassword;
    private JLabel lblPassword2;

    private JTextField tfPhone;
    private JTextField tfLastName;
    private JTextField tfFirstName;
    private JTextField tfMiddleName;
    private JTextField tfBirth;
    private JTextField tfPassword;
    private JTextField tfPassword2;

    private JButton btnReg;
    private JButton btnCancel;
    private Client client;


    public RegWindow(JFrame parent, Client client){
        this.client = client;
        setSize(600,450);
        GroupLayout gl = new GroupLayout(getContentPane());
        setLayout(gl);
        lblPhone = new JLabel("Номер телефона: ");
        lblLastName = new JLabel("Фамилия: ");
        lblFirstName = new JLabel("Имя: ");
        lblMiddleName = new JLabel("Отчество: ");
        lblBirth = new JLabel("Дата рождения: ");
        lblPassword = new JLabel("Пароль: ");
        lblPassword2 = new JLabel("Повтор пароля: ");
        tfPhone = new JTextField();
        tfLastName = new JTextField();
        tfFirstName = new JTextField();
        tfMiddleName = new JTextField();
        tfBirth = new JTextField();
        tfPassword = new JTextField();
        tfPassword2 = new JTextField();
        btnReg = new JButton("Зарегистрироваться");
        btnCancel = new JButton("Отмена");

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
                                                .addComponent(lblLastName, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(tfLastName,MAX_SZ, MAX_SZ, MAX_SZ)
                                )
                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addComponent(lblFirstName, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(tfFirstName,MAX_SZ, MAX_SZ, MAX_SZ)
                                )
                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addComponent(lblMiddleName, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(tfMiddleName,MAX_SZ, MAX_SZ, MAX_SZ)
                                )
                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addComponent(lblBirth, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(tfBirth,MAX_SZ, MAX_SZ, MAX_SZ)
                                )
                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addComponent(lblPassword, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(tfPassword,MAX_SZ, MAX_SZ, MAX_SZ)
                                )
                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addComponent(lblPassword2, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(tfPassword2,MAX_SZ, MAX_SZ, MAX_SZ)
                                )
                                .addGroup(
                                        gl.createSequentialGroup()
                                                .addGap(8,8,Integer.MAX_VALUE)
                                                .addComponent(btnReg, MIN_SZ, MIN_SZ, MIN_SZ)
                                                .addGap(8)
                                                .addComponent(btnCancel,MIN_SZ, MIN_SZ, MIN_SZ)
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
                                        .addComponent(lblLastName,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(tfLastName, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(4)
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(lblFirstName,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(tfFirstName, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(4)
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(lblMiddleName,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(tfMiddleName, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(4)
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(lblBirth,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(tfBirth, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(4)
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(lblPassword,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(tfPassword, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(4)
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(lblPassword2,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(tfPassword2, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(8)
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(btnReg,MIN_SZ, MIN_SZ, MIN_SZ)
                                        .addComponent(btnCancel, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(8,8,Integer.MAX_VALUE)
        );
        gl.linkSize(0, lblPhone, lblFirstName, lblLastName, lblMiddleName, lblBirth, lblPassword, lblPassword2);
        gl.linkSize(0,btnReg,btnCancel);
        btnReg.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = new User();
                user.setPhone(tfPhone.getText());
                user.setFirstName(tfFirstName.getText());
                user.setLastName(tfLastName.getText());
                user.setMiddleName(tfMiddleName.getText());
                user.setBirth(Date.valueOf(tfBirth.getText()));
                if(tfPassword.getText().equals(tfPassword2.getText()))
                {
                    user.setPassword(tfPassword.getText());
                    try {
                        client.regUser(user);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                else {
                    JOptionPane.showMessageDialog(RegWindow.this,"Пароли не совпадают(");
                }
            }
        });
        btnCancel.addActionListener((e)->{
            setVisible(false);
            parent.setVisible(true);
        });
    }
}
