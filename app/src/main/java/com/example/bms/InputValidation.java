package com.example.bms;

public class InputValidation {
    public static boolean emailValidate(String email) {
        if (!email.trim().contains("@") || !email.trim().endsWith(".com"))
            return false;
        return true;
    }

    public static boolean passwordValidate(String password){
        return password.trim().length() > 4;
    }

    public static boolean confirmPassword(String password, String cPassword){ return password.trim().equals(cPassword.trim()); }

    public static boolean phoneValidate(String phone) { return phone.trim().length() == 9; }
}
