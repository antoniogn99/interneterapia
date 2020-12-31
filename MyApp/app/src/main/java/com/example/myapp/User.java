package com.example.myapp;

import android.widget.Toast;

public abstract class User {
    public static String username = "";
    public static String password = "";
    public static String email = "";
    public static String birthdate = "";
    public static String gender = "";

    public static void reset() {
        username = "";
        password = "";
        email = "";
        birthdate = "";
        gender = "";
    }
}
