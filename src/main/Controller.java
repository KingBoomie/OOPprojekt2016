package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {
    @FXML
    protected void handleButtonAction(ActionEvent event) {
        System.out.println(event.getTarget().toString());
        System.out.println(event.getSource().getClass().toGenericString());
    }
}
