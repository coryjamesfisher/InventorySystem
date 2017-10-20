package inventory.controller;

import inventory.Main;
import inventory.model.Inventory;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable {

    private Inventory inventory;

    public BaseController nextScene(Event event, String fxml) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Button clicked = (Button) event.getSource();
        Stage stage = (Stage) clicked.getScene().getWindow();

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();

        return loader.getController();
    }

    @FXML
    public void goHome(Event event) throws IOException {
        nextScene(event, "../view/main.fxml");
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInventory(Main.getInventory());
    }
}
