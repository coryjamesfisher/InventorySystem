package inventory.controller;

import inventory.model.Inhouse;
import inventory.model.Outsourced;
import inventory.model.Part;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.IOException;

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
        min.setText(String.valueOf(part.getMin()));
        max.setText(String.valueOf(part.getMax()));

        if (part instanceof Inhouse) {
            machID.setText(String.valueOf(((Inhouse)part).getMachineID()));
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

        if (part.getPartID() != 0) {
            getInventory().updatePart(part.getPartID(), part);

        } else {
            getInventory().addPart(part);
        }

        goHome(event);
    }

    private Part parseForm() {
        Part part = inHouse.isSelected() ? new Inhouse() : new Outsourced();

        if (id.getText().length() > 0) {
            part.setPartID(Integer.valueOf(id.getText()));
        }

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
