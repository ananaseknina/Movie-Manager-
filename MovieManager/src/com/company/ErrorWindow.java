package com.company;

import javax.swing.*;
import java.awt.*;

public class ErrorWindow extends JFrame {

    String iconImageURL = "C:\\Users\\nina\\IdeaProjects\\MovieManager\\programming.png";
    String errorMessageImageURL = "C:\\Users\\nina\\IdeaProjects\\MovieManager\\maxresdefault.jpg\\";
    int windowWidth = 400;
    int getWindowHeight = 160;

    public ErrorWindow(){

        ImageIcon img = new ImageIcon(iconImageURL);
        this.setIconImage(img.getImage());
        this.setLayout(new BorderLayout());
        this.add(new JLabel(new ImageIcon(errorMessageImageURL)));
        this.setSize(windowWidth,getWindowHeight);
        this.setResizable(false);
        this.setVisible(true);
    }
}
