package com.example.july30demo.login;

import java.util.Map;

public class LoginService {
    private static Map<String, String> users = new java.util.HashMap<>();



    public void login(String username, String cookie) {
        users.put(username, cookie);
    }

    public boolean isLogIn(String username, String cookie){
        if(users.containsKey(username)){
            if(users.get(username).equals(cookie)){
                return true;
            }
            return false;
        }else{
            return false;
        }
    }


}
