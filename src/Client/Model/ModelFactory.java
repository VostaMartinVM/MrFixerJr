package Client.Model;

import Client.Network.ClientFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ModelFactory
{
    private DataModel model;
    private User user;
    private ClientFactory cf;
    private MovieDataModel movie;
    private GenreDataModel genre;

    //    private ClientInterface clientInterface;
//
//    public ClientInterface getClientInterface(){
//        if(clientInterface==null){
//            clientInterface = new ClientRMI()
//        }
//    }

    public ModelFactory(ClientFactory cf){
        this.cf = cf;
    }

    public DataModel getDataModel()
    {
        if (model == null)
        {
            model = new DataModelManager();
        }
        return model;
    }
    public User getUser()
        throws RemoteException, NotBoundException, InterruptedException
    {
        if(user==null){
            user = new UserModelManager(cf.getClientInterface());
        }
        return user;
    }
    public MovieDataModel getMovieDataModel(){
        if (movie==null){
            movie = new MovieDataModelManager();
        }
        return movie;
    }

    public GenreDataModel getGenreDataModel() {
        if (genre == null)
        {
            genre = new GenreModelManager(cf.getClientInterface());
        }
        return genre;
    }
}
