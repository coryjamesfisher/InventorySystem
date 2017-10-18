package inventory.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainController extends BaseController {

    public void handleSearchPartClick(ActionEvent event) throws IOException {
    }

    @FXML
    private void handleAddPartClick(ActionEvent event) throws IOException {
        PartController controller = (PartController) nextScene(event,"../view/part.fxml");
        controller.setText("HELLO TRANSFERED NURSE");
    }

    @FXML
    public void handleModifyPartClick(ActionEvent event) throws IOException {
        PartController controller = (PartController) nextScene(event,"../view/part.fxml");
        controller.loadPart("abc123");
    }

    @FXML
    public void handleDeletePartClick(ActionEvent event) throws IOException {
    }

    @FXML
    public void handleSearchProductClick(ActionEvent event) throws IOException {
    }

    @FXML
    public void handleAddProductClick(ActionEvent event) throws IOException {
    }

    @FXML
    public void handleModifyProductClick(ActionEvent event) throws IOException {
    }

    @FXML
    public void handleDeleteProductClick(ActionEvent event) throws IOException {
    }

    @FXML
    public void handleExitClick(ActionEvent event) throws IOException {
    }
}
