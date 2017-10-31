package inventory.controller;

import inventory.Main;
import inventory.model.Inventory;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Base controller for which all other controllers extend.
 * This provides some very useful utility methods.
 */
public class BaseController implements Initializable {

    /**
     * Essentially our data store for all inventory.
     */
    private Inventory inventory;

    /**
     * A utility method for loading the next scene.
     * It provides a reference to the controller it loads so that other controllers may pass data.
     *
     * @param Event - the UI event that triggered this transition
     * @param fxml - the fxml file to load
     * @return BaseController - Reference to the controller that was loaded.
     * @throws IOException
     */
    public BaseController nextScene(Event event, String fxml) throws IOException{

        // Load the fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource(getBasePath() + fxml));

        // Get the button that was clicked to hook into the window/stage.
        Button clicked = (Button) event.getSource();
        Stage stage = (Stage) clicked.getScene().getWindow();

        // Load the new scene.
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();

        // Return a reference to the controller so the calling controller can pass data.
        return loader.getController();
    }

    private String getBasePath() {
        return "/inventory/";
    }

    /**
     * Helper method to navigate to the main screen.
     *
     * @param event - UI event that triggered the transition.
     * @throws IOException
     */
    @FXML
    public void goHome(Event event) throws IOException {
        nextScene(event, "view/main.fxml");
    }

    /**
     * Helper method to prompt for cancellation of the current activity and return to the main screen.
     *
     * @param event - UI event that triggered the transition.
     * @throws IOException
     */
    @FXML
    public void handleCancelClick(Event event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel and return to the home screen?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            goHome(event);
        }
    }

    /**
     * Getter for the Inventory object.
     *
     * @return Inventory - the inventory object.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Setter for the Inventory object.
     *
     * @param inventory - Inventory object to set.
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Store a reference to the main inventory so all controllers have access.
        setInventory(Main.getInventory());
    }
}
