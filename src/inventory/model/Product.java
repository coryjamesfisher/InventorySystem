package inventory.model;

import java.util.ArrayList;
import java.util.Optional;

public class Product {
    private ArrayList<Part> associatedParts;
    private int productID;
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;

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
