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

    private ObservableList<Part> productParts = FXCollections.observableArrayList();
    private ObservableList<Part> filteredParts;

    public void loadProduct(int productID) {
        Product product = getInventory().lookupProduct(productID);
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

        productParts.addAll(product.getAssociatedParts());
    }

    @FXML
    public void handleSaveClick(Event event) throws IOException {
        Product product = parseForm();

        List<String> errors = validateProduct(product);

        // Product invalid
        if (errors.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, String.join("\n", errors), ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }

        if (product.getProductID() != 0) {
            getInventory().updateProduct(product.getProductID(), product);

        } else {
            getInventory().addProduct(product);
        }

        goHome(event);
    }

    private List<String> validateProduct(Product product) {

        List<String> errors = new ArrayList<>();
        if (product.getMin() > product.getMax()) {
            errors.add("Minimum allowed must be less than maximum allowed.");
        }

        if (product.getInStock() > product.getMax() || product.getInStock() < product.getMin()) {
            errors.add("Inventory must be contained with in the minimum and maximum boundaries.");
        }

        if (product.getAssociatedParts().size() == 0) {
            errors.add("Must have one or more parts associated to the product.");
        }

        if (product.getName() == null || product.getName().length() == 0) {
            errors.add("Name is a required field.");
        }

        if (product.getPrice() == -1) {
            errors.add("Price is a required field.");
        }

        return errors;
    }

    public void handleDeletePartClick(Event event) throws IOException {
        Part part = (Part) productPartsTable.getSelectionModel().getSelectedItem();

        if (part != null) {
            productParts.remove(part);
        }
    }

    @FXML
    public void handleAddPartClick(Event event) throws IOException {
        Part part = (Part) partSearchTable.getSelectionModel().getSelectedItem();

        if (part != null) {
            productParts.add(part);
        }
    }

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
        }

        if (max.getText().matches("\\d+")) {
            product.setMax(Integer.valueOf(max.getText()));
        }

        // Add all parts from the product parts list to the product.
        productParts.forEach(product::addAssociatedPart);

        return product;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

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
