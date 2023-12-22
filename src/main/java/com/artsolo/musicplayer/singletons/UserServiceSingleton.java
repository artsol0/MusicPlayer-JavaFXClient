package com.artsolo.musicplayer.singletons;

import com.artsolo.musicplayer.services.UserService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UserServiceSingleton {
    private static UserServiceSingleton instance = null;
    private UserService userService;

    private UserServiceSingleton() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 5294);
            userService = (UserService) registry.lookup("UserService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserServiceSingleton getInstance() {
        if (instance == null) {
            instance = new UserServiceSingleton();
        }
        return instance;
    }

    public UserService getAlbumService() {return userService;}
}
