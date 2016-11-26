package me.yotkaz.computersimulations.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

/**
 * Created by yotkaz on 2015-01-07.
 */
public class MainMenuListener implements ActionListener {

    Properties config;
    SimulationChanger sChanger;
    JFrame frame;

    public MainMenuListener(Properties config, SimulationChanger sChanger, JFrame frame){
        this.config = config;
        this.sChanger = sChanger;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String code = e.getActionCommand();
        switch (code){
            case "info":
                new AppInfoMessage(config).showMessage();
                break;

            case "exit":
                System.exit(0);
                break;

            case "projectile_motion":
                sChanger.change(new me.yotkaz.computersimulations.simulation.projectilemotion.UserInterface());
                frame.setTitle("Computer Simulations ::: Projectile Motion");
                break;

            case "math_pendulum":
                sChanger.change(new me.yotkaz.computersimulations.simulation.mathpendulum.UserInterface());
                frame.setTitle("Computer Simulations ::: Mathematical Pendulum");
                break;

            case "inclined_plane":
                sChanger.change(new me.yotkaz.computersimulations.simulation.inclinedplane.UserInterface());
                frame.setTitle("Computer Simulations ::: Inclined Plane");
                break;
        }
    }
}
