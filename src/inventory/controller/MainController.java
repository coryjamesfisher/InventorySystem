package inventory.controller;

import inventory.model.Part;
import inventory.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private TextField partSearchField;

    @FXML
    private TableView productTable;

    @FXML
    private TableColumn productID;

    @FXML
    private TableColumn productName;

    @FXML
    private TableColumn productInventoryLevel;

    @FXML
    private TableColumn productPrice;

    @FXML
    private TextField productSearchField;

    private ObservableList<Part> filteredParts = FXCollections.observableArrayList();

    private ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

    public void handleSearchPartClick(ActionEvent event) throws IOException {

        // Clear current filter.
        filteredParts.clear();

        // IF search field is empty just return all the results.
        if (partSearchField.getText().length() == 0) {
            filteredParts.addAll(getInventory().getAllParts());
            return;
        }

        try {

            // Try parsing the string as integer and look up the part.
            // On NumberFormatException we fall into the exception handler.
            Part part = getInventory().lookupPart(Integer.valueOf(partSearchField.getText()));

            // If part not found leave the filter as no parts.
            if (part == null) {
                return;
            }

            // Add part
            filteredParts.add(part);

        } catch (NumberFormatException e) {

            // Searching with a string is considered a "contains case insensitive" search on the name field only.
            List<Part> parts = getInventory().lookupPart(partSearchField.getText());
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
        // Clear current filter.
        filteredProducts.clear();

        // IF search field is empty just return all the results.
        if (partSearchField.getText().length() == 0) {
            filteredProducts.addAll(getInventory().getProducts());
            return;
        }

        try {

            // Try parsing the string as integer and look up the part.
            // On NumberFormatException we fall into the exception handler.
            Product product = getInventory().lookupProduct(Integer.valueOf(productSearchField.getText()));

            // If part not found leave the filter as no parts.
            if (product == null) {
                return;
            }

            // Add part
            filteredProducts.add(product);

        } catch (NumberFormatException e) {

            // Searching with a string is considered a "contains case insensitive" search on the name field only.
            List<Product> products = getInventory().lookupProduct(productSearchField.getText());
            filteredProducts.addAll(products);
        }
    }

    @FXML
    public void handleAddProductClick(ActionEvent event) throws IOException {
        nextScene(event,"../view/product.fxml");
    }

    @FXML
    public void handleModifyProductClick(ActionEvent event) throws IOException {
        ProductController controller = (ProductController) nextScene(event,"../view/product.fxml");

        Product product = (Product) productTable.getSelectionModel().getSelectedItem();

        if (product != null) {
            controller.loadProduct(product.getProductID());
        }
    }

    @FXML
    public void handleDeleteProductClick(ActionEvent event) throws IOException {
        Product product = (Product) productTable.getSelectionModel().getSelectedItem();

        if (product != null) {

            if (product.getAssociatedParts().size() > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You may not delete a product that has a part assigned to it.", ButtonType.CLOSE);
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete the selected product?",
                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                getInventory().removeProduct(product.getProductID());
            }
        }
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

        filteredProducts.addAll(getInventory().getProducts());

        // Bind table columns to properties
        productID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("productID"));
        productName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        productInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("inStock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        // Bind the part table to the allParts observable list in Inventory
        productTable.setItems(filteredProducts);
    }
}
