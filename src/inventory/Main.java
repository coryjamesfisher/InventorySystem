package inventory;

import inventory.model.Inventory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Inventory inventory = new Inventory();

    /**
     * Start method required by JavaFX application
     *
     * @param primaryStage - Reference to the JavaFX primaryStage.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load the main view/controller.
        Parent root = FXMLLoader.load(getClass().getResource("view/main.fxml"));

        // Set the title of the window.
        primaryStage.setTitle("Inventory Management System");

        // Set a suitable width/height.
        primaryStage.setScene(new Scene(root, 990, 465));

        // Display the stage.
        primaryStage.show();
    }

    /**
     * Entry point of the application
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Static getter method for the global inventory object.
     *
     * @return Inventory object
     */
    public static Inventory getInventory() {
        return inventory;
    }
}
