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

    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

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
