package com.artsolo.musicplayer.services;

import com.artsolo.musicplayer.models.Album;
import com.artsolo.musicplayer.models.Music;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AlbumService extends Remote {
    public boolean createNewAlbum(int userId, String newAlbumTitle) throws RemoteException;
    public List<Album> getAlbum(int userId) throws RemoteException;
    public String addMusicToAlbum(int albumId, int musicId) throws RemoteException;
    public List<Music> getAlbumMusic(int albumId) throws RemoteException;
    public String removeMusicFromAlbum(int albumId, int musicId) throws RemoteException;
    public boolean removeAlbum(int albumId) throws RemoteException;
}
