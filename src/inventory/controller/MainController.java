package inventory.controller;

import inventory.model.Part;
import inventory.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller that handles the main screen and related actions.
 */
public class MainController extends BaseController {

    /**
     * All member variables below are related to the UI for the main screen.
     * They are bound to the fxml elements in main.fxml
     */
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

    /**
     * An observable list of Parts filtered for the part search.
     * When a different search is performed the table in the UI follows this list.
     */
    private ObservableList<Part> filteredParts = FXCollections.observableArrayList();

    /**
     * An observable list of Products filtered for the part search.
     * When a different search is performed the table in the UI follows this list.
     */
    private ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

    /**
     * All of the following methods prefixed with handle are used for handling events from the UI.
     */

    /**
     * Handle the part search.
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleSearchPartClick(Event event) throws IOException {

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

    /**
     * Handle add part click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    private void handleAddPartClick(Event event) throws IOException {
        nextScene(event,"view/part.fxml");
    }

    /**
     * Handle modify part click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleModifyPartClick(Event event) throws IOException {

        Part part = (Part) partTable.getSelectionModel().getSelectedItem();

        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a part to modify.");
            alert.showAndWait();
            return;
        }

        PartController controller = (PartController) nextScene(event,"view/part.fxml");
        controller.loadPart(part.getPartID());
    }

    /**
     * Handle delete part click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleDeletePartClick(Event event) throws IOException {
        Part part = (Part) partTable.getSelectionModel().getSelectedItem();

        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select the part you wish to delete.", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }

        if (getInventory().getProductsContainingPart(part.getPartID()).size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You may not remove this part as it is still associated to one or more products.", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete the selected part?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            getInventory().deletePart(part.getPartID());
            filteredParts.remove(part);
        }
    }

    /**
     * Handle product search click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleSearchProductClick(Event event) throws IOException {
        // Clear current filter.
        filteredProducts.clear();

        // IF search field is empty just return all the results.
        if (productSearchField.getText().length() == 0) {
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

    /**
     * Handle add product click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleAddProductClick(Event event) throws IOException {
        nextScene(event,"view/product.fxml");
    }

    /**
     * Handle modify product click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleModifyProductClick(Event event) throws IOException {

        Product product = (Product) productTable.getSelectionModel().getSelectedItem();

        if (product == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a product to modify.");
            alert.showAndWait();
            return;
        }

        // Load the product controller
        ProductController controller = (ProductController) nextScene(event,"view/product.fxml");

        // Pass productID to the product controller
        controller.loadProduct(product.getProductID());
    }

    /**
     * Handle delete product click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleDeleteProductClick(Event event) throws IOException {

        // Get the selected product from the table
        Product product = (Product) productTable.getSelectionModel().getSelectedItem();

        if (product == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select the product you wish to delete.", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }

        // Must remove all parts prior to deleting a product
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
            filteredProducts.remove(product);
        }

    }

    /**
     * Handle exit button click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleExitClick(Event event) throws IOException {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        // Initialize the parts table with all parts from inventory
        filteredParts.addAll(getInventory().getAllParts());

        // Bind table columns to properties
        partID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("partID"));
        partName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("inStock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        // Bind the part table to the allParts observable list in Inventory
        partTable.setItems(filteredParts);

        // Initialize the products table with all products from inventory
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
