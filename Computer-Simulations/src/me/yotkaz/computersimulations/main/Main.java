package me.yotkaz.computersimulations.main;

import me.yotkaz.computersimulations.gui.MainFrame;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by yotkaz on 2014-11-11.
 */
public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainFrame(new MainConfig().getConfigProperties());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
