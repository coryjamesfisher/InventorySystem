package inventory.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class PartController extends BaseController {
    @FXML
    private Text message;

    public void setText(String text) {
        message.setText(text);
    }

    public void loadPart(String part) {
        //db calls here
        message.setText(part);
    }
}
