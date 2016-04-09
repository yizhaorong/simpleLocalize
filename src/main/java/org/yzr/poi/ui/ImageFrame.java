package org.yzr.poi.ui;

import org.yzr.poi.App;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Administrator on 2016/3/28.
 */
public class ImageFrame extends JFrame{
    //得到当然屏幕的宽度和高度
    public Toolkit kit = Toolkit.getDefaultToolkit();
    public Dimension screenSize = kit.getScreenSize();
    public int screenWidth = screenSize.width;
    public int screenHeight = screenSize.height;
    //定义一个图像
    Image icon = kit.createImage(ImageFrame.class.getClassLoader().getResource("icon.png"));

    public ImageFrame(String title) {
        // 设置本地风格
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try { UIManager.setLookAndFeel(lookAndFeel);}  catch (Exception e) {}
        //设置框架的图标,可以在任务栏或alt+tab的时候显示
        this.setIconImage(icon);
        //定义用户关闭这个框架时的响应动作为关闭框架
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置框架大小不能改变
        this.setResizable(false);
        // 设置背景图片
        ImageIcon background = new ImageIcon(App.class.getClassLoader().getResource("images/window_bg.png"));// 背景图片
        JLabel label = new JLabel(background);// 把背景图片显示在一个标签里面
        // 把标签的大小位置设置为图片刚好填充整个面板
        label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
        JPanel imagePanel = (JPanel) this.getContentPane();
        imagePanel.setOpaque(false);
        // 内容窗格默认的布局管理器为BorderLayout
        imagePanel.setLayout(new FlowLayout());
        this.getLayeredPane().setLayout(null);
        // 把背景图片添加到分层窗格的最底层作为背景
        this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
        // 设置宽高
        this.setSize(background.getIconWidth(), background.getIconHeight());
        // 设置显示在正中间
        this.setLocation(screenWidth / 2 - background.getIconWidth() / 2, screenHeight / 2 - background.getIconHeight() / 2);
        // 设置绝对布局
        this.setLayout(null);
    }
}
