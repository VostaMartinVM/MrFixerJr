package Client.View;

import Client.Model.Movie;
import Client.ViewModel.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;


import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * A class used for giving functionality to CreateMovie FXML
 */

public class CreateMovieController {
    @FXML
    private ImageView movieImage;
    @FXML
    private TextField title;
    @FXML
    private TextField year;
    @FXML
    private CheckComboBox genre;
    @FXML
    private TextArea description;
    @FXML
    private TextArea actors;


    private ViewModelFactory viewModelFactory;
    private ViewHandler viewHandler;
    private String url = "";

    public void init(ViewModelFactory viewModelFactory, ViewHandler viewHandler) throws NotBoundException, RemoteException {
        this.viewModelFactory = viewModelFactory;
        this.viewHandler = viewHandler;
        for (int i = 0; i < viewModelFactory.getMovieViewModel().getGenres().size(); i++) {
            genre.getItems().add(viewModelFactory.getMovieViewModel().getGenres().get(i));
        }
    }

    /**
     * A method for choosing an image from your local repository
     */

    public void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        File tmp = fileChooser.showOpenDialog(new Stage());
        Image image = new Image("file:\\" + tmp.getAbsolutePath());
        url = "file:\\" + tmp.getAbsolutePath();
        movieImage.setImage(image);

    }

    public void createMovie() throws NotBoundException, RemoteException, SQLException {
        ArrayList<Object> chosenGenres = new ArrayList<>();
        for (int i = 0; i < genre.getItems().size(); i++) {
            if (genre.getCheckModel().isChecked(i)) {
                chosenGenres.add(genre.getItems().get(i));
            }
        }

        ArrayList<String> urls = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();

        for (int i = 0; i < viewModelFactory.getMovieViewModel().getMovies().size(); i++) {
            Movie movie = viewModelFactory.getMovieViewModel().getMovies().get(i);
            urls.add(movie.getImageURL());
            titles.add(movie.getTitle());
        }
        if (!(url.equals(""))) {
            if (!(urls.contains(url) && titles.contains(title.getText()))) {
                viewModelFactory.getMovieViewModel().createMovie(url, title.getText(), year.getText(), chosenGenres, description.getText(), actors.getText());
                System.out.println(url);
            }
        }
    }

    public void backToMovieManager() throws IOException {
        viewHandler.openMovieManager();
    }

}
