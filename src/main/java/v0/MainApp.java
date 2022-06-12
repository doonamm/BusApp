package v0;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage mainStage;

    public void start(Stage stage) throws Exception{
        mainStage = stage;
        mainStage.setTitle("BusApp");

        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Scene scene = new Scene(root, 900, 500);
        scene.getStylesheets().add(getClass().getResource("style/MainStyle.css").toString());

        mainStage.setScene(scene);
        mainStage.show();
    }
}
