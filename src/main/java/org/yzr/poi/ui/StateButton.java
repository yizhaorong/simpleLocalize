package org.yzr.poi.ui;


import org.yzr.poi.App;

import javax.swing.*;

/**
 * Created by Administrator on 2016/3/30.
 */
public class StateButton extends ImageButton {

    private String defaultIconName;

    private String defaultIconPressedIconName;

    private String selectedIconName;

    private String selectedIconPressedName;

    public StateButton(String defaultIconName) {
        super(defaultIconName);
    }

    public StateButton(String defaultIconName,String disableIconName) {
        super(defaultIconName + "_normal.png", disableIconName + "_normal.png");
        this.defaultIconName = defaultIconName + "_normal.png";
        this.defaultIconPressedIconName = defaultIconName + "_h.png";
        this.selectedIconName = disableIconName + "_normal.png";
        this.selectedIconPressedName = disableIconName + "_h.png";
        ImageIcon rolloverIcon = new ImageIcon(App.class.getClassLoader().getResource(this.defaultIconPressedIconName));
        this.setRolloverIcon(rolloverIcon);
        this.setPressedIcon(rolloverIcon);
        ImageIcon selectedIcon = new ImageIcon(App.class.getClassLoader().getResource(this.selectedIconName));
        this.setSelectedIcon(selectedIcon);
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        if(b == false) {
            ImageIcon rolloverIcon = new ImageIcon(App.class.getClassLoader().getResource(this.defaultIconPressedIconName));
            this.setRolloverIcon(rolloverIcon);
            this.setPressedIcon(rolloverIcon);
        } else {
            ImageIcon rolloverIcon = new ImageIcon(App.class.getClassLoader().getResource(this.selectedIconPressedName));
            this.setRolloverSelectedIcon(rolloverIcon);
            this.setPressedIcon(rolloverIcon);
        }
    }
}
