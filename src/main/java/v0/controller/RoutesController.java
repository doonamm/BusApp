package v0.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RoutesController implements Initializable {

    @FXML
    protected FlowPane flowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i=0; i<50; i++){
            Button button = new Button(String.valueOf(i));
            button.getStyleClass().add("route-btn");
            flowPane.getChildren().add(button);
        }
    }
}
