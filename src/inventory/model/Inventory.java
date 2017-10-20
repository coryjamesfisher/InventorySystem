package inventory.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class Inventory {

    private ObservableList<Product> products = FXCollections.observableArrayList();
    private ObservableList<Part> allParts = FXCollections.observableArrayList();

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean removeProduct(int productID) {
        Optional<Product> product = products.stream()
                .filter(prod -> prod.getProductID() == productID)
                .findFirst();

        if (!product.isPresent()) {
            return false;
        }

        products.remove(product.get());
        return true;
    }

    public Product lookupProduct(int productID) {
        return products.stream()
                .filter(product -> product.getProductID() == productID)
                .findFirst().orElse(null);
    }

    public void updateProduct(int productID) {
        // uml wtf?
    }

    public void addPart(Part part) {

        // Get max partID + 1
        int partID = allParts.stream()
                .mapToInt(p -> p.getPartID())
                .max()
                .orElse(0) + 1;

        // Set partID
        part.setPartID(partID);

        // Add part to list
        allParts.add(part);
    }

    public boolean deletePart(int partID) {
        Optional<Part> part = allParts.stream()
                .filter(p -> p.getPartID() == partID)
                .findFirst();

        if (!part.isPresent()) {
            return false;
        }

        allParts.remove(part.get());
        return true;
    }

    public Part lookupPart(int partID) {
        return allParts.stream()
                .filter(part -> part.getPartID() == partID)
                .findFirst().orElse(null);
    }

    public void updatePart(int partID) {
        // uml wtf?
    }

    public ObservableList<Part> getAllParts() {
        return allParts;
    }
}
