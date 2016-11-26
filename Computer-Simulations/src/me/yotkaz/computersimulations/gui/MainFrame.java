package me.yotkaz.computersimulations.gui;

import me.yotkaz.computersimulations.simulation.mathpendulum.Physics;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

/**
 * Created by yotkaz on 2014-10-19.
 */
public class MainFrame extends JFrame {
    Properties config;

    public MainFrame(Properties config) {
        this.config = config;
        setTitle(config.getProperty("title"));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize((int)(0.8 * Toolkit.getDefaultToolkit().getScreenSize().getWidth()), (int)(0.8 * Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        SimulationChanger sChanger = new SimulationChanger(getContentPane());
        MainMenuListener mListener = new MainMenuListener(config, sChanger, this);
        setJMenuBar(new MainMenuBar(config, mListener));

        setVisible(true);



    }
}
