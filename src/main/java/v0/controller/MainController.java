package v0.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    protected Button homeTabBtn;
    @FXML
    protected Button routesTabBtn;
    @FXML
    protected Button loveTabBtn;

    private Button lastTabBtnClicked;

    @FXML
    protected AnchorPane homeTab;
    @FXML
    protected AnchorPane routesTab;
    @FXML
    protected AnchorPane loveTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeTabBtn.getStyleClass().add("selected");
        lastTabBtnClicked = homeTabBtn;
    }

    private void changeTabBtn(Button button){
        lastTabBtnClicked.getStyleClass().remove("selected");

        button.getStyleClass().add("selected");
        lastTabBtnClicked = button;
    }

    @FXML
    public void handleChangeTab(ActionEvent event){
        if(event.getSource() == homeTabBtn){
            homeTab.toFront();

            changeTabBtn(homeTabBtn);
        }
        else if (event.getSource() == routesTabBtn){
            routesTab.toFront();

            changeTabBtn(routesTabBtn);
        }
        else if (event.getSource() == loveTabBtn){
            loveTab.toFront();

            changeTabBtn(loveTabBtn);
        }
    }


}
