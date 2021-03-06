package Server.Network;

import Client.Model.Movie;
import Client.Model.SimpleUser;
import Client.Model.User;
import Client.Network.ClientInterface;
import Server.Database.DatabaseConnection;
import javafx.scene.image.Image;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *  A class used for initializing server connection
 */

public class ServerRMI implements ServerInterface{

    private ArrayList<ClientInterface> connectedUsers;
    private DatabaseConnection databaseConnection;


    public ServerRMI(String databasePassword) throws RemoteException
    {
        UnicastRemoteObject.exportObject( this, 0);
        connectedUsers = new ArrayList<>();
        databaseConnection = new DatabaseConnection(databasePassword);
    }

    @Override
    public void addBroadcast(ClientInterface clientReceiver) {
        connectedUsers.add(clientReceiver);
    }

    @Override
    public void newUser(SimpleUser user) throws RemoteException{
       databaseConnection.addUser(user);
        System.out.println("new user in server");
    }

    @Override public void start() throws RemoteException, AlreadyBoundException
    {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("Server", this);
        System.out.println("Server started!");

    }

    @Override public ArrayList<SimpleUser> logIn()
        throws RemoteException
    {      ResultSet rs = null;
            ArrayList<SimpleUser> simpleUsers = new ArrayList<>();

        rs=databaseConnection.logIn() ;
        try{
                    while(rs.next()){
                        String username1 = rs.getString("username");
                        String password1 = rs.getString("password");

                        SimpleUser temp = new SimpleUser(username1, password1, "");
                        simpleUsers.add( temp);
                    }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return simpleUsers;
    }

    public ArrayList<Movie> loadFavouriteList(String username){
        ResultSet rs = null;
        ResultSet rsGenres = null;
        ResultSet rsActors = null;
        ArrayList<Object> genresO = new ArrayList<>();
        ArrayList<String> actorsList = new ArrayList<>();
        ArrayList<Movie> favouriteListOfMovies = new ArrayList<Movie>();
        rs = databaseConnection.loadFavouriteMovies(username);
        try{
            while(rs.next()){
                String imageurl = rs.getString("imageurl");
                String title = rs.getString("title");
                String year = rs.getString("year");
                double averagerating = rs.getDouble("averagerating");
                String description = rs.getString("description");

                int id = rs.getInt("movieid");


                rsActors = databaseConnection.getActorsForMovie(id);
                while(rsActors.next()){
                    String actor = rsActors.getString("actor");
                    actorsList.add(actor);
                  System.out.println(actorsList);
                }
                rsGenres = databaseConnection.getGenresForMovie(id);
                    while(rsGenres.next()){
                        String genre = rsGenres.getString("genre");
                        genresO.add(genre);
                    }


                String[] actors = actorsList.toArray(new String[0]);

                Movie movie = new Movie(imageurl, title, year, genresO, description, actors);
                    favouriteListOfMovies.add(movie);

                System.out.println(genresO.size() + "genres inside database !!!!!!!!!!@");

                    genresO.clear();
                    actorsList.clear();
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
        return favouriteListOfMovies;

    }

    @Override public String getRole(String username) throws RemoteException
    {
        ResultSet rs= null;
        rs = databaseConnection.getRole(username);
        String role = "";
        try
        {
            while(rs.next())
            {
                role = rs.getString("role");
                System.out.println(role);

            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return role;


    }

    @Override public void removeGenre(String genreName) throws RemoteException
    {
        databaseConnection.removeGenre(genreName);
    }

    @Override public void chooseThreeGenresForUser(String username, String firstGenre, String secondGenre, String thirdGenre)
    {
        databaseConnection.chooseThreeGenresForUser(username, firstGenre, secondGenre, thirdGenre);
    }

    @Override
    public ArrayList<Movie> getMovies() throws RemoteException {
        ResultSet movieTable = databaseConnection.getMoviesFromDatabase();
        ResultSet actorTable = databaseConnection.getActorsFromDatabase();
        ResultSet genreRelationshipTable = databaseConnection.getGenresFromGenresRelationship();
        ArrayList<String> actorsTemp = new ArrayList<>();
        ArrayList<Object> genres = new ArrayList<>();
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            while (movieTable.next())
            {
                while (actorTable.next())
                {
                    if (movieTable.getString("movieID").equals(actorTable.getString("movieID")))
                    actorsTemp.add(actorTable.getString("actor"));
                }
                while (genreRelationshipTable.next())
                {
                    if (movieTable.getString("movieID").equals(genreRelationshipTable.getString("movieID")))
                    {
                        genres.add(genreRelationshipTable.getString("genre"));
                    }
                }
                String[] actors = actorsTemp.toArray(new String[0]);
                Movie movie = new Movie(movieTable.getString("imageURL"), movieTable.getString("title"), movieTable.getString("year"),
                      genres,movieTable.getString("description"), actors);
                movies.add(movie);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
return movies;
    }

    @Override public ArrayList<Movie> loadMoviesByChosenGenre(String username)
    {
        ResultSet movieTable = databaseConnection.loadMoviesByChosenGenre(username);
        ResultSet actorTable = databaseConnection.getActorsFromDatabase();
        ResultSet genreRelationshipTable = databaseConnection.getGenresFromGenresRelationship();
        ArrayList<String> actorsTemp = new ArrayList<>();
        ArrayList<Object> genres = new ArrayList<>();
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            while (movieTable.next())
            {
                while (actorTable.next())
                {
                    if (movieTable.getString("movieID").equals(actorTable.getString("movieID")))
                        actorsTemp.add(actorTable.getString("actor"));
                }
                while (genreRelationshipTable.next())
                {
                    if (movieTable.getString("movieID").equals(genreRelationshipTable.getString("movieID")))
                    {
                        genres.add(genreRelationshipTable.getString("genre"));
                    }
                }
                String[] actors = actorsTemp.toArray(new String[0]);
                Movie movie = new Movie(movieTable.getString("imageURL"), movieTable.getString("title"), movieTable.getString("year"),
                    genres,movieTable.getString("description"), actors);
                movie.setMovieID(Integer.parseInt(movieTable.getString("movieID")));
                movies.add(movie);
//                genres.clear();
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return movies;
    }

    @Override public ArrayList<String> getGenresForMovie(int id)
        throws RemoteException
    {
        String genre0 = "";
        ResultSet rs = null;
        ArrayList<String > genres = new ArrayList<String>();
        rs = databaseConnection.getGenresForMovie(id);

        try{
        while (rs.next()){
            genre0 = rs.getString("genre");
            genres.add(genre0);
        }
    }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        System.out.println(genres.size() + "!!!!!");
        return genres;
    }

    @Override
    public void addMovieToDatabase(Movie movie) throws RemoteException, SQLException {
        databaseConnection.addMovies(movie);
    }

    @Override
    public void addGenre(String genre) throws RemoteException {
        databaseConnection.addGenre(genre);
    }

    @Override
    public ArrayList<String> getGenresFromDatabase() throws RemoteException {
        ResultSet rs = null;
        rs = databaseConnection.getGenresFromDatabase();
                ArrayList<String> genres = new ArrayList<>();
        try
        {
            while(rs.next())
            {
                String genre = rs.getString("genre");
                genres.add(genre);

            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return genres;

    }
    @Override public ArrayList<String> getActorsForMovie(int id)
            throws RemoteException
    {
        String actor0 = "";
        ArrayList<String> actors = new ArrayList<String>();
        ResultSet rs = databaseConnection.getActorsForMovie(id);

        try{
            while (rs.next())
            {
                actor0 = rs.getString("actor");
                actors.add(actor0);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return actors;
    }

    @Override
    public ArrayList<Movie> sortMoviesByGenres(ArrayList<String> chosenGenres) throws RemoteException, SQLException {
        ResultSet moviesTable = databaseConnection.getSortedMoviesByGenres(chosenGenres);

        ArrayList<Movie> sortedMovies = new ArrayList<>();
        ArrayList<Object> movieGenres = new ArrayList<>();

        while (moviesTable.next())
        {
            ResultSet genres = databaseConnection.getGenresForMovie(moviesTable.getInt("movieID"));
            while (genres.next())
            {
                movieGenres.add(genres.getString("genre"));
            }
            ArrayList<String> movieActorsTemp = getActorsForMovie(moviesTable.getInt("movieID"));
            String[] movieActors = movieActorsTemp.toArray(new String[0]);
            sortedMovies.add(new Movie(moviesTable.getString("imageURL"), moviesTable.getString("title"), moviesTable.getString("year"),
                     movieGenres, moviesTable.getString("description"), movieActors));
            movieGenres.clear();
        }

        return sortedMovies;
    }


        @Override public void addToWathced(String title, String description,
        String username)
    {
        databaseConnection.addToWatched(title, description, username);
    }

    @Override public void addTofavorite(int id, String username)
        throws RemoteException
    {
        databaseConnection.addToFavorite(id, username);
        System.out.println("added to the fav list");
    }

    @Override public int getMovieid(String title, String description)
        throws RemoteException
    {
        ResultSet rs = null;
        try
        {
            rs = databaseConnection.getMovieId(title, description);
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        int id = 0;
        try
        {
            while (rs.next())
            {
                id = rs.getInt("movieid");
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return id;
    }

    @Override public void addToWatchlater(int id, String username)
    {
        databaseConnection.addToWatchLater(id, username);
    }

    @Override public ArrayList<Movie> loadWatchLater(String username)
        throws RemoteException
    {
        ResultSet rs = null;
        ResultSet rsGenres = null;
        ResultSet rsActors = null;
        ArrayList<Object> genresO = new ArrayList<>();
        ArrayList<String> actorsList = new ArrayList<>();
        ArrayList<Movie> WatchLaterListOfMovies = new ArrayList<Movie>();
        rs = databaseConnection.loadWatchLater(username);
        try{
            while(rs.next()){
                String imageurl = rs.getString("imageurl");
                String title = rs.getString("title");
                String year = rs.getString("year");
                double averagerating = rs.getDouble("averagerating");
                String description = rs.getString("description");

                int id = rs.getInt("movieid");


                rsActors = databaseConnection.getActorsForMovie(id);
                while(rsActors.next()){
                    String actor = rsActors.getString("actor");
                    actorsList.add(actor);
                    System.out.println(actorsList);
                }
                rsGenres = databaseConnection.getGenresForMovie(id);
                while(rsGenres.next()){
                    String genre = rsGenres.getString("genre");
                    genresO.add(genre);
                }


                String[] actors = actorsList.toArray(new String[0]);

                Movie movie = new Movie(imageurl, title, year, genresO, description, actors);
                WatchLaterListOfMovies.add(movie);

                System.out.println(genresO.size() + "genres inside database !!!!!!!!!!@");

                genresO.clear();
                actorsList.clear();
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
        return WatchLaterListOfMovies;

    }

    @Override public ArrayList<Movie> loadAlreadyWatched(String username)
        throws RemoteException
    {
        ResultSet rs = null;
        ResultSet rsGenres = null;
        ResultSet rsActors = null;
        ArrayList<Object> genresO = new ArrayList<>();
        ArrayList<String> actorsList = new ArrayList<>();
        ArrayList<Movie> alreadyWatchedListOfMovies = new ArrayList<Movie>();
        rs = databaseConnection.loadAlreadyWatchedMovies(username);
        try{
            while(rs.next()){
                String imageurl = rs.getString("imageurl");
                String title = rs.getString("title");
                String year = rs.getString("year");
                double averagerating = rs.getDouble("averagerating");
                String description = rs.getString("description");

                int id = rs.getInt("movieid");


                rsActors = databaseConnection.getActorsForMovie(id);
                while(rsActors.next()){
                    String actor = rsActors.getString("actor");
                    actorsList.add(actor);
                    System.out.println(actorsList);
                }
                rsGenres = databaseConnection.getGenresForMovie(id);
                while(rsGenres.next()){
                    String genre = rsGenres.getString("genre");
                    genresO.add(genre);
                }


                String[] actors = actorsList.toArray(new String[0]);

                Movie movie = new Movie(imageurl, title, year, genresO, description, actors);
                alreadyWatchedListOfMovies.add(movie);

                System.out.println(genresO.size() + "genres inside database !!!!!!!!!!@");

                genresO.clear();
                actorsList.clear();
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
        return alreadyWatchedListOfMovies;

    }

    @Override
    public ArrayList<Movie> searchByTitle(String titleText) throws SQLException, RemoteException {
        ResultSet movieTable = databaseConnection.SearchMovieByName(titleText);
        ArrayList<Movie> movies  = new ArrayList<>();
        ArrayList<Object> genres = new ArrayList<>();

        while (movieTable.next())
        {
            ResultSet genreTemp = databaseConnection.getGenresForMovie(movieTable.getInt("movieID"));
             while (genreTemp.next())
             {
                 genres.add(genreTemp.getString("genre"));
             }
            ArrayList<String> movieActorsTemp = getActorsForMovie(movieTable.getInt("movieID"));
            String[] movieActors = movieActorsTemp.toArray(new String[0]);
            movies.add(new Movie(movieTable.getString("imageURL"), movieTable.getString("title"), movieTable.getString("year"),
                    genres, movieTable.getString("description"), movieActors));
            genres.clear();
        }
        return movies;
    }
}
