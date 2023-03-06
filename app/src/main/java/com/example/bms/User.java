package com.example.bms;

import java.io.Serializable;

public class User implements Serializable {
    private String phone;
    private String role;

    private int pin;

    public User() {
    }

    public User(String phone, String role, int pin) {
        this.phone = phone;
        this.role = role;
        this.pin = pin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}
