package inventory.controller;

import inventory.model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends BaseController {

    @FXML
    private TableView partTable;

    @FXML
    private TableColumn partID;

    @FXML
    private TableColumn partName;

    @FXML
    private TableColumn partInventoryLevel;

    @FXML
    private TableColumn partPrice;

    public void handleSearchPartClick(ActionEvent event) throws IOException {
    }

    @FXML
    private void handleAddPartClick(ActionEvent event) throws IOException {
        PartController controller = (PartController) nextScene(event,"../view/part.fxml");
//        controller.setText("HELLO TRANSFERED NURSE");
    }

    @FXML
    public void handleModifyPartClick(ActionEvent event) throws IOException {
        PartController controller = (PartController) nextScene(event,"../view/part.fxml");

        Part part = (Part) partTable.getSelectionModel().getSelectedItem();

        if (part != null) {
            controller.loadPart(part.getPartID());
        }
    }

    @FXML
    public void handleDeletePartClick(ActionEvent event) throws IOException {
    }

    @FXML
    public void handleSearchProductClick(ActionEvent event) throws IOException {
    }

    @FXML
    public void handleAddProductClick(ActionEvent event) throws IOException {
        nextScene(event,"../view/product.fxml");
    }

    @FXML
    public void handleModifyProductClick(ActionEvent event) throws IOException {
    }

    @FXML
    public void handleDeleteProductClick(ActionEvent event) throws IOException {
    }

    @FXML
    public void handleExitClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        // Bind table columns to properties
        partID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("partID"));
        partName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("inStock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        // Bind the part table to the allParts observable list in Inventory
        partTable.setItems(getInventory().getAllParts());
    }
}
