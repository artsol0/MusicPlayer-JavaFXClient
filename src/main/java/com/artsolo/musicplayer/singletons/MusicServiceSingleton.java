package com.artsolo.musicplayer.singletons;

import com.artsolo.musicplayer.services.MusicService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MusicServiceSingleton {
    private static MusicServiceSingleton instance = null;
    private MusicService musicService;
    private Registry registry;

    private MusicServiceSingleton() {
        try {
            registry = LocateRegistry.getRegistry("localhost", 5294);
            musicService = (MusicService) registry.lookup("MusicService");
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
