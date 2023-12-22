package com.artsolo.musicplayer.services;

import com.artsolo.musicplayer.models.Music;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MusicService extends Remote {
    public List<Music> getMusic(int userId) throws RemoteException;
    public List<Music> getMusicByGenre(int genreId) throws RemoteException;
    public List<Music> getMusicByString(String searchString) throws RemoteException;
    public List<Music> likedSearch(String searchString) throws RemoteException;
    public byte[] getMusicInBytes(int musicId) throws RemoteException;
    public String addMusicToLiked(int musicId, int userId) throws RemoteException;
    public String removeMusicFromLiked(int musicId, int userId) throws RemoteException;
}
