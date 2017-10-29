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

    /**
     * All member variables below are related to the UI for the add/modify part screen.
     * They are bound to the fxml elements in part.fxml
     */
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

    /**
     * This method will load values from a part into the form elements.
     * It is used when a user clicks "Modify" part from the main screen.
     * @param partID - partID that should be loaded.
     */
    public void loadPart(int partID) {

        // Lookup the part
        Part part = getInventory().lookupPart(partID);

        // Set values from the part into the form elements
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

        // Based on Inhouse or Outsourced set appropriate fields.
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

    /**
     * Handle save click.
     *
     * @param event - UI event
     * @throws IOException
     */
    @FXML
    public void handleSaveClick(Event event) throws IOException {

        // Parse the part form.
        Part part = parseForm();

        // Validate the part and get any errors.
        List<String> errors = validatePart(part);

        // If the part is invalid display messages.
        if (errors.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, String.join("\n", errors), ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }

        // If there is a part id this is an update.
        if (part.getPartID() != 0) {
            getInventory().updatePart(part.getPartID(), part);

        } else {

            // No part id, add the part.
            getInventory().addPart(part);
        }

        // Return home.
        goHome(event);
    }

    /**
     * This method will validate the part and return any error messages.
     *
     * @param part - The part to validate
     * @return List<String> of error messages
     */
    private List<String> validatePart(Part part) {
        List<String> errors = new ArrayList<>();

        if (part.getMax() == -2) {
            errors.add("Maximum allowed is optional but when provided must be a positive integer.");
        }

        if (part.getMin() == -2) {
            errors.add("Minimum allowed is optional but when provided must be an integer >= 0.");
        }

        if (part.getMin() >= 0 && part.getMax() > 0 && part.getMin() > part.getMax()) {
            errors.add("Minimum allowed must be less than maximum allowed.");
        }

        if (part.getMin() >= 0 && part.getInStock() < part.getMin()) {
            errors.add("Inventory must be greater than the minimum.");
        }

        if (part.getMax() > 0 && part.getInStock() > part.getMax()) {
            errors.add("Inventory must be less than the maximum.");
        }

        if (part.getName() == null || part.getName().length() == 0) {
            errors.add("Name is a required field.");
        }

        if (part.getPrice() == -1) {
            errors.add("Price is a required decimal field.");
        }

        if (part instanceof Inhouse) {

            int machineID = ((Inhouse)part).getMachineID();
            if (machineID == 0) {
                errors.add("Machine ID is optional but if provided must be a positive integer.");
            }
        }

        return errors;
    }

    /**
     * This method parses the data from the part form into an object.
     * Some data type validation is done to prevent exceptions.
     *
     * @return Part constructed from the form data
     * @implNote For max, min fields we set the value to -2 to indicate that an invalid value was passed.
     */
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
        } else if (!max.getText().equals("")) {
            part.setMax(-2);
        }

        if (min.getText().matches("\\d+")) {
            part.setMin(Integer.valueOf(min.getText()));
        } else if (!min.getText().equals("")) {
            part.setMin(-2);
        }

        if (inHouse.isSelected()) {
            if (machID.getText().matches("\\d+")) {
                ((Inhouse) part).setMachineID(Integer.valueOf(machID.getText()));
            } else if (machID.getText().length() != 0) {

                // Validation error set machine id to 0.
                ((Inhouse) part).setMachineID(0);
            }
        } else {
            ((Outsourced) part).setCompanyName(companyName.getText());
        }

        return part;
    }

    /**
     * Handle inHouse click.
     *
     * @param event - UI event
     */
    @FXML
    public void handleInHouseClick(Event event) {
        outsourced.setSelected(false);
        companyName.setVisible(false);
        companyNameLabel.setVisible(false);
        machID.setVisible(true);
        machIDLabel.setVisible(true);
    }

    /**
     * Handle outsourced click.
     *
     * @param event - UI event
     */
    @FXML
    public void handleOutsourcedClick(Event event) {
        inHouse.setSelected(false);
        companyName.setVisible(true);
        companyNameLabel.setVisible(true);
        machID.setVisible(false);
        machIDLabel.setVisible(false);
    }
}
