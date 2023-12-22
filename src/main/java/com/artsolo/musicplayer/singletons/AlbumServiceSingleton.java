package com.artsolo.musicplayer.singletons;
import com.artsolo.musicplayer.services.AlbumService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AlbumServiceSingleton {
    private static AlbumServiceSingleton instance = null;
    private AlbumService albumService;

    private AlbumServiceSingleton() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 5294);
            albumService = (AlbumService) registry.lookup("AlbumService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AlbumServiceSingleton getInstance() {
        if (instance == null) {
            instance = new AlbumServiceSingleton();
        }
        return instance;
    }

    public AlbumService getAlbumService() {return albumService;}
}
