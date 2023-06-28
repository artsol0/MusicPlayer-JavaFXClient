package com.artsolo.musicplayer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MusicService extends Remote {
    public boolean login(String loginData) throws RemoteException;
    public String registration(String registrationData) throws RemoteException;
    public String[][] getMusic() throws RemoteException;
    public String[][] getMusic(int genreId) throws RemoteException;
    public String[][] getMusic(String searchString) throws RemoteException;
    public String[][] likedSearch(String searchString) throws RemoteException;
    public byte[] getMusicData(String musicId) throws RemoteException;
    public String addToLiked(String musicId) throws RemoteException;
    public String removeFromLiked(String musicId) throws RemoteException;
    public boolean createNewAlbum(String newAlbumTitle) throws RemoteException;
    public String[][] getAlbum() throws RemoteException;
    public String addToAlbum(String albumId, String musicId) throws RemoteException;
    public String[][] getAlbumMusic(String albumId) throws RemoteException;
    public String removeFromAlbum(String albumId, String musicId) throws RemoteException;
    public boolean removeAlbum(String albumId) throws RemoteException;
}
