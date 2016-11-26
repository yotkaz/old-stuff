package me.yotkaz.computersimulations.gui;

import javax.swing.*;
import java.util.Properties;

/**
 * Created by yotkaz on 2014-11-11.
 */
public class MainMenuBar extends JMenuBar {

    Properties config;

    JMenu appMenu;
    JMenu simulationMenu;

    JMenuItem[] appMenuArray;
    JMenuItem[] simMenuArray;

    MainMenuListener mListener;

    private void prepareMenu(){
        appMenu = new JMenu ("App");
        appMenuArray = new JMenuItem[] {
            createMenuItem("Info", "info"),
            createMenuItem("Exit", "exit")
        };
        createMenu(appMenu, appMenuArray);

        simulationMenu = new JMenu ("Simulation");
        simMenuArray = new JMenuItem[] {
            createMenuItem("Projectile Motion", "projectile_motion"),
            createMenuItem("Mathematical Pendulum", "math_pendulum"),
            createMenuItem("Inclined Plane", "inclined_plane")
        };
        createMenu(simulationMenu, simMenuArray);

    }

    private JMenuItem createMenuItem(String name, String command){
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(mListener);
        item.setActionCommand(command);
        return item;
    }

    public MainMenuBar(Properties config, MainMenuListener mListener){
        this.config = config;
        this.mListener = mListener;
        prepareMenu();
    }

    /**
     *
     * @param menu menu to add to MenuBar
     * @param menuArray array containing items to add to menu
     * @see javax.swing.JMenu
     * @see javax.swing.JMenuItem
     *
     */
    private void createMenu(JMenu menu, JMenuItem[] menuArray){
        for(JMenuItem menuItem : menuArray){
            menu.add(menuItem);
        }
        this.add(menu);
    }
}
