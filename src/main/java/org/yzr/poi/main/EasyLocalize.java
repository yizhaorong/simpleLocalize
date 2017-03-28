package org.yzr.poi.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.yzr.poi.controller.MainController;
import org.yzr.poi.utils.Constant;

import java.net.URL;

public class EasyLocalize extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/layout/main_stage.fxml"));
        Parent root = loader.load();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Main");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        Image icon = new Image(this.getClass().getResourceAsStream("/images/icon.png"));
        primaryStage.getIcons().add(icon);

        MainController controller = loader.getController();
        controller.init();

        root.setOnMousePressed((MouseEvent event) -> {
            event.consume();
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            event.consume();
            primaryStage.setX(event.getScreenX() - xOffset);

            //根据自己的需求，做不同的判断
            if (event.getScreenY() - yOffset < 0) {
                primaryStage.setY(0);
            } else {
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
