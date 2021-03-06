package Client.View;

import Client.ViewModel.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;

/**
 * A class used for giving functionality to UserMangaer FXML
 */

public class UserManagerController {

    private ViewModelFactory viewModelFactory;
    private Scene movieScene;
    private Scene genreScene;
    private ViewHandler viewHandler;

    public void init(ViewModelFactory viewModelFactory, ViewHandler viewHandler) throws IOException {
        this.viewModelFactory = viewModelFactory;
        this.viewHandler = viewHandler;

    }


    public void setSceneToMovie() throws IOException {
        viewHandler.openMovieManager();
    }
    public void setSceneToGenre() throws IOException, NotBoundException
    {
        viewHandler.openGenreManager();
    }

    public void logOut() throws IOException
    {
        viewHandler.start();
    }

    public void back() throws IOException, NotBoundException {
        viewHandler.openHomePage();
    }
}
