package org.yzr.poi.controller;

import com.sun.javafx.stage.StageHelper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.yzr.poi.model.ButtonState;
import org.yzr.poi.utils.Constant;
import org.yzr.poi.utils.PropertiesManager;
import org.yzr.poi.view.*;

/**
 * Created by yizhaorong on 2017/3/26.
 */
public class SettingController extends VBox {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private VBox box;

    @FXML private org.yzr.poi.view.Button closeButton;

//    @FXML private CheckBox ignoreChinese;

    @FXML private CheckBox ignoreAndroidEnglish;

    @FXML private CheckBox fixAndroidIdLanguage;

    @FXML private CheckBox useNewAndroid;

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

        closeButton.setImage("/images/close_normal.png", ButtonState.Normal);

        ignoreAndroidEnglish.setOnAction(event -> {
            PropertiesManager.setProperty(Constant.IGNORE_ENGLISH_SUFFIX, ignoreAndroidEnglish.isSelected() ? Constant.TRUE : Constant.FALSE);
        });
        ignoreAndroidEnglish.setSelected(new Boolean(PropertiesManager.getProperty(Constant.IGNORE_ENGLISH_SUFFIX)));

        fixAndroidIdLanguage.setOnAction(event -> {
            PropertiesManager.setProperty(Constant.FIX_ID_LANGUAGE, fixAndroidIdLanguage.isSelected() ? Constant.TRUE : Constant.FALSE);
        });
        fixAndroidIdLanguage.setSelected(new Boolean(PropertiesManager.getProperty(Constant.FIX_ID_LANGUAGE)));

        useNewAndroid.setOnAction(event -> {
            PropertiesManager.setProperty(Constant.USE_NEW_ANDROID, useNewAndroid.isSelected() ? Constant.TRUE : Constant.FALSE);
        });
        useNewAndroid.setSelected(new Boolean(PropertiesManager.getProperty(Constant.USE_NEW_ANDROID)));

        useDefault.setOnAction(event -> {
            PropertiesManager.setProperty(Constant.USE_DEFAULT_VALUE, useDefault.isSelected() ? Constant.TRUE : Constant.FALSE);
        });
        useDefault.setSelected(new Boolean(PropertiesManager.getProperty(Constant.USE_DEFAULT_VALUE)));
    }

    @FXML
    protected void close() {
        Event.fireEvent(getSettingStage(), new WindowEvent(getSettingStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public static Stage getSettingStage() {
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
