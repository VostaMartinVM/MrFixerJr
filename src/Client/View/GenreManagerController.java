package Client.View;

import Client.ViewModel.GenreViewModel;
import Client.ViewModel.MovieViewModel;
import Client.ViewModel.ViewModelFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * A class used for giving functionality to GenreManager FXML
 */

public class GenreManagerController {

    @FXML
    private VBox genreList;

    private Button save;
    private HBox newRow;
    private Label introduceName;
    private TextField genreName;


    private ViewModelFactory viewModelFactory;
    private GenreViewModel genreViewModel;
    private ViewHandler viewHandler;
    private boolean genreExists = false;

    private ArrayList<HBox> rows = new ArrayList<>();

    public void init(ViewModelFactory viewModelFactory,ViewHandler viewHandler)
        throws IOException, NotBoundException
    {

        this.viewModelFactory = viewModelFactory;
        genreViewModel = viewModelFactory.getGenreViewModel();
        this.viewHandler = viewHandler;
        loadGenres();
    }

    /**
     * A method for creating a raw where you can write the name of the new genre
     * this method includes the functionality of deleting the genre by pressing the delete button
     */

    public void addGenre()
    {
        newRow = new HBox();

        introduceName = new Label("Genre name: ");

        genreName = new TextField();

        save = new Button("Save");
        Button delete = new Button("Delete");

        save.setOnAction(actionEvent -> saveGenre(delete));

        delete.setOnAction(actionEvent -> {

            genreList.getChildren().clear();
            try
            {
                genreViewModel.deleteGenreFromDataBase(genreName.getText());
                rows.remove(newRow);
                Platform.runLater(new Runnable()
                {
                    @Override public void run()
                    {
                        try
                        {
                            loadGenres();
                        }
                        catch (RemoteException e)
                        {
                            e.printStackTrace();
                        }
                        catch (NotBoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }

        });

        newRow.getChildren().addAll(introduceName, genreName, save, delete);
        newRow.setSpacing(10);
        genreList.getChildren().add(newRow);
    }
    public void saveGenre(Button delete)
    {
        if(!(genreViewModel.getGenre().contains(genreName.getText()))){
            try
            {
//                delete = new Button("Delete");
                genreViewModel.createGenre(genreName.getText());
                newRow.getChildren().clear();
                            Label savedGenre = new Label("Genre name: " + genreName.getText());
                            newRow.getChildren().addAll(savedGenre, delete);
                            rows.add(newRow);

            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
            catch (NotBoundException e)
            {
                e.printStackTrace();
            }
        }



    }

    public void setSceneToUser() throws IOException {
        viewHandler.openUserManager();
    }
    public void setSceneToMovie() throws IOException {
        viewHandler.openMovieManager();
    }

    /**
     * A method for getting already existing genres from the database
     */

    public void loadGenres() throws RemoteException, NotBoundException
    {
        ArrayList<String> genres = genreViewModel.getGenre();
        ArrayList<Button> deletes = new ArrayList<Button>();
        for(int i=0; i<genres.size(); i++){
            newRow = new HBox();

            Label savedGenre = new Label("Genre name: " + genres.get(i));
            String genre0 = genres.get(i);
            Button delete1 = new Button("Delete");
            deletes.add(delete1);
            deletes.get(i).setOnAction(actionEvent -> {
                newRow.getChildren().clear();
                try
                {
                    genreViewModel.deleteGenreFromDataBase(genre0);
                    viewHandler.openGenreManager();
                    rows.remove(newRow);
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                catch (NotBoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });
            newRow.getChildren().addAll(savedGenre, delete1);
            newRow.setSpacing(10);
            genreList.getChildren().add(newRow);
            rows.add(newRow);
        }
    }

    public void logOut() throws IOException
    {
        viewHandler.start();
    }

    public void back() throws IOException, NotBoundException {
        viewHandler.openHomePage();
    }
}
