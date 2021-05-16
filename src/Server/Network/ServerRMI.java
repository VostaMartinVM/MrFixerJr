package Server.Network;

import Client.Model.User;
import Client.Network.ClientInterface;
import Server.Database.DatabaseConnection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerRMI implements ServerInterface{

    private ArrayList<ClientInterface> connectedUsers;
    private DatabaseConnection databaseConnection;

    public ServerRMI() throws RemoteException
    {
        UnicastRemoteObject.exportObject( this, 0);
        connectedUsers = new ArrayList<>();
        databaseConnection = new DatabaseConnection();
    }
    @Override
    public void addBroadcast(ClientInterface clientReceiver) {
        connectedUsers.add(clientReceiver);
    }

    @Override
    public void newUser(User user) {
       databaseConnection.addUser(user);
    }
}