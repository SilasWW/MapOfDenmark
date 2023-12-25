package Map_of_Denmark;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import Map_of_Denmark.controller.Controller;
import Map_of_Denmark.model.Model;
import Map_of_Denmark.view.FileSelect;
import Map_of_Denmark.view.View;

/**
 * The main class of the application.
 */
public class App extends Application {

    /**
     * The main method of the application.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application with JavaFX
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet.
     * Applications may create other stages, if needed, but they will not be
     * primary stages and will not be embedded in the browser.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        String filename = "data/empty.osm";
        var model = Model.load(filename);
        var view = new View(model, primaryStage);
        new Controller(model, view);

        Image taskbarIcon = new Image("file:data/images/Denmark.png");
        primaryStage.getIcons().add(taskbarIcon);

        FileSelect fileSelect = new FileSelect(view);
        fileSelect.show();
    }
}