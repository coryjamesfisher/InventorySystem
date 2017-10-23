package inventory.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Inventory {

    private List<Product> products = new ArrayList<>();
    private List<Part> allParts = new ArrayList<>();

    public void addProduct(Product product) {
        // Get max partID + 1
        int productID = products.stream()
                .mapToInt(p -> p.getProductID())
                .max()
                .orElse(0) + 1;

        // Set productID
        product.setProductID(productID);

        // Add product to list
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

    public List<Product> lookupProduct(String productName) {
        return products.stream()
                .filter(product -> product.getName().toUpperCase().contains(productName.toUpperCase()))
                .collect(Collectors.toList());
    }

    public void updateProduct(int productID, Product product) {
        // uml wtf

        Integer productIndex = products.indexOf(lookupProduct(productID));

        if (productIndex == -1) {
            return;
        }

        products.set(productIndex, product);
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

    public List<Part> lookupPart(String name) {
        return allParts.stream()
                .filter(part -> part.getName().toUpperCase().contains(name.toUpperCase()))
                .collect(Collectors.toList());
    }

    public void updatePart(int partID, Part part) {
        // uml wtf?
        Integer partIndex = allParts.indexOf(lookupPart(partID));

        if (partIndex == -1) {
            return;
        }

        allParts.set(partIndex, part);
    }



    public List<Part> getAllParts() {
        return allParts;
    }

    public List<Product> getProducts() {
        return products;
    }
}
