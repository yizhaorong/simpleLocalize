package org.yzr.poi.controller;

import com.sun.javafx.stage.StageHelper;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.yzr.poi.model.CopyWriteContainer;
import org.yzr.poi.utils.*;
import org.yzr.poi.view.Button;
import org.yzr.poi.model.ButtonState;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MainController {

    @FXML private Label filePathLabel;

    @FXML private Button openFileButton;

    @FXML private Button androidButton;

    @FXML private Button iOSButton;

    @FXML private Button serverButton;

    @FXML private Button generateButton;

    @FXML private Pane maskView;

    @FXML private ImageView loading;

    @FXML private FlowPane mainPane;

    @FXML private Button settingButton;

    @FXML private Button minButton;

    @FXML private Button closeButton;

    @FXML private Button showLostButton;

    private ObservableList<String> lostList = FXCollections.observableArrayList();

    public void init() {

        filePathLabel.setText("");

        mainPane.setOnDragOver(new DragOverEvent());
        mainPane.setOnDragDropped(new DragDroppedEvent(filePathLabel));
        setShadow(mainPane);

        settingButton.setImage("/images/setting_normal.png", ButtonState.Normal);
        settingButton.setImage("/images/setting_normal_h.png", ButtonState.Highlight);
        settingButton.setMouseClicked(event -> setting());

        minButton.setImage("/images/min_normal.png", ButtonState.Normal);
        minButton.setMouseClicked(event -> min());

        closeButton.setImage("/images/close_normal.png", ButtonState.Normal);
        closeButton.setMouseClicked(event -> close());

        openFileButton.setOnAction(event -> {
            File file = FileChooser.getFileFromFileChooser();
            if (file != null) {
                filePathLabel.setText(file.getAbsolutePath());
            }
        });

        openFileButton.setOnMouseEntered(event -> {
            ScaleTransition scale = new ScaleTransition(new Duration(250), openFileButton);
            scale.setFromX(1);
            scale.setFromY(1);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.setCycleCount(1);
            scale.play();
        });

        openFileButton.setOnMouseExited(event -> {
            ScaleTransition scale = new ScaleTransition(new Duration(250), openFileButton);
            scale.setFromX(1.1);
            scale.setFromY(1.1);
            scale.setToX(1);
            scale.setToY(1);
            scale.setCycleCount(1);
            scale.play();
        });
        
        androidButton.setImage("/images/Android_normal.png", ButtonState.Normal);
        androidButton.setImage("/images/Android_disable.png", ButtonState.Selected);
        androidButton.setMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                PropertiesManager.setProperty(Constant.ANDROID_SWITCH, androidButton.isSelected() ? Constant.FALSE : Constant.TRUE);
            }
        });
        androidButton.setSelected(!new Boolean(PropertiesManager.getProperty(Constant.ANDROID_SWITCH)));

        iOSButton.setImage("/images/iOS_normal.png", ButtonState.Normal);
        iOSButton.setImage("/images/iOS_disable.png", ButtonState.Selected);
        iOSButton.setMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                PropertiesManager.setProperty(Constant.IOS_SWITCH, iOSButton.isSelected() ? Constant.FALSE : Constant.TRUE);
            }
        });
        iOSButton.setSelected(!new Boolean(PropertiesManager.getProperty(Constant.IOS_SWITCH)));

        serverButton.setImage("/images/server_normal.png", ButtonState.Normal);
        serverButton.setImage("/images/server_disable.png", ButtonState.Selected);
        serverButton.setMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                PropertiesManager.setProperty(Constant.SERVER_SWITCH, serverButton.isSelected() ? Constant.FALSE : Constant.TRUE);
            }
        });
        serverButton.setSelected(!new Boolean(PropertiesManager.getProperty(Constant.SERVER_SWITCH)));

        generateButton.setImage("/images/generate.png", ButtonState.Normal);
        generateButton.setMouseClicked(event -> {
            System.out.println("生成");

            if (generateButton.isSelected()) {
                maskView.setVisible(true);
                new Thread(() -> {
                    try {
                        List<CopyWriteContainer> copyWriteContainers = ExcelUtils.read(filePathLabel.getText());
                        CopyWriteContainer defaultCopyWriteContainer = new CopyWriteContainer();
                        for (CopyWriteContainer copyWriteContainer : copyWriteContainers) {
                            if (copyWriteContainer.getLanguage().equalsIgnoreCase("cn")) {
                                defaultCopyWriteContainer = copyWriteContainer;
                                break;
                            }
                        }
                        for (CopyWriteContainer copyWriteContainer : copyWriteContainers) {
                            ExcelUtils.generate(copyWriteContainer, defaultCopyWriteContainer);
                        }

                        lostList.clear();
                        for (CopyWriteContainer copyWriteContainer: copyWriteContainers) {
                            int lostCount = copyWriteContainer.getLostCopyWrites().size();
                            if (lostCount > 0) {
                                lostList.add(copyWriteContainer.getLanguage() + "缺失" + lostCount + "个文案");
                                for (String key : copyWriteContainer.getLostCopyWrites()) {
                                    System.out.println(key);
                                    lostList.add(key);
                                }
                            }
                        }

                        if (lostList.size() > 0) {
                            showLostButton.setVisible(true);
                        } else {
                            showLostButton.setVisible(false);
                        }

                        if (copyWriteContainers.size() > 0) {
                            FileUtils.showInFinder();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        maskView.setVisible(false);
                        generateButton.setSelected(false);
                    });
                }).start();
            }
        });

        generateButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ScaleTransition scale = new ScaleTransition(new Duration(250), generateButton);
                scale.setFromX(1);
                scale.setFromY(1);
                scale.setToX(1.02);
                scale.setToY(1.02);
                scale.setCycleCount(1);
                scale.play();
            }
        });

        generateButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ScaleTransition scale = new ScaleTransition(new Duration(250), generateButton);
                scale.setFromX(1.02);
                scale.setFromY(1.02);
                scale.setToX(1);
                scale.setToY(1);
                scale.setCycleCount(1);
                scale.play();
            }
        });

        double loadingFitWidth = loading.getImage().getWidth();
        double loadingFitHeight = loading.getImage().getHeight();

        double maskViewPrefWidth = maskView.getPrefWidth();
        double maskViewPrefHeight = maskView.getPrefHeight();

        loading.setLayoutX((maskViewPrefWidth - loadingFitWidth) / 2);
        loading.setLayoutY((maskViewPrefHeight - loadingFitHeight) / 2);
    }


    @FXML
    protected void close() {
        for (Stage stage : StageHelper.getStages()) {
            if (stage.getTitle().equals("Main")) {
                Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                Platform.exit();
                return;
            }
        }
    }

    @FXML
    protected void min() {
        Stage stage = StageHelper.getStages().get(0);
        stage.setIconified(true);
    }

    protected void setting() {
        settingButton.setSelected(false);
        if (SettingController.getSettingStage() != null) {
            SettingController.getSettingStage().show();
            SettingController.getSettingStage().toFront();
            return;
        }
        try {
            Stage settingStage = new Stage();
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/layout/setting_stage.fxml"));
            Parent root = loader.load();
            SettingController settingController = loader.getController();
            settingController.init();
            settingStage.initStyle(StageStyle.TRANSPARENT);
            settingStage.setTitle("设置");
            settingStage.setScene(new Scene(root));
            settingStage.setResizable(false);
            settingStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/setting_icon.png")));

            settingStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void showLostStage() {
        if (LostController.getLostStage() != null) {
            LostController.getLostStage().show();
            LostController.getLostStage().toFront();
            return;
        }
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/layout/lost_stage.fxml"));
            Parent root = loader.load();
            LostController lostController = loader.getController();
            lostController.init(lostList);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Lost");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/setting_icon.png")));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setShadow(Node node) {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(2.0);
        ds.setOffsetX(2.0);
        ds.setColor(Color.GRAY);
        node.setEffect(ds);
    }

    /**
     * Created by loongshawn 2016/11/3.
     *
     * NOTE 文件拖到控件上方事件
     */
    public class DragOverEvent implements EventHandler<DragEvent> {
        public void handle(DragEvent event) {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()){
                for (int i = 0; i < dragboard.getFiles().size(); i++) {
                    File file=dragboard.getFiles().get(i);
                    String absolutePath = file.getAbsolutePath();
                    if (!file.isDirectory()) {
                        String fileExt = absolutePath.substring(absolutePath.lastIndexOf(".")).toLowerCase();
                        if (Arrays.asList(FileChooser.fileExtNames).contains(fileExt)) {
                            event.acceptTransferModes(TransferMode.ANY);
                            return;
                        }
                    }

                }
            }

        }
    }

    /**
     * Created by loongshawn 2016/11/3.
     *
     * NOTE 文件拖到控件上方，鼠标松开事件
     */
    private class DragDroppedEvent implements EventHandler<DragEvent> {

        private Label filePathLabel;

        public DragDroppedEvent(Label filePathLabel){
            this.filePathLabel = filePathLabel;
        }

        public void handle(DragEvent event) {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()){
                try {
                    File file = dragboard.getFiles().get(0);
                    if (file != null) {
                        filePathLabel.setText(file.getAbsolutePath());
                    }
                }catch (Exception e){

                }
            }
        }
    }
}
