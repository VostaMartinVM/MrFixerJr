package Client.View;

import Client.Model.Movie;
import Client.ViewModel.LoginViewModel;
import Client.ViewModel.ViewModelFactory;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A class used for giving functionality to Homepage FXML
 */

public class HomepageController {

    private ViewModelFactory viewModelFactory;
    private Scene watchLaterScene;
    private Scene watchedScene;
    private Scene favoriteScene;
    private ViewHandler viewHandler;
    @FXML private CheckComboBox genresToChose;
    @FXML private VBox movies;
    @FXML private TextField searchBar;
    @FXML private ImageView manageImage;


    public void init(ViewModelFactory viewModelFactory, ViewHandler viewHandler)
        throws IOException, NotBoundException
    {
        this.viewModelFactory = viewModelFactory;
        this.viewHandler = viewHandler;
        loadMovies();
      LoginViewModel loginViewModel = viewModelFactory.getLoginViewModel();
      String role = loginViewModel.getRole(loginViewModel.getUsername().getValue());
      switch(loginViewModel.logIn()){
        case "true":
          if(role.equals("Admin") || role.equals("Moderator")){
            manageImage.setVisible(true);
          }
          else if(role.equals("SimpleUser")){
            manageImage.setVisible(false);
          }
          break;
      }
      for(int i=0; i<viewModelFactory.getGenreViewModel().getGenre().size(); i++){
        genresToChose.getItems().add(viewModelFactory.getGenreViewModel().getGenre().get(i));
      }


    }
    public ImageView getManageImage(){
        return manageImage;
    }

    public void openManageWindow() throws IOException {
        viewHandler.openMovieManager();
    }


    public void setSceneToWatchLater() throws IOException {
        viewHandler.openWatchLater();
    }
    public void setSceneToWatched() throws IOException {
        viewHandler.openWatched();
    }
    public void setSceneToFavorite() throws IOException {
       viewHandler.openFavorite();
    }

    public void logOut() throws IOException
    {
      viewHandler.start();
    }


    /**
     * A method that loads movies from database to homepage by 3 favorite genres chosen by the user when creating an account
     */

  public void loadMovies() throws RemoteException
  {
    int count = 0;
    HBox newRow = new HBox();
    movies.getChildren().clear();


    String username = viewHandler.getUserName();
      ArrayList<Movie> movies = viewModelFactory.getMovieViewModel().loadMoviesByChosenGenre(username);

    for (int i = 0; i < movies.size(); i++) {
      Movie movie0 = movies.get(i);
      VBox movie = new VBox();
      movie.setPrefWidth(216);
      movie.setPrefHeight(143);
      movie.setPadding(new Insets(15));

      int currentMovie = i;
      System.out.println(movie0.getMovieID()+" !!!!!!!!!!@");
        movie.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    openMovie(currentMovie, movies);
                } catch (NotBoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

                ImageView image = new ImageView(movie0.getImageURL());
      image.setFitWidth(113);
      image.setFitHeight(150);
      Label title = new Label(movie0.getTitle());
      Label year = new Label(movie0.getYear());
      String genres = "";
      for (int j = 0; j < viewModelFactory.getMovieViewModel().getGenresForMovie(movie0.getMovieID()).size(); j++) {
        genres += viewModelFactory.getMovieViewModel().getGenresForMovie(movie0.getMovieID()).get(j);

        if (!(j == viewModelFactory.getMovieViewModel().getGenresForMovie(movie0.getMovieID()).size()-1))
        {
          genres += ",";
        }
      }
      Label genreLabel = new Label(genres);
      movie.getChildren().addAll(image, title, year, genreLabel);


      newRow.getChildren().add(movie);

      count++;
      if (count % 3 == 0)
      {
        this.movies.getChildren().add(newRow);
        newRow = new HBox();
      }
    }
    this.movies.getChildren().add(newRow);

  }

    /**
     * A method for opening full description of specific movie
     */

  public void openMovie(int currentMovie, ArrayList<Movie> movies) throws NotBoundException, IOException {
      viewHandler.openViewMovieDescription(currentMovie, movies);
  }
  public void sortByGenre() throws SQLException, RemoteException {
      movies.getChildren().clear();
        ArrayList<String> chosenGenres = new ArrayList<>();
      for (int i = 0; i < genresToChose.getItems().size(); i++) {
          if (genresToChose.getCheckModel().isChecked(i)) {
              chosenGenres.add((String) genresToChose.getItems().get(i));
          }
      }
      ArrayList<Movie> sortedMovies = viewModelFactory.getMovieViewModel().sortMoviesByGenres(chosenGenres);
      HBox newRow = new HBox();
      int count =0;
      for (int i = 0; i < sortedMovies.size(); i++) {
          Movie movie0 = sortedMovies.get(i);
          int currentMovie = i;

          VBox movie = new VBox();

          movie.setPrefWidth(216);
          movie.setPrefHeight(143);
          movie.setPadding(new Insets(15));

          movie.setOnMouseClicked(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent mouseEvent) {
                  try {
                      openMovie(currentMovie, sortedMovies);
                  } catch (NotBoundException e) {
                      e.printStackTrace();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          });

          ImageView image = new ImageView(movie0.getImageURL());
          image.setFitWidth(113);
          image.setFitHeight(150);
          Label title = new Label(movie0.getTitle());
          Label year = new Label(movie0.getYear());
          String genres = "";
          for (int j = 0; j < movie0.getGenres().size(); j++) {
              genres += movie0.getGenres().get(j);

              if (!(j == movie0.getGenres().size()-1))
              {
                  genres += ",";
              }
          }
          Label genreLabel = new Label(genres);
          movie.getChildren().addAll(image, title, year, genreLabel);

          newRow.getChildren().add(movie);

          count++;
          if (count % 3 == 0)
          {
              movies.getChildren().add(newRow);
              newRow = new HBox();
          }
      }

      movies.getChildren().add(newRow);
  }

    /**
     * A method used for showing all movies that contain the searched text in their title
     */

    public void searchByTitle() throws SQLException, RemoteException {
      ArrayList<Movie> movies = viewModelFactory.getMovieViewModel().searchByTitle(searchBar.getText());

      this.movies.getChildren().clear();
      HBox newRow = new HBox();
      int count =0;

      for (int i = 0; i < movies.size(); i++) {
          Movie movie0 = movies.get(i);
          int currentMovie = i;

          VBox movie = new VBox();

          movie.setPrefWidth(216);
          movie.setPrefHeight(143);
          movie.setPadding(new Insets(15));

          movie.setOnMouseClicked(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent mouseEvent) {
                  try {
                      openMovie(currentMovie, movies);
                  } catch (NotBoundException e) {
                      e.printStackTrace();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          });

          ImageView image = new ImageView(movie0.getImageURL());
          image.setFitWidth(113);
          image.setFitHeight(150);
          Label title = new Label(movie0.getTitle());
          Label year = new Label(movie0.getYear());
          String genres = "";
          for (int j = 0; j < movie0.getGenres().size(); j++) {
              genres += movie0.getGenres().get(j);

              if (!(j == movie0.getGenres().size()-1))
              {
                  genres += ",";
              }
          }
          Label genreLabel = new Label(genres);
          movie.getChildren().addAll(image, title, year, genreLabel);

          newRow.getChildren().add(movie);

          count++;
          if (count % 3 == 0)
          {
              this.movies.getChildren().add(newRow);
              newRow = new HBox();
          }
      }

      this.movies.getChildren().add(newRow);
  }
}
