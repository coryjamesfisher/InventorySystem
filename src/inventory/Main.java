package inventory;

import inventory.model.Inventory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Inventory inventory = new Inventory();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/main.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 990, 465));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Inventory getInventory() {
        return inventory;
    }
}
