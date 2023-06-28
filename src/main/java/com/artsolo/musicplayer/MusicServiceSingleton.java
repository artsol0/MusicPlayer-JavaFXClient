package com.artsolo.musicplayer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MusicServiceSingleton {
    private static MusicServiceSingleton instance = null;
    private MusicService musicService;
    private Registry registry;

    private MusicServiceSingleton() {
        try {
            registry = LocateRegistry.getRegistry("localhost", 5294);
            musicService = (MusicService) registry.lookup("MusicPlayer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MusicServiceSingleton getInstance() {
        if (instance == null) {
            instance = new MusicServiceSingleton();
        }
        return instance;
    }

    public MusicService getMusicService() {
        return musicService;
    }
}
