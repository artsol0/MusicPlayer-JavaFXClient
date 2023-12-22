package com.artsolo.musicplayer.services;

import com.artsolo.musicplayer.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserService extends Remote {
    public User loginUser(String data) throws RemoteException;
    public String registerNewUser(String data) throws RemoteException;
}
