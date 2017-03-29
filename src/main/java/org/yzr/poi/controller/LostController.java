package org.yzr.poi.controller;

import com.sun.javafx.stage.StageHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by yizhaorong on 2017/3/28.
 */
public class LostController {
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private Pane mainPane;

    @FXML private ListView<String> list;

    public void init(ObservableList<String> data) {

        mainPane.setOnMousePressed((MouseEvent event) -> {
            event.consume();
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        mainPane.setOnMouseDragged((MouseEvent event) -> {
            event.consume();
            getLostStage().setX(event.getScreenX() - xOffset);

            //根据自己的需求，做不同的判断
            if (event.getScreenY() - yOffset < 0) {
                getLostStage().setY(0);
            } else {
                getLostStage().setY(event.getScreenY() - yOffset);
            }
        });

        list.setItems(data);
        list.setCellFactory((ListView<String> l) -> new ColorRectCell());
    }

    @FXML
    protected void close() {
        Event.fireEvent(getLostStage(), new WindowEvent(getLostStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public static Stage getLostStage() {
        for (Stage stage : StageHelper.getStages()) {
            if (stage.getTitle().equals("Lost")) {
                return stage;
            }
        }
        return null;
    }

    static class ColorRectCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            Label label = new Label(item);
            if (item != null) {
                if (item.contains("缺失")) {
                    label.setTextFill(Paint.valueOf("RED"));
                } else {
                    label.setTextFill(Paint.valueOf("BLACK"));
                }
                setGraphic(label);
            } else {
                setGraphic(null);
            }
        }
    }
}
