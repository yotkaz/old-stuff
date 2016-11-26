package me.yotkaz.computersimulations.gui;

import javax.swing.*;
import java.util.Properties;

/**
 * Created by yotkaz on 2015-01-07.
 */
public class AppInfoMessage extends JOptionPane{
    Properties config;

    AppInfoMessage(Properties config){
        this.config = config;
    }

    void showMessage(){

        String  html = "<html>";
                html += ("<h2>" + config.getProperty("title") + "</h2>");
                html += ("Author: <b>" + config.getProperty("author") + "</b><br>");
                html += ("Date: <b>" + config.getProperty("date") + "</b><br>");
                html += ("Url: <b><a href=\"" + config.getProperty("url") + "\" target=\"_blank\">" + config.getProperty("url") + "</a></b><br>");
                html += ("Mail: <b><a href=\"mailto:" + config.getProperty("mail") + "\" target=\"_blank\">" + config.getProperty("mail") + "</a></b><br>");
                html += ("<br><b>" + config.getProperty("shortinfo") + "</b><br>");
                html += "</html>";

        showMessageDialog(null, html, "Computer Simulations Info", PLAIN_MESSAGE);
    }
}
