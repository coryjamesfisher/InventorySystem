package inventory.controller;

import inventory.model.Part;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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

    @FXML
    private TextField searchField;

    private ObservableList<Part> filteredParts = FXCollections.observableArrayList();

    public void handleSearchPartClick(ActionEvent event) throws IOException {

        // Clear current filter.
        filteredParts.clear();

        // IF search field is empty just return all the results.
        if (searchField.getText().length() == 0) {
            filteredParts.addAll(getInventory().getAllParts());
            return;
        }

        try {

            // Try parsing the string as integer and look up the part.
            // On NumberFormatException we fall into the exception handler.
            Part part = getInventory().lookupPart(Integer.valueOf(searchField.getText()));

            // If part not found leave the filter as no parts.
            if (part == null) {
                return;
            }

            // Add part
            filteredParts.add(part);

        } catch (NumberFormatException e) {

            // Searching with a string is considered a "contains case insensitive" search on the name field only.
            List<Part> parts = getInventory().lookupPart(searchField.getText());
            filteredParts.addAll(parts);
        }
    }

    @FXML
    private void handleAddPartClick(ActionEvent event) throws IOException {
        nextScene(event,"../view/part.fxml");
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
        Part part = (Part) partTable.getSelectionModel().getSelectedItem();

        if (part != null) {
            getInventory().deletePart(part.getPartID());
        }
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

        filteredParts.addAll(getInventory().getAllParts());

        // Bind table columns to properties
        partID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("partID"));
        partName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("inStock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        // Bind the part table to the allParts observable list in Inventory
        partTable.setItems(filteredParts);
    }
}
