package inventory.controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseController {

    public BaseController nextScene(Event event, String fxml) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Button clicked = (Button) event.getSource();
        Stage stage = (Stage) clicked.getScene().getWindow();

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();

        return loader.getController();
    }
}
