package inventory.controller;

import inventory.model.Inhouse;
import inventory.model.Outsourced;
import inventory.model.Part;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

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
    }

    @FXML
    public void handleSaveClick(Event event) {
        Part part = parseForm();
        getInventory().addPart(part);
    }

    private Part parseForm() {
        Part part = inHouse.isSelected() ? new Inhouse() : new Outsourced();
        part.setName(name.getText());
        part.setInStock(Integer.valueOf(inv.getText()));
        part.setPrice(Double.valueOf(price.getText()));
        part.setMax(Integer.valueOf(max.getText()));
        part.setMin(Integer.valueOf(min.getText()));

        if (inHouse.isSelected()) {
            ((Inhouse) part).setMachineID(Integer.valueOf(machID.getText()));
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
