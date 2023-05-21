package ru.vadignat.data;

import com.lambdaworks.crypto.SCryptUtil;

import java.io.Serializable;

public class UserVerifier implements Serializable {
    private String phone;
    private String password;

    public UserVerifier(String phone, String password){
        this.phone = phone;
        setPassword(password);
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
