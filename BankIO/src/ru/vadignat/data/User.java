package ru.vadignat.data;

import com.lambdaworks.crypto.SCryptUtil;
import java.io.Serializable;
import java.sql.Date;


public class User implements Serializable {
    private String phone;
    private String lastName;
    private String firstName;
    private String middleName;
    private Date birth;
    private String password;



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        var hash = SCryptUtil.scrypt(password,512,128,16);
        this.password = hash;
    }
    public void setPasswordHash(String hash)
    {
        this.password = hash;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    @Override
    public String toString()
    {
        return phone + " ("+ lastName +")";
    }
}
