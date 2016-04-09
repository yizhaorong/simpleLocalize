package org.yzr.poi.ui;

import org.yzr.poi.utils.PropertiesManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by Administrator on 2016/3/28.
 */
public class ImageButton extends JButton {

    public ImageButton(String defaultIconName){
        ImageIcon defaultIcon = new ImageIcon(ImageButton.class.getClassLoader().getResource(defaultIconName));
        this.setIcon(defaultIcon);
        this.setSize(defaultIcon.getIconWidth(), defaultIcon.getIconHeight());
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setBorder(null);
    }

    public ImageButton(String defaultIconName,String disableIconName) {
        this(defaultIconName);
        ImageIcon disableIcon = new ImageIcon(ImageButton.class.getClassLoader().getResource(disableIconName));
        this.setDisabledIcon(disableIcon);
    }

}
