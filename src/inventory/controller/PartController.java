package inventory.controller;

import inventory.model.Inhouse;
import inventory.model.Outsourced;
import inventory.model.Part;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartController extends BaseController {

    @FXML
    private RadioButton inHouse;

    @FXML
    private RadioButton outsourced;

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField inv;

    @FXML
    private TextField price;

    @FXML
    private TextField min;

    @FXML
    private TextField max;

    @FXML
    private TextField companyName;

    @FXML
    private Label companyNameLabel;

    @FXML
    private TextField machID;

    @FXML
    private Label machIDLabel;

    public void loadPart(int partID) {
        Part part = getInventory().lookupPart(partID);
        id.setText(String.valueOf(part.getPartID()));
        name.setText(part.getName());
        inv.setText(String.valueOf(part.getInStock()));
        price.setText(String.valueOf(part.getPrice()));

        if (part.getMin() != -1) {
            min.setText(String.valueOf(part.getMin()));
        }

        if (part.getMax() != -1) {
            max.setText(String.valueOf(part.getMax()));
        }

        if (part instanceof Inhouse) {

            if (((Inhouse) part).getMachineID() != -1) {
                machID.setText(String.valueOf(((Inhouse) part).getMachineID()));
            }
            handleInHouseClick(null);
            inHouse.setSelected(true);
            outsourced.setSelected(false);
        } else {
            companyName.setText(((Outsourced)part).getCompanyName());
            handleOutsourcedClick(null);
            inHouse.setSelected(false);
            outsourced.setSelected(true);
        }
    }

    @FXML
    public void handleSaveClick(Event event) throws IOException {
        Part part = parseForm();

        List<String> errors = validatePart(part);

        // Part invalid
        if (errors.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, String.join("\n", errors), ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }

        if (part.getPartID() != 0) {
            getInventory().updatePart(part.getPartID(), part);

        } else {
            getInventory().addPart(part);
        }

        goHome(event);
    }

    private List<String> validatePart(Part part) {
        List<String> errors = new ArrayList<>();
        if (part.getMin() != -1 && part.getMax() != -1 && part.getMin() > part.getMax()) {
            errors.add("Minimum allowed must be less than maximum allowed.");
        }

        if (part.getMin() != -1 && part.getInStock() < part.getMin()) {
            errors.add("Inventory must be greater than the minimum.");
        }

        if (part.getMax() != -1 && part.getInStock() > part.getMax()) {
            errors.add("Inventory must be less than the maximum.");
        }

        if (part.getName() == null || part.getName().length() == 0) {
            errors.add("Name is a required field.");
        }

        if (part.getPrice() == -1) {
            errors.add("Price is a required field.");
        }

        return errors;
    }

    private Part parseForm() {
        Part part = inHouse.isSelected() ? new Inhouse() : new Outsourced();

        if (id.getText().length() > 0) {
            part.setPartID(Integer.valueOf(id.getText()));
        }

        part.setName(name.getText());

        // Added safety checks around integer values to avoid NPE
        if (inv.getText().matches("\\d+")) {
            part.setInStock(Integer.valueOf(inv.getText()));
        }

        if (price.getText().matches("[0-9]+\\.?[0-9]*")) {
            part.setPrice(Double.valueOf(price.getText()));
        }

        if (max.getText().matches("\\d+")) {
            part.setMax(Integer.valueOf(max.getText()));
        }

        if (min.getText().matches("\\d+")) {
            part.setMin(Integer.valueOf(min.getText()));
        }

        if (inHouse.isSelected()) {
            if (machID.getText().matches("\\d+")) {
                ((Inhouse) part).setMachineID(Integer.valueOf(machID.getText()));
            }
        } else {
            ((Outsourced) part).setCompanyName(companyName.getText());
        }

        return part;
    }

    @FXML
    public void handleInHouseClick(Event event) {
        outsourced.setSelected(false);
        companyName.setVisible(false);
        companyNameLabel.setVisible(false);
        machID.setVisible(true);
        machIDLabel.setVisible(true);
    }

    @FXML
    public void handleOutsourcedClick(Event event) {
        inHouse.setSelected(false);
        companyName.setVisible(true);
        companyNameLabel.setVisible(true);
        machID.setVisible(false);
        machIDLabel.setVisible(false);
    }
}
