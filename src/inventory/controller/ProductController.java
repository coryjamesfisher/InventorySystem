package inventory.controller;

import inventory.model.Part;
import inventory.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductController extends BaseController {


    /**
     * All member variables below are related to the UI for the add/modify product screen.
     * They are bound to the fxml elements in product.fxml
     */
    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField inv;

    @FXML
    private TextField price;

    @FXML
    private TextField max;

    @FXML
    private TextField min;

    @FXML
    private TextField searchField;

    @FXML
    private TableView partSearchTable;

    @FXML
    private TableColumn searchPartID;

    @FXML
    private TableColumn searchPartName;

    @FXML
    private TableColumn searchPartInventoryLevel;

    @FXML
    private TableColumn searchPartPrice;

    @FXML
    private TableView productPartsTable;

    @FXML
    private TableColumn productPartID;

    @FXML
    private TableColumn productPartName;

    @FXML
    private TableColumn productPartInventoryLevel;

    @FXML
    private TableColumn productPartPrice;

    /**
     * An observable list of parts that were added to the current product.
     * When a part is added or removed, the table in the UI follows this list.
     */
    private ObservableList<Part> productParts = FXCollections.observableArrayList();

    /**
     * An observable list of parts filtered for the part search.
     * When a different search is performed the table in the UI follows this list.
     */
    private ObservableList<Part> filteredParts;

    /**
     * This method will load values from a product into the form elements.
     * It is used when a user clicks "Modify" product from the main screen.
     * @param productID - productID that should be loaded.
     */
    public void loadProduct(int productID) {

        // Lookup the product
        Product product = getInventory().lookupProduct(productID);

        // Set values from the product into the form elements
        id.setText(String.valueOf(product.getProductID()));
        name.setText(product.getName());
        inv.setText(String.valueOf(product.getInStock()));
        price.setText(String.valueOf(product.getPrice()));

        if (product.getMin() != -1) {
            min.setText(String.valueOf(product.getMin()));
        }

        if (product.getMax() != -1) {
            max.setText(String.valueOf(product.getMax()));
        }

        // Add associated parts to the observable parts list
        productParts.addAll(product.getAssociatedParts());
    }

    /**
     * Handle save button click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleSaveClick(Event event) throws IOException {

        // Parse the product form into a Product object
        Product product = parseForm();

        // Validate the product and return any errors.
        List<String> errors = validateProduct(product);

        // If the product is invalid display messages.
        if (errors.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, String.join("\n", errors), ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }

        // If there is a productID this is an update.
        if (product.getProductID() != 0) {
            getInventory().updateProduct(product.getProductID(), product);
        } else {

            // No productID, add the product.
            getInventory().addProduct(product);
        }

        goHome(event);
    }

    /**
     * This method will validate the product and return any error messages.
     *
     * @param product - The product to validate
     * @return List<String> of error messages
     */
    private List<String> validateProduct(Product product) {

        List<String> errors = new ArrayList<>();

        if (product.getMax() == -2) {
            errors.add("Maximum allowed is optional but when provided must be a positive integer.");
        }

        if (product.getMin() == -2) {
            errors.add("Minimum allowed is optional but when provided must be an integer >= 0.");
        }

        if (product.getMin() > product.getMax()) {
            errors.add("Minimum allowed must be less than maximum allowed.");
        }

        if (product.getMin() >= 0 && product.getInStock() < product.getMin()) {
            errors.add("Inventory must be greater than the minimum.");
        }

        if (product.getMax() > 0 && product.getInStock() > product.getMax()) {
            errors.add("Inventory must be less than the maximum.");
        }

        if (product.getAssociatedParts().size() == 0) {
            errors.add("Must have one or more parts associated to the product.");
        }

        if (product.getName() == null || product.getName().length() == 0) {
            errors.add("Name is a required field.");
        }

        if (product.getPrice() == -1) {
            errors.add("Price is a required field.");
        } else {

            // Make sure that the product costs at least as much as the sum of the parts' cost.
            Double totalCost = 0.0;
            for (Part part : product.getAssociatedParts()) {
                totalCost += part.getPrice();
            }

            if (product.getPrice() < totalCost) {
                errors.add("Price of the product can not be less than the sum of part costs.");
            }
        }

        return errors;
    }

    /**
     * Handle delete part click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleDeletePartClick(Event event) throws IOException {
        Part part = (Part) productPartsTable.getSelectionModel().getSelectedItem();

        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a part to disassociate from the product.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to disassociate this part from the product.", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            productParts.remove(part);
        }
    }

    /**
     * Handle add part click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleAddPartClick(Event event) throws IOException {
        Part part = (Part) partSearchTable.getSelectionModel().getSelectedItem();

        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select the part you wish to associate with this product.");
            alert.showAndWait();
            return;
        }

        productParts.add(part);
    }

    /**
     * Handle search part click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleSearchPartClick(ActionEvent event) throws IOException {

        // @todo remove duplicated search logic

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


    /**
     * This method parses the data from the product form into an object.
     * Some data type validation is done to prevent exceptions.
     *
     * @return Product constructed from the form data
     * @implNote For max, min fields we set the value to -2 to indicate that an invalid value was passed.
     */
    private Product parseForm() {
        Product product = new Product();

        if (id.getText().length() != 0) {
            product.setProductID(Integer.valueOf(id.getText()));
        }

        product.setName(name.getText());
        if (inv.getText().matches("\\d+")) {
            product.setInStock(Integer.valueOf(inv.getText()));
        }

        if (price.getText().matches("[0-9]+\\.?[0-9]*")) {
            product.setPrice(Double.valueOf(price.getText()));
        }

        if (min.getText().matches("\\d+")) {
            product.setMin(Integer.valueOf(min.getText()));
        } else if (!min.getText().equals("")) {
            product.setMin(-2);
        }

        if (max.getText().matches("\\d+")) {
            product.setMax(Integer.valueOf(max.getText()));
        } else if (!max.getText().equals("")) {
            product.setMax(-2);
        }

        // Add all parts from the product parts list to the product.
        productParts.forEach(product::addAssociatedPart);

        return product;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        // Initialize the part search table to include all parts
        filteredParts = FXCollections.observableList(getInventory().getAllParts());

        // Bind partSearchTable columns to properties
        searchPartID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("partID"));
        searchPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        searchPartInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("inStock"));
        searchPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        // Bind the part table to the allParts observable list in Inventory
        partSearchTable.setItems(filteredParts);

        // Bind productPartsTable columns to properties
        productPartID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("partID"));
        productPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        productPartInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("inStock"));
        productPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        // Bind the part table to the allParts observable list in Inventory
        productPartsTable.setItems(productParts);
    }
}
