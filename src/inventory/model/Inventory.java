package inventory.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Inventory {

    /**
     * A list of products in inventory
     */
    private List<Product> products = new ArrayList<>();

    /**
     * A list of parts in inventory.
     */
    private List<Part> allParts = new ArrayList<>();

    /**
     * Add product to the inventory.
     * @param product - The product to add.
     */
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

    /**
     * Remove product from the inventory.
     *
     * @param productID - The product to remove.
     * @return On success true, on product not found, false.
     */
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

    /**
     * Method to lookup a product by id.
     *
     * @param productID - Product id to look up
     * @return Product if found, else null.
     */
    public Product lookupProduct(int productID) {
        return products.stream()
                .filter(product -> product.getProductID() == productID)
                .findFirst().orElse(null);
    }

    /**
     * Method to lookup a product by name.
     *
     * @param productName - Product name to look up
     * @return List of products where the name at least partially matches.
     * @implNote The match is case insensitive.
     */
    public List<Product> lookupProduct(String productName) {
        return products.stream()
                .filter(product -> product.getName().toUpperCase().contains(productName.toUpperCase()))
                .collect(Collectors.toList());
    }

    /**
     * Method to update a product.
     *
     * @param productID - Product ID to update
     * @param product - Product to set in the looked up product's place
     * @implNote The UMNL called for a method that only took an int, but that didn't make sense since the method wouldn't know
     * which data to update it with. I have changed the signature to int, Product
     */
    public void updateProduct(int productID, Product product) {

        Integer productIndex = products.indexOf(lookupProduct(productID));

        if (productIndex == -1) {
            return;
        }

        products.set(productIndex, product);
    }

    /**
     * Method to add a part to inventory.
     *
     * @param part - The part to add
     */
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

    /**
     * Method to delete a part from inventory.
     *
     * @param partID - The partID to delete.
     * @return
     */
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

    /**
     * Method to lookup the part by id.
     *
     * @param partID - partID to look up.
     * @return Part matching the ID, else null.
     */
    public Part lookupPart(int partID) {
        return allParts.stream()
                .filter(part -> part.getPartID() == partID)
                .findFirst().orElse(null);
    }

    /**
     * Method to search for products by name
     *
     * @param name - Name to match product on.
     * @return Parts at least partially matching on name.
     */
    public List<Part> lookupPart(String name) {
        return allParts.stream()
                .filter(part -> part.getName().toUpperCase().contains(name.toUpperCase()))
                .collect(Collectors.toList());
    }

    /**
     * Method to update a part.
     *
     * @param partID - PartID to update.
     * @param part - Part to replace it with.
     * @implNote The UML called for only using partID, but that didn't make sense since the method needs to know which
     * data to update it with. I have changed the signature to int, part.
     */
    public void updatePart(int partID, Part part) {
        Integer partIndex = allParts.indexOf(lookupPart(partID));

        if (partIndex == -1) {
            return;
        }

        allParts.set(partIndex, part);
    }

    /**
     * Get all parts in inventory.
     * @return List<Part>
     */
    public List<Part> getAllParts() {
        return allParts;
    }

    /**
     * Get all products in inventory.
     * @return List<Product>
     */
    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getProductsContainingPart(int partID) {
        return getProducts().stream()
                .filter(p -> p.lookupAssociatedPart(partID) != null)
                .collect(Collectors.toList());
    }
}
