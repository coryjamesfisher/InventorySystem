package inventory.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Product {
    private List<Part> associatedParts = new ArrayList<>();
    private int productID;
    private String name;
    private double price = -1;
    private int inStock = 0;
    private int min = -1;
    private int max = -1;

    /**
     * Method to add an associated part to the product.
     * @param part - The part to associate.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * This method will remove an associated part from the product.
     * This method is required by the UML but it is not used because my application
     * makes one atomic save of the data on click of the save button.
     *
     * @param partID - The partID to remove the association for.
     * @return true if success, false if part wasn't associated.
     */
    public boolean removeAssociatedPart(int partID) {
        Optional<Part> part = associatedParts.stream()
                .filter(p -> p.getPartID() == partID)
                .findFirst();

        if (!part.isPresent()) {
            return false;
        }

        associatedParts.remove(part.get());
        return true;
    }

    /**
     * This method looks up an associated part by ID.
     *
     * @param partID - Part ID to look up.
     * @return Part matching partID.
     */
    public Part lookupAssociatedPart(int partID) {
        return associatedParts.stream()
                .filter(part -> part.getPartID() == partID)
                .findFirst().orElse(null);
    }

    public List<Part> getAssociatedParts() {
        return associatedParts;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
