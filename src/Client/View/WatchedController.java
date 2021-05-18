package Client.View;

import Client.ViewModel.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class WatchedController {

    private ViewModelFactory viewModelFactory;
    private Stage stage;
    private Scene watchLaterScene;
    private Scene homepageScene;
    private Scene favoriteScene;

    public void init(ViewModelFactory viewModelFactory, Stage stage) throws IOException {
        this.viewModelFactory = viewModelFactory;
        this.stage = stage;
        watchLaterScene();
        homepageScene();
        favoriteScene();
    }


    public void openManageWindow() throws IOException {
        Stage stage = new Stage();
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;

        loader.setLocation(getClass().getResource("MovieManager.fxml"));
        root = loader.load();
        MovieManagerController controller = loader.getController();
        controller.init(viewModelFactory, stage);
        stage.setTitle("Admin window");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void watchLaterScene() throws IOException {
        watchLaterScene = null;
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;

        loader.setLocation(getClass().getResource("WatchLater.fxml"));

        root = loader.load();

        UserManagerController controller = loader.getController();
        controller.init(viewModelFactory, stage);
        watchLaterScene = new Scene(root);
    }

    private void homepageScene() throws IOException {
        homepageScene = null;
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;

        loader.setLocation(getClass().getResource("Homepage.fxml"));

        root = loader.load();

        UserManagerController controller = loader.getController();
        controller.init(viewModelFactory, stage);
        homepageScene = new Scene(root);
    }

    private void favoriteScene() throws IOException {
        favoriteScene = null;
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;

        loader.setLocation(getClass().getResource("Favorite.fxml"));

        root = loader.load();

        UserManagerController controller = loader.getController();
        controller.init(viewModelFactory, stage);
        favoriteScene = new Scene(root);
    }

    public void setSceneToWatchLater()
    {
        stage.setScene(watchLaterScene);
    }
    public void setSceneToHomepage()
    {
        stage.setScene(homepageScene);
    }
    public void setSceneToFavorite()
    {
        stage.setScene(favoriteScene);
    }
}