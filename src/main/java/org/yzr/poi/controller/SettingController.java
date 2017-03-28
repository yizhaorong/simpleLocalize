package org.yzr.poi.controller;

import com.sun.javafx.stage.StageHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.yzr.poi.utils.Constant;
import org.yzr.poi.utils.PropertiesManager;

/**
 * Created by yizhaorong on 2017/3/26.
 */
public class SettingController extends VBox {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private VBox box;

    @FXML private Label label;

    @FXML private CheckBox ignoreChinese;

    @FXML private CheckBox ignoreAndroidEnglish;

    @FXML private CheckBox useDefault;

    @FXML private TextField defaultValue;

    public void init() {
        box.setOnMousePressed((MouseEvent event) -> {
            event.consume();
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        box.setOnMouseDragged((MouseEvent event) -> {
            event.consume();
            getSettingStage().setX(event.getScreenX() - xOffset);

            //根据自己的需求，做不同的判断
            if (event.getScreenY() - yOffset < 0) {
                getSettingStage().setY(0);
            } else {
                getSettingStage().setY(event.getScreenY() - yOffset);
            }
        });

        setShadow(box);

        ignoreChinese.setOnAction(event -> {
            PropertiesManager.setProperty(Constant.IGNORE_CHINESE, ignoreChinese.isSelected() ? Constant.TRUE : Constant.FALSE);
        });
        ignoreChinese.setSelected(new Boolean(PropertiesManager.getProperty(Constant.IGNORE_CHINESE)));

        ignoreAndroidEnglish.setOnAction(event -> {
            PropertiesManager.setProperty(Constant.IGNORE_ENGLISH_SUFFIX, ignoreAndroidEnglish.isSelected() ? Constant.TRUE : Constant.FALSE);
        });
        ignoreAndroidEnglish.setSelected(new Boolean(PropertiesManager.getProperty(Constant.IGNORE_ENGLISH_SUFFIX)));

        useDefault.setOnAction(event -> {
            PropertiesManager.setProperty(Constant.USE_DEFAULT_VALUE, useDefault.isSelected() ? Constant.TRUE : Constant.FALSE);
//            defaultValue.setDisable(!useDefault.isSelected());
        });
        useDefault.setSelected(new Boolean(PropertiesManager.getProperty(Constant.USE_DEFAULT_VALUE)));

//        defaultValue.setDisable(!useDefault.isSelected());
//        defaultValue.setText(PropertiesManager.getProperty(Constant.DEFAULT_VALUE));
//        defaultValue.focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (!newValue) {
//                    String text = defaultValue.getText();
//                    if (text.trim().length() < 1) {
//                        text = "##我是没有翻译的##";
//                    }
//                    PropertiesManager.setProperty(Constant.DEFAULT_VALUE, text);
//                }
//            }
//        });

    }

    @FXML
    protected void close() {
        Event.fireEvent(getSettingStage(), new WindowEvent(getSettingStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private Stage getSettingStage() {
        for (Stage stage : StageHelper.getStages()) {
            if (stage.getTitle().equals("设置")) {
                return stage;
            }
        }
        return null;
    }

    private void setShadow(Node node) {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0);
        ds.setOffsetX(3.0);
        ds.setColor(Color.GRAY);
        ds.setRadius(5);
        node.setEffect(ds);
    }

}
