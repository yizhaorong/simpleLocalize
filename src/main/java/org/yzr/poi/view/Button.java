package org.yzr.poi.view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.yzr.poi.model.ButtonState;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by yizhaorong on 2017/3/26.
 */
public class Button extends javafx.scene.control.Button {
    // 是否选中
    private boolean selected = false;
    // 是否是高亮
    private boolean highlight = false;
    // 当前状态
    private ButtonState state;
    // 普通状态下图标
    private String normalImageName;
    // 高亮状态图标
    private String highlightImageName;
    // 选中状态图标
    private String selectedImageName;
    // 不可点状态图标
    private String disabledImageName;

    public Button() {
        setStyle("-fx-background-color: transparent;");
        EventHandler highlightHandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                Image image = loadImage(highlightImageName);

                if (image == null) {
                    if (isSelected()) {
                        if (selectedImageName == null) {
                            return;
                        }
                        String imageName = selectedImageName.substring(0, selectedImageName.lastIndexOf("."));
                        String extension = selectedImageName.substring(selectedImageName.lastIndexOf("."), selectedImageName.length());
                        imageName = imageName + "_h" + extension;
                        image = loadImage(imageName);
                        if (image != null) {
                            setGraphic(new ImageView(image));
                        }
                    } else {
                        if (normalImageName == null) {
                            return;
                        }
                        String imageName = normalImageName.substring(0, normalImageName.lastIndexOf("."));
                        String extension = normalImageName.substring(normalImageName.lastIndexOf("."), normalImageName.length());
                        imageName = imageName + "_h" + extension;
                        image = loadImage(imageName);
                        if (image != null) {
                            setGraphic(new ImageView(image));
                        }
                    }
                } else {
                    setGraphic(new ImageView(image));
                }

            }
        };

        EventHandler normalHandler = new EventHandler() {
            @Override
            public void handle(Event event) {

                if (isSelected()) {
                    if (selectedImageName == null) {
                        return;
                    }
                    Image image = loadImage(selectedImageName);
                    if (image != null) {
                        setGraphic(new ImageView(image));
                    }
                } else {
                    if (normalImageName == null) {
                        return;
                    }
                    Image image = loadImage(normalImageName);
                    if (image != null) {
                        setGraphic(new ImageView(image));
                    }
                }
            }
        };

        setOnMouseEntered(highlightHandler);
        setOnMouseExited(normalHandler);
    }

    public void setImage(String imageName, ButtonState state) {
        switch (state) {
            case Normal: {
                normalImageName = imageName;
            } break;
            case Highlight:{
                highlightImageName = imageName;
            } break;
            case Selected:{
                selectedImageName = imageName;
            } break;
            case Disabled:{
                disabledImageName = imageName;
            } break;
        }
    }

    public void setMouseClicked(EventHandler<? super MouseEvent> value) {
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setSelected(!isSelected());
                value.handle(event);
            }
        });
    }

    private Image loadImage(String imageName) {
        Image image = null;
        try {
            image = new Image(getClass().getResourceAsStream(imageName));
        } catch (Exception e) {

        }
        return image;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            Image image = loadImage(selectedImageName);
            if (image != null) {
                setGraphic(new ImageView(image));
            }
        } else {
            Image image = loadImage(normalImageName);
            if (image != null) {
                setGraphic(new ImageView(image));
            }
        }

    }

    public boolean isSelected() {
        return selected;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public boolean isHighlight() {
        return highlight;
    }
}
