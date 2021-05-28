package Client.Model;

import Client.Network.ClientInterface;
import Client.Network.ClientRMI;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class UserModelManager implements User, Serializable
{
  private ClientInterface clientInterface;
  private ArrayList<User> simpleUsers;
  public UserModelManager(ClientInterface clientInterface)
      throws RemoteException, NotBoundException, InterruptedException
  {
    this.clientInterface = clientInterface;
    clientInterface = new ClientRMI();
    clientInterface.startClient();
    simpleUsers = new ArrayList<>();
  }




  @Override public ArrayList<User> logIn()
  {
    try
    {
     simpleUsers = clientInterface.logIn();
    }
    catch (RemoteException | NotBoundException e)
    {
      e.printStackTrace();
    }
      return simpleUsers;
    }



  @Override public void createAccount(String username, String password, String email)
      throws RemoteException, NotBoundException
  {
    User user = new SimpleUser();
    user.set(username, password, email);
    System.out.println("UMM");

    clientInterface.newUser(user);

  }







  @Override public String getUserName() throws RemoteException
  {
    return null;
  }

  @Override public String getPassword() throws RemoteException
  {
    return null;
  }

  @Override public String getEmail() throws RemoteException
  {
    return null;
  }

  @Override public void set(String username, String password, String email)
      throws RemoteException
  {

  }

  @Override public String getRole(String username)
      throws RemoteException, NotBoundException
  {
    return clientInterface.getRole(username);
  }

  @Override public void chooseThreeGenresForUser(String username, String firstGenre, String secondGenre, String thirdGenre)
      throws RemoteException
  {
    clientInterface.chooseThreeGenresForUser(username, firstGenre,secondGenre, thirdGenre);
  }

  @Override public void addPropertyChangeListener(String name,
      PropertyChangeListener listener) throws RemoteException
  {

  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener) throws RemoteException
  {

  }

  @Override public void removePropertyChangeListener(String name,
      PropertyChangeListener listener) throws RemoteException
  {

  }

  @Override public void removePropertyChangeListener(
      PropertyChangeListener listener) throws RemoteException
  {

  }

}
